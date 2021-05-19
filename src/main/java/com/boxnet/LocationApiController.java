package com.boxnet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.boxnet.Util.errorResult;
import static com.boxnet.Util.getDate;

@RestController
public class LocationApiController {
    final LocationDAO ld;

    // 모든 Location데이터가 저장되는 리스트
    final List<LocationDTO> allLocationDTOList = new ArrayList<>();

    public LocationApiController(LocationDAO ld) {
        this.ld = ld;
    }

    /**
     * 클라이언트에서 서버로 Location 데이터 전송 및 저장
     */
    @RequestMapping(value = "/api/saveLocationData", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String saveLocationData(@RequestParam(value = "id") String id, /*계정*/
                               @RequestParam(value = "latitude") double latitude, /*위도*/
                               @RequestParam(value = "longitude") double longitude /*경도*/) {
        JsonObject result = new JsonObject();

        try {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(id);
            locationDTO.setDate(getDate());

            locationDTO.setLatitude(latitude);
            locationDTO.setLongitude(longitude);

            synchronized (allLocationDTOList) {
                allLocationDTOList.add(locationDTO);
            }

            result.addProperty("resultMessage", "SUCCESS");
            result.addProperty("resultCode", "0000");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return errorResult(result);
        }
    }

    /**
     * allLocationDTOList에서 파라미터id의 Location데이터를 insert
     */
    public void insertLocationData(String id) {
        synchronized (allLocationDTOList) {
            List<LocationDTO> insertLocationDTOList = new ArrayList<>();

            for (int i = 0; i < allLocationDTOList.size(); i++) {
                if (allLocationDTOList.get(i).getId().equals(id)) {
                    // insert되지 않은 Location데이터 리스트에서,
                    // 파라미터 id의 Location데이터를 insert 처리
//                    ud.insertLocationData(allLocationDTOList.get(i));

                    insertLocationDTOList.add(allLocationDTOList.get(i)); // insert된 Location데이터의 index 저장 (리스트에서 제거하기 위함)
                }
            }

            ld.insertLocationDataList(insertLocationDTOList);

            for (LocationDTO locationDTO : insertLocationDTOList) {
                // insert된 Location데이터를, insert되지 않은 Location데이터 리스트에서 제거
                allLocationDTOList.remove(locationDTO);
            }
        }
    }

    /**
     * Location Data(위도, 경도, 날짜) 를 Json으로 리턴
     * <p>
     * resultCode: 0000 (SUCCESS)
     *           : 1000 (FAIL: Location데이터 없음)
     *           : 2000 (ERROR)
     */
    @RequestMapping(value = "/api/getLocationData", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String selectLocationData(@RequestParam(value = "id") String id /*계정*/) {
        JsonObject result = new JsonObject();

        try {
            insertLocationData(id); // allLocationDTOList에서 파라미터id의 Location데이터를 insert

            List<LocationDTO> locationDTOList = ld.selectLocationData(id);

            if (locationDTOList.size() > 0) {
                JsonArray jsonArray = new JsonArray();
                JsonObject jsonObject;

                for (LocationDTO locationDTO : locationDTOList) {
                    jsonObject = new JsonObject();

                    jsonObject.addProperty("latitude", locationDTO.getLatitude());
                    jsonObject.addProperty("longitude", locationDTO.getLongitude());
                    jsonObject.addProperty("date", locationDTO.getDate());

                    jsonArray.add(jsonObject);
                }
                result.addProperty("resultMessage", "SUCCESS");
                result.addProperty("resultCode", "0000");
                result.add("locationData", jsonArray);

            } else {
                result.addProperty("resultMessage", "FAIL");
                result.addProperty("resultCode", "1000");
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return errorResult(result);
        }
    }

    @RequestMapping(value = "/api/showAllLocationDataList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String showAllLocationDataList() {
        JsonObject result = new JsonObject();

        try {
            JsonArray jsonArray = new JsonArray();
            synchronized (allLocationDTOList) {
                if (allLocationDTOList.size() > 0) {
                    for (LocationDTO locationDTO : allLocationDTOList) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", locationDTO.getId());
                        jsonObject.addProperty("date", locationDTO.getDate());
                        jsonObject.addProperty("latitude", locationDTO.getLatitude());
                        jsonObject.addProperty("longitude", locationDTO.getLongitude());

                        jsonArray.add(jsonObject);
                    }

                    result.addProperty("resultMessage", "SUCCESS");
                    result.addProperty("resultCode", "0000");
                    result.add("LocationData", jsonArray);

                } else {
                    result.addProperty("resultMessage", "FAIL");
                    result.addProperty("resultCode", "1000");
                }
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return errorResult(result);
        }
    }
}
