package com.app.myhousereport;

import com.google.gson.annotations.SerializedName;

import java.security.acl.LastOwnerException;
import java.util.Map;

public class ReportModel {
    String id;
    String user;
    String title;
    long  price;
    String date;
    String description;
    String category;
    long type;// 0=hazine , 1 =daramad

    public ReportModel(String id, Map<String, Object> map) {
        this.id = id;
        this.user = (String) map.get("user");
        this.title = (String) map.get("title");
        this.price = (Long) map.get("price");
        this.date = (String) map.get("date");
        this.description = (String) map.get("description");
        this.category = (String) map.get("category");
        this.type = (Long) map.get("type");
    }
}
