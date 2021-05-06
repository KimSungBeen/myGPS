package com.boxnet;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.boxnet.Util.errorResult;

@RestController
public class LoginApiController {
    final UserDAO ud;

    public LoginApiController(UserDAO ud) {
        this.ud = ud;
    }

    @RequestMapping(value = "/api/getUser", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String selectUser(@RequestParam(value = "id") String id,
                             @RequestParam(value = "sns") String sns) {
        JsonObject result = new JsonObject();

        try {
            ud.selectUser(id, sns);

            result.addProperty("resultMessage", "SUCCESS");
            result.addProperty("resultCode", "0000");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return errorResult(result);
        }
    }

    @RequestMapping(value = "/api/signUp", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String insertUser(@RequestParam(value = "id") String id,
                           @RequestParam(value = "sns") String sns) {
        JsonObject result = new JsonObject();

        try {
            ud.insertUser(id, sns);

            result.addProperty("resultMessage", "SUCCESS");
            result.addProperty("resultCode", "0000");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty("resultMessage", "중복이거나 사용할 수 없는 아이디입니다.");
            result.addProperty("resultCode", "2000");
            return result.toString();
        }
    }

}
