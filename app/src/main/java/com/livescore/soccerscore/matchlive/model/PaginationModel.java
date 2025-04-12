package com.livescore.soccerscore.matchlive.model;

import com.google.gson.annotations.SerializedName;

public class PaginationModel {
    @SerializedName("count")
    public int count;
    @SerializedName("next_page")
    public String next_page;
    @SerializedName("has_more")
    public boolean has_more = false;

    public PaginationModel() {
    }

    @Override
    public String toString() {
        return "PaginationModel{" +
                "count=" + count +
                ", next_page=" + next_page +
                ", has_more=" + has_more +
                '}';
    }
}
