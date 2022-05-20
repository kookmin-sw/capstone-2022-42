package com.example.capstone42_sancheck.object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class User {
    public String name;
    public int rank;
    public int score;

    public int goal;

    public int walkDaily;
    public int walkMon;
    public int walkTue;
    public int walkWen;
    public int walkThu;
    public int walkFri;
    public int walkSat;
    public int walkSun;
    public long walkTotal;

    // 등산로 정보 파트
    // value: 등산로 index
    public List<Integer> trailComplited;
    public List<Integer> trailPlan;

    public List<String> trailComplitedDate;

    // 미션 파트
    // value: 달성한 미션의 index
    public List<Integer> missionDaily;
    public List<Integer> missionWeekly;
    public List<Integer> missionMonthly;

    public int missionDailyCount;
    public int missionWeeklyCount;

    public int dailyCheck;
    public String weeklyCheck;
    public int monthlyCheck;

    public User() {

    }

    public User(String name, int rank, int score, int goal,
                int walkDaily, int walkMon, int walkTue, int walkWen, int walkThu, int walkFri, int walkSat, int walkSun, long walkTotal,
                List<Integer> trailComplited, List<Integer> trailPlan, List<String> trailComplitedDate,
                List<Integer> missionDaily, List<Integer> missionWeekly, List<Integer> missionMonthly,
                int missionDailyCount, int missionWeeklyCount, int dailyCheck, String weeklyCheck, int monthlyCheck) {

        this.name = name;
        this.rank = rank;
        this.score = score;

        this.goal = goal;

        this.walkDaily = walkDaily;
        this.walkMon = walkMon;
        this.walkTue = walkTue;
        this.walkWen = walkWen;
        this.walkThu = walkThu;
        this.walkFri = walkFri;
        this.walkSat = walkSat;
        this.walkSun = walkSun;
        this.walkTotal = walkTotal;

        this.trailComplited = trailComplited;
        this.trailPlan = trailPlan;

        this.trailComplitedDate = trailComplitedDate;

        this.missionDaily = missionDaily;
        this.missionWeekly = missionWeekly;
        this.missionMonthly = missionMonthly;

        this.missionDailyCount = missionDailyCount;
        this.missionWeeklyCount = missionWeeklyCount;

        this.dailyCheck = dailyCheck;
        this.weeklyCheck = weeklyCheck;
        this.monthlyCheck = monthlyCheck;
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

    public int getGoal(){
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getWalkDaily(){
        return walkDaily;
    }

    public void setWalkDaily(int walkDaily){
        this.walkDaily = walkDaily;
    }

    public int getWalkMon(){
        return walkMon;
    }

    public void setWalkMon(int walkMon){
        this.walkMon = walkMon;
    }

    public int getWalkTue(){
        return walkTue;
    }

    public void setWalkTue(int walkTue){
        this.walkTue = walkTue;
    }

    public int getWalkWen(){
        return walkWen;
    }

    public void setWalkWen(int walkWen){
        this.walkWen = walkWen;
    }

    public int getWalkThu(){
        return walkThu;
    }

    public void setWalkThu(int walkThu){
        this.walkThu = walkThu;
    }

    public int getWalkFri(){
        return walkFri;
    }

    public void setWalkFri(int walkFri) {
        this.walkFri = walkFri;
    }

    public int getWalkSat(){
        return walkSat;
    }

    public void setWalkSat(int walkSat){
        this.walkSat= walkSat;
    }

    public int getWalkSun(){
        return walkSun;
    }

    public void setWalkSun(int walkSun){
        this.walkSun = walkSun;
    }

    public long getWalkTotal(){
        return walkDaily + walkTotal;
    }

    public void setWalkTotal(long walkTotal, int walkDaily){
        this.walkTotal = walkTotal + walkDaily;
    }

    public List<Integer> getTrailComplited(){
        return trailComplited;
    }

    public void setTrailComplitedAdd(int idx, List<Integer> trailComplited){
        // 중복 제거
        trailComplited.add(idx);
        List<Integer> list = new ArrayList<>();
        Set<Integer> uniqueValues = new HashSet<>();
        for (Integer integer : trailComplited) {
            if (uniqueValues.add(integer)) {
                list.add(integer);
            }
        }
        this.trailComplited = list;
    }

    public void setTrailComplitedDelete(int idx, List<Integer> trailComplited) {
        trailComplited.remove(idx);
        this.trailComplited = trailComplited;
    }

    public List<Integer> getTrailPlan(){
        return trailPlan;
    }

    public void setTrailPlanAdd(int idx, List<Integer> trailPlan) {
        // 중복 제거
        trailPlan.add(idx);
        List<Integer> list = new ArrayList<>();
        Set<Integer> uniqueValues = new HashSet<>();
        for (Integer integer : trailPlan) {
            if (uniqueValues.add(integer)) {
                list.add(integer);
            }
        }
        this.trailPlan = list;
    }

    public void setTrailPlanDelete(int idx, List<Integer> trailPlan) {
        trailPlan.remove(idx);
        this.trailPlan = trailPlan;
    }

    public List<String> getTrailComplitedDate(){
        return trailComplitedDate;
    }

    public void setTrailComplitedDate(String date, List<String> trailComplitedDate){
        trailComplitedDate.add(date);
        this.trailComplitedDate = trailComplitedDate;
    }

    public void setTrailPlanDel(int idx, List<Integer> trailPlan){
        trailPlan.remove(Integer.valueOf(idx));
        this.trailPlan = trailPlan;
    }

    public List<Integer> getMissionDaily(){
        return missionDaily;
    }

    public void setMissionDaily(List<Integer> missionDaily){
        this.missionDaily = missionDaily;
    }

    public List<Integer> getMissionWeekly(){
        return missionWeekly;
    }

    public void setMissionWeekly(List<Integer> missionWeekly){
        this.missionWeekly = missionWeekly;
    }

    public List<Integer> getMissionMonthly(){
        return missionMonthly;
    }

    public void setMissionMonthly(List<Integer> missionMonthly){
        this.missionMonthly = missionMonthly;
    }

    public int getMissionDailyCount(){
        return missionDailyCount;
    }

    public void setMissionDailyCount(int missionDailyCount){
        this.missionDailyCount = missionDailyCount;
    }

    public int getMissionWeeklyCount(){
        return missionWeeklyCount;
    }

    public void setMissionWeeklyCount(int missionWeeklyCount){
        this.missionWeeklyCount = missionWeeklyCount;
    }

    public int getDailyCheck(){
        return dailyCheck;
    }

    public void setDailyCheck(int dailyCheck){
        this.dailyCheck = dailyCheck;
    }

    public String getWeeklyCheck(){
        return weeklyCheck;
    }

    public void setWeeklyCheck(String weeklyCheck){
        this.weeklyCheck = weeklyCheck;
    }

    public int getMonthlyCheck(){
        return monthlyCheck;
    }

    public void setMonthlyCheck(int monthlyCheck){
        this.monthlyCheck = monthlyCheck;
    }
}
