package com.example.capstone42_sancheck.object;

public class User {
    public String name;
    public int rank;
    public int score;

    public User() {

    }

    public User(String name, int rank, int score) {
        this.name = name;
        this.rank = rank;
        this.score = score;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getRank() { return rank; }

    public void setRank(int rank) { this.rank = rank; }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

//    @Override
//    public String toString() {
//        return "User{" + "score='" + String.valueOf(score) + '\'' + "}";
//    }
}
