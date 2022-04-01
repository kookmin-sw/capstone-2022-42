package com.example.capstone42_sancheck.object;

public class ListViewItem {
    private String text1;
    private String text2;

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public void setText2(String text2) {
        if (text2.equals("null"))
            this.text2 = " ";
        else
            this.text2 = text2;
    }
}
