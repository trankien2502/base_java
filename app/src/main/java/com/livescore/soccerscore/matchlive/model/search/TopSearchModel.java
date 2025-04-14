package com.livescore.soccerscore.matchlive.model.search;

public class TopSearchModel {
    public int image;
    public String name;
    public boolean isTeam;

    public TopSearchModel(int image, String name, boolean isTeam) {
        this.image = image;
        this.name = name;
        this.isTeam = isTeam;
    }

    public TopSearchModel() {
    }
}
