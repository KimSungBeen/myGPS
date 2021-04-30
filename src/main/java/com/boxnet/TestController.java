package com.boxnet;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @RequestMapping(value = "/api/test", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getApiTest() {
        return "{\"result\":\"s_been\"}";
    }

    @RequestMapping(value = "/api/test2", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String getApiTest2() {
        return "{\"result\":\"s_been2\"}";
    }

    @RequestMapping(value = "/api/test3", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String getApiTest3(@RequestParam(value = "id") String id,
                              @RequestParam(value = "cn") String cn,
                              @RequestParam(value = "error_msg") String error_msg) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", "SUCCESS");
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("cn", cn);
        jsonObject.addProperty("error_msg", error_msg);
        return jsonObject.toString();
    }
    // localhost:8080/api/test
}
