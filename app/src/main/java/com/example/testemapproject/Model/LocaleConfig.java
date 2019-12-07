package com.example.testemapproject.Model;

public class LocaleConfig {
    private int id;
    private String configName;
    private Double lat;
    private Double longi;
    private int zoom;
    private int wifi;
    private int media;
    private int ring;
    private int alarm;

    public LocaleConfig(int id, String configName, Double lat, Double longi, int zoom,
                        int wifi, int media, int ring, int alarm){
        this.id = id;
        this.configName = configName;
        this.lat = lat;
        this.longi = longi;
        this.zoom = zoom;
        this.wifi = wifi;
        this.media = media;
        this.ring = ring;
        this.alarm = alarm;

    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getConfigName() {return configName;}

    public void setConfigName(String configName) {this.configName = configName;}

    public Double getLat() {return lat;}

    public void setLat(Double lat) {this.lat = lat;}

    public Double getLongi() {return longi;}

    public void setLongi(Double longi) {this.longi = longi;}

    public int getZoom() {return zoom;}

    public void setZoom(int zoom) {this.zoom = zoom;}

    public int getWifi() {return wifi;}

    public void setWifi(int wifi) {this.wifi = wifi;}

    public int getMedia() {return media;}

    public void setMedia(int media) {this.media = media;}

    public int getRing() {return ring;}

    public void setRing(int ring) {this.ring = ring;}

    public int getAlarm() {return alarm;}

    public void setAlarm(int alarm) {this.alarm = alarm;}
}

