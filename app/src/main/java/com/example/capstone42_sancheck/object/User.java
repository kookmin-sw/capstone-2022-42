package com.example.capstone42_sancheck.object;

public class User {
    public int score;
    // 후에 다른 사용자 정보 추가 //

    public User(int score) {
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    @Override
    public String toString() {
        return "User{" + "score='" + String.valueOf(score) + '\'' + "}";
    }
}
