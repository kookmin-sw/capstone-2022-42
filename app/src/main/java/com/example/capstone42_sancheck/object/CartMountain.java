package com.example.capstone42_sancheck.object;

public class CartMountain {
    private String MNTN_NM; // 산 이름
    private String PMNTN_NM; // 등산로 이름
    private String PMNTN_DFFL;
    private int index;
    private int drawableId; // 사진
    private Double PMNTN_LT; // 등산로 길이
    private String START_PNT; // 시작점
    private String END_PNT; // 도착점

    public CartMountain(){

    }

    public CartMountain(String MNTN_NM, String PMNTN_NM, String PMNTN_DFFL, int drawableId, int index, double PMNTN_LT, String START_PNT, String END_PNT) {
        this.MNTN_NM = MNTN_NM;
        this.PMNTN_NM = PMNTN_NM;
        this.PMNTN_DFFL = PMNTN_DFFL;
        this.drawableId = drawableId;
        this.index = index;
        this.PMNTN_LT = PMNTN_LT;
        this.START_PNT = START_PNT;
        this.END_PNT = END_PNT;
    }

    public String getMNTN_NM() {
        return MNTN_NM;
    }

    public String getPMNTN_NM() {
        return PMNTN_NM;
    }

    public String getPMNTN_DFFL() {
        return PMNTN_DFFL;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getIndex() {
        return index;
    }

    public Double getPMNTN_LT() { return PMNTN_LT; }

    public String getSTART_PNT() { return START_PNT; }

    public String getEND_PNT() { return END_PNT; }

    public void setMNTN_NM(String MNTN_NM) {
        this.MNTN_NM = MNTN_NM;
    }

    public void setPMNTN_NM(String PMNTN_NM) {
        this.PMNTN_NM = PMNTN_NM;
    }

    public void setPMNTN_DFFL(String PMNTN_DFFL) {
        this.PMNTN_DFFL = PMNTN_DFFL;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public void setIndex(int index) { this.index = index; }

    public void setSTART_PNT(String START_PNT) { this.START_PNT = START_PNT; }

    public void setEND_PNT(String END_PNT) { this.END_PNT = END_PNT; }

    public void setPMNTN_LT(Double PMNTN_LT) { this.PMNTN_LT = PMNTN_LT; }
}
