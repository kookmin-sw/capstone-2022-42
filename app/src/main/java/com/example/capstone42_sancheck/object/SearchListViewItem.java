package com.example.capstone42_sancheck.object;

public class SearchListViewItem {
    private int index; // 인덱스
    private String MNTN_NM; // 산 이름
    private String PMNTN_NM; // 등산로 이름
    private Double PMNTN_LT; // 등산로 길이
    private Double PMNTN_UPPL; // 상행 시간
    private Double PMNTN_GODN; // 하행 시간
    private String PMNTN_DFFL; // 난이도
    private String START_PNT; // 시작점
    private String END_PNT; // 도착점
    private int PEOPLE; // 사람 수

    public int getIndex() { return index; }
    public String getMNTN_NM() { return MNTN_NM; }
    public String getPMNTN_NM() { return PMNTN_NM; }
    public Double getPMNTN_LT() { return PMNTN_LT; }
    public Double getPMNTN_UPPL() { return PMNTN_UPPL; }
    public Double getPMNTN_GODN() { return PMNTN_GODN; }
    public String getPMNTN_DFFL() { return PMNTN_DFFL; }
    public int getTime() { return (int)(PMNTN_GODN + PMNTN_UPPL); }
    public String getSTART_PNT() { return START_PNT; }
    public String getEND_PNT() { return END_PNT; }
    public int getPEOPLE() { return PEOPLE; }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMNTN_NM(String MNTN_NM) {
        this.MNTN_NM = MNTN_NM;
    }

    public void setPMNTN_NM(String PMNTN_NM) {
        if (PMNTN_NM.equals("null"))
            this.PMNTN_NM = " ";
        else
            this.PMNTN_NM = PMNTN_NM;
    }

    public void setPMNTN_LT(Double PMNTN_LT) { this.PMNTN_LT = PMNTN_LT; }
    public void setPMNTN_UPPL(Double PMNTN_UPPL) { this.PMNTN_UPPL = PMNTN_UPPL; }
    public void setPMNTN_GODN(Double PMNTN_GODN) { this.PMNTN_GODN = PMNTN_GODN; }
    public void setPMNTN_DFFL(String PMNTN_DFFL) { this.PMNTN_DFFL = PMNTN_DFFL; }
    public void setSTART_PNT(String START_PNT) { this.START_PNT = START_PNT; }
    public void setEND_PNT(String END_PNT) { this.END_PNT = END_PNT; }
    public void setPEOPLE(int PEOPLE) { this.PEOPLE = PEOPLE; }
}
