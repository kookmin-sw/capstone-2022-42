package com.example.capstone42_sancheck.object;

public class RecyclerViewItem {
    private int drawableId; // 사진
    private String MNTN_NM; // 산 이름
    private String PMNTN_NM; // 등산로 이름
    private String PMNTN_DFFL; // 난이도

    public int getDrawableId(){ return drawableId; }
    public String getMNTN_NM() { return MNTN_NM; }
    public String getPMNTN_NM() { return PMNTN_NM; }
    public String getPMNTN_DFFL() { return PMNTN_DFFL; }

    public void setDrawableId(int drawableId){ this.drawableId = drawableId; }
    public void setMNTN_NM(String MNTN_NM) {
        this.MNTN_NM = MNTN_NM;
    }
    public void setPMNTN_NM(String PMNTN_NM) { this.PMNTN_NM = PMNTN_NM; }
    public void setPMNTN_DFFL(String PMNTN_DFFL) { this.PMNTN_DFFL = PMNTN_DFFL; }
}
