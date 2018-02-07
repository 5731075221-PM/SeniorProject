package com.example.uefi.seniorproject.fragment;

/**
 * Created by UEFI on 27/12/2560.
 */
public class Hospital{
    private String name;
    private Double lat;
    private Double lng;
    private String address,phone,website,zone, distance, duration;

    public Hospital(String name, Double lat, Double lng, String address, String phone, String website, String zone) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.zone = zone;
        this.distance = "";
        this.duration = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Double getDistanceValue() {
        String[] tmp = distance.split(" ");
        System.out.println(tmp.toString());
        System.out.println(tmp[0]+" "+tmp[1]);
        return Double.parseDouble(tmp[0]);
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
