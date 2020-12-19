package com.aditya.pindrop;

import io.realm.RealmObject;

public class GeoFenceEntity extends RealmObject {

    private Double lat,longt;
    private String name,id;

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLongt() { return longt; }
    public void setLongt(Double longt) { this.longt = longt; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return "GeoFenceDao{" +
                "lat=" + lat +
                ", longt=" + longt +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}