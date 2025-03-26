package com.livescore.soccerscore.matchlive.api_data.model;

import com.google.gson.annotations.SerializedName;

public class PaginationModel {
    @SerializedName("count")
    public int count;

    @SerializedName("per_page")
    public int per_page;

//    @SerializedName("current_page")
//    public int current_page;

    @SerializedName("next_page")
    public String next_page;
    @SerializedName("has_more")
    public boolean has_more;

    @Override
    public String toString() {
        return "PaginationModel{" +
                "count=" + count +
                ", per_page=" + per_page +
//                ", current_page=" + current_page +
                ", next_page=" + next_page +
                ", has_more=" + has_more +
                '}';
    }
}
