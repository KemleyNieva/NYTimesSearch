package com.codepath.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.jar.JarException;

/**
 * Created by kemleynieva on 6/20/16.
 */
@Parcel
public class Article  {
    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadlines() {
        return headlines;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    String webUrl;
    String headlines;
    String thumbnail;

    public Article(){

    }

    public Article(JSONObject jsonObject){
        try{
            this.webUrl=jsonObject.getString("web_url");
            this.headlines= jsonObject.getJSONObject("headline").getString("main");
            if(this.headlines == null){

            }

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length()>0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnail="http://www.nytimes.com/" + multimediaJson.getString("url");

            } else{
                this.thumbnail="";
            }
        }catch(JSONException e){

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();

        for(int x=0;x<array.length();x++){
            try{
                results.add(new Article(array.getJSONObject(x)));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
