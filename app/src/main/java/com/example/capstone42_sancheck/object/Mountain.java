package com.example.capstone42_sancheck.object;

public class Mountain {
    private int index; // 인덱스
    private String M_Name; // 산명
    private String PM_Name; // 등산로명
    private String diff; // 난이도
    private double LT; // 등산로 길이
    private double uppl; // 상행 시간
    private double godn; // 하행 시간

    public int getIndex() {
        return index;
    }

    public String getM_Name() {
        return M_Name;
    }

    public String getPM_Name() {
        return PM_Name;
    }

    public double getLT() {
        return LT;
    }

    public double getGodn() {
        return godn;
    }

    public double getUppl() {
        return uppl;
    }

    public String getDiff() {
        return diff;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setM_Name(String m_Name) {
        M_Name = m_Name;
    }

    public void setPM_Name(String PM_Name) {
        this.PM_Name = PM_Name;
    }

    public void setLT(double LT) {
        this.LT = LT;
    }

    public void setUppl(double uppl) {
        this.uppl = uppl;
    }

    public void setGodn(double godn) {
        this.godn = godn;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
