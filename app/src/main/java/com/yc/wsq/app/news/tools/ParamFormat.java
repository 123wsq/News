package com.yc.wsq.app.news.tools;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParamFormat {

    public static Map<String, Object> onJsonToMap(String string) throws  Exception{

        JSONObject jsonObject = new JSONObject(string);
        int count = jsonObject.length();
        Map<String, Object> map = new HashMap<>();
        Iterator<String> iterator =  jsonObject.keys();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                JSONArray jsona = (JSONArray) value;
                for (int i = 0; i < jsona.length(); i++){

                    list.add(onJsonToMap(jsona.get(i).toString()));
                }
                map.put(key, list);
//            }else if(value instanceof JSONObject){
//                map.put(key, onJsonToMap(value.toString()));
            }else{

                if (TextUtils.isEmpty(value+"") || value.toString().equals("null")){
                    map.put(key, "");
                }else {
                    map.put(key, value);
                }

            }


        }
        return map;
    }

    /**
     * 最新
     * @param string
     * @return
     * @throws Exception
     */
    public static Map<String, Object> onAllJsonToMap(String string) throws  Exception{

        JSONObject jsonObject = new JSONObject(string);
        int count = jsonObject.length();
        Map<String, Object> map = new HashMap<>();
        Iterator<String> iterator =  jsonObject.keys();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                JSONArray jsona = (JSONArray) value;

                for (int i = 0; i < jsona.length(); i++){

                    list.add(onAllJsonToMap(jsona.get(i).toString()));
                }
                map.put(key, list);
            }else if(value instanceof JSONObject){
                map.put(key, onJsonToMap(value.toString()));
            }else{


                if (TextUtils.isEmpty(value+"") || value.toString().equals("null")){
                    map.put(key, "");
                }else {
                    map.put(key, value);
                }

            }


        }
        return map;
    }
}
