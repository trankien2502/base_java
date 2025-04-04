package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats;

public class StatsHomeAndAway {
    public long id = -1;
    public String name = "";
    public long homeValue = -1;
    public long awayValue = -1;

    @Override
    public String toString() {
        return "StatsHomeAndAway{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", homeValue=" + homeValue +
                ", awayValue=" + awayValue +
                '}';
    }
}
