package com.works.adeogo.doctor.utils;

import com.works.adeogo.doctor.model.MedicalArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ademi on 21/03/2018.
 */

public class JSONFormat {
    public static List<MedicalArticle> getObjectArray(String JSONresponse ) throws JSONException {

        List<MedicalArticle> newsList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(JSONresponse);
        JSONArray jsonArray = jsonObject.getJSONArray("articles");
        for (int i = 0; i < jsonArray.length(); i++){
            newsList.add(getMedicalNewsObject(jsonArray.getJSONObject(i)));
        }
        return newsList;
    }

    private static MedicalArticle getMedicalNewsObject(JSONObject newsObject) throws JSONException {
        MedicalArticle medicalArticle = new MedicalArticle();
        try {
            JSONObject jsonObject = newsObject.getJSONObject("source");
            medicalArticle.setSourceId(jsonObject.getString("id"));
            medicalArticle.setSourceName(jsonObject.getString("name"));

            medicalArticle.setAuthor(newsObject.getString("author"));
            medicalArticle.setTitle(newsObject.getString("title"));

            medicalArticle.setDescription(newsObject.getString("description"));
            medicalArticle.setUrl(newsObject.getString("url"));
            medicalArticle.setImageUrl(newsObject.getString("urlToImage"));
            medicalArticle.setPublishedAt(newsObject.getString("publishedAt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return medicalArticle;
    }
}