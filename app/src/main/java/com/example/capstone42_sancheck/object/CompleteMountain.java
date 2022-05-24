package com.example.capstone42_sancheck.object;

public class CompleteMountain {
    private String monthDay;
    private String year;
    private String mountainName;
    private String pmountainName;

    public CompleteMountain(){

    }

    public CompleteMountain(String monthDay, String year, String mountainName, String pmountainName){
        this.monthDay = monthDay;
        this.year = year;
        this.mountainName = mountainName;
        this.pmountainName = pmountainName;
    }

    public String getMonthDay(){
        return monthDay;
    }

    public void setMonthDay(String monthDay){
        this.monthDay = monthDay;
    }

    public String getYear(){
        return year;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getMountainName(){
        return mountainName;
    }

    public void setMountainName(String mountainName){
        this.mountainName = mountainName;
    }

    public String getPmountainName(){
        return pmountainName;
    }

    public void setPmountainName(){
        this.pmountainName = pmountainName;
    }
}
