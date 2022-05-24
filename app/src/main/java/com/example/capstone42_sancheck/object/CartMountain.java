package com.example.capstone42_sancheck.object;

public class CartMountain {
    private String MNTN_NM; // 산 이름
    private String PMNTN_NM; // 등산로 이름
    private String PMNTN_DFFL;
    private int drawableId; // 사진

    public CartMountain(){

    }

    public CartMountain(String MNTN_NM, String PMNTN_NM, String PMNTN_DFFL, int drawableId) {
        this.MNTN_NM = MNTN_NM;
        this.PMNTN_NM = PMNTN_NM;
        this.PMNTN_DFFL = PMNTN_DFFL;
        this.drawableId = drawableId;
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
}
