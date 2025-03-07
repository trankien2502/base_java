package com.assistivetouch.easytouch.homebutton.ui.screenshot;

import com.google.gson.annotations.SerializedName;


public class ItemVideoConfig {
    @SerializedName("advance")
    private boolean advance;
    @SerializedName("bitrateAudio")
    private int bitrateAudio;
    @SerializedName("bitrateVideo")
    private int bitrateVideo;
    @SerializedName("channels")
    private int channels;
    @SerializedName("enaAudio")
    private boolean enaAudio;
    @SerializedName("frameRate")
    private int frameRate;
    @SerializedName("quality")
    private int quality;
    @SerializedName("sampleRate")
    private int sampleRate;

    public ItemVideoConfig(boolean z, int i, boolean z2, int i2, int i3, int i4, int i5, int i6) {
        this.advance = z;
        this.quality = i;
        this.enaAudio = z2;
        this.bitrateVideo = i2;
        this.frameRate = i3;
        this.channels = i4;
        this.sampleRate = i5;
        this.bitrateAudio = i6;
    }

    public boolean isAdvance() {
        return this.advance;
    }

    public void setAdvance(boolean z) {
        this.advance = z;
    }

    public int getQuality() {
        return this.quality;
    }

    public void setQuality(int i) {
        this.quality = i;
    }

    public boolean isEnaAudio() {
        return this.enaAudio;
    }

    public void setEnaAudio(boolean z) {
        this.enaAudio = z;
    }

    public int getBitrateVideo() {
        return this.bitrateVideo;
    }

    public void setBitrateVideo(int i) {
        this.bitrateVideo = i;
    }

    public int getFrameRate() {
        return this.frameRate;
    }

    public void setFrameRate(int i) {
        this.frameRate = i;
    }

    public int getChannels() {
        return this.channels;
    }

    public void setChannels(int i) {
        this.channels = i;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(int i) {
        this.sampleRate = i;
    }

    public int getBitrateAudio() {
        return this.bitrateAudio;
    }

    public void setBitrateAudio(int i) {
        this.bitrateAudio = i;
    }
}
