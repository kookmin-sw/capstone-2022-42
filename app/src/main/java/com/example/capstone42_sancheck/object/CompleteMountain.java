package com.example.capstone42_sancheck.object;

public class CompleteMountain {
    private String monthDay;
    private String year;
    private String mountainName;
    private String pmountainName;
    private int index;
    private Double lt;
    private String START_PNT;
    private String END_PNT;

    public CompleteMountain(){

    }

    public CompleteMountain(String monthDay, String year, String mountainName, String pmountainName, int index, Double lt, String START_PNT, String END_PNT){
        this.monthDay = monthDay;
        this.year = year;
        this.mountainName = mountainName;
        this.pmountainName = pmountainName;
        this.index = index;
        this.lt = lt;
        this.START_PNT = START_PNT;
        this.END_PNT = END_PNT;
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

    public String getPmountainName(){ return pmountainName; }

    public void setPmountainName(){
        this.pmountainName = pmountainName;
    }

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public double getLt() { return lt; }

    public void setLt(double lt) { this.lt = lt; }

    public String getSTART_PNT() { return START_PNT; }

    public void setSTART_PNT(String START_PNT) { this.START_PNT = START_PNT; }

    public String getEND_PNT() { return END_PNT; }

    public void setEND_PNT(String END_PNT) { this.END_PNT = END_PNT; }
}
