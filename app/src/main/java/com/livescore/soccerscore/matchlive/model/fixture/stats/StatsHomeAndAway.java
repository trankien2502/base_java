package com.livescore.soccerscore.matchlive.model.fixture.stats;

public class StatsHomeAndAway {
    public long id = -1;
    public String name = "";
    public long homeValue = -1;
    public long awayValue = -1;

    public StatsHomeAndAway() {
    }

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
