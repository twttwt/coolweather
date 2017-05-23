package com.example.twt.myweather.util;

import android.content.ContentValues;

import com.example.twt.myweather.Bean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by twt on 2017/4/28.
 */

public class JsonParseUtils {
    public static String getJsonData() {
        URL url = null;
        String jsonData = ""; // 请求服务器返回的json字符串数据
        InputStreamReader in = null; // 读取的内容（输入流）
        try {
            url = new URL("http://guolin.tech/api/weather?cityid=CN101210411&key=18e1bc47573c4a6294d29ba137f1de94");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 这一步会连接网络得到输入流
            in = new InputStreamReader(conn.getInputStream());
            // 为输入创建BufferedReader
            BufferedReader br = new BufferedReader(in);
            String inputLine = null;
            while(((inputLine = br.readLine()) != null)){
                jsonData += inputLine;
            }
            in.close(); // 关闭InputStreamReader
            // 断开网络连接
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonData;
    }
   /* private void parseJSONWithGSON(String jsonData){
        Gson gson=new Gson();
        List<Bean> beanList=gson.fromJson(jsonData,new TypeToken<List<Bean>>(){}.getType());

    }*/
   public static ContentValues parseJSONToWeather(String jsonData) {
       ContentValues contentValues = new ContentValues();
       try {
           JSONObject jsonObject = new JSONObject(jsonData);
//此时将字符串转变为一个JSONObject实例，形象的说法就是{"key":"value"}这就是个object
           JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
//这里在此object中get了一个数组（JSONArray），输入这个数组的key，即可得到
           JSONObject allJsonObject = jsonArray.getJSONObject(0);
           String status = allJsonObject.getString("status");
           if (status.equals("ok")) {
               JSONObject basic = allJsonObject.getJSONObject("basic");
               contentValues.put("id", basic.getString("id"));
               contentValues.put("city", basic.getString("city"));
               JSONObject now = allJsonObject.getJSONObject("now");
               JSONObject now_cond = now.getJSONObject("cond");
               contentValues.put("now_cond_txt", now_cond.getString("txt"));
               contentValues.put("now_tmp", now.getString("tmp"));
               JSONArray daily_forecast = allJsonObject.getJSONArray("daily_forecast");
               for (int i = 0; i < 5; i++) {
                   String num = null;
                   switch (i){
                       case 0:
                           num = "first";
                           break;
                       case 1:
                           num = "second";
                           break;
                       case 2:
                           num = "third";
                           break;
                       case 3:
                           num = "fourth";
                           break;
                       case 4:
                           num = "fifth";
                           break;
                   }
                   JSONObject data = daily_forecast.getJSONObject(i);
                   contentValues.put(num + "_date", data.getString("date"));
                   JSONObject cond = data.getJSONObject("cond");
                   contentValues.put(num + "_txt_d", cond.getString("txt_d"));
                   contentValues.put(num + "_txt_n", cond.getString("txt_n"));
                   JSONObject tmp = data.getJSONObject("tmp");
                   contentValues.put(num + "_tmp_max", tmp.getString("max"));
                   contentValues.put(num + "_tmp_min", tmp.getString("min"));
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return contentValues;     //包含了所有的数据库信息
   }
}
