package com.example.uefi.seniorproject.alert;

/**
 * Created by palida on 13-Apr-18.
 */

public class AppointmentItem {
    public String hospital;
    public int id,hour,minute,day,month,year;

    public AppointmentItem(int id,int day,int month,int year,int hour,int minute,String hospital){
        this.id = id;
        this.day =day;
        this.month =month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.hospital = hospital;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}