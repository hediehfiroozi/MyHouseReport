package com.app.myhousereport;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.annotations.SerializedName;

import java.security.acl.LastOwnerException;
import java.util.Map;

public class ReportModel {
    String id;
    String user;
    String title;
    long price;
    String date;
    String description;
    String category;
    long type;// 0=hazine , 1 =daramad

    public ReportModel(DocumentSnapshot snapshot) {
        this.id = snapshot.getId();
        if (snapshot.getData() != null) {
            this.user = (String) snapshot.getData().get("user");
            this.title = (String) snapshot.getData().get("title");
            this.price = (Long) snapshot.getData().get("price");
            this.date = (String) snapshot.getData().get("date");
            this.description = (String) snapshot.getData().get("description");
            this.category = (String) snapshot.getData().get("category");
            this.type = (Long) snapshot.getData().get("type");
        }
    }
}
