package com.example.hp.myapplication.entity;

public class Selection {
    private String name;
    private int imgId;
    private int time;
    private int leaderId;
    private int teamId;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public Selection(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public Selection(int time, String name) {
        this.name = name;
        this.time = time;
    }

    public Selection(String name) {
        this.name = name;
        this.imgId = imgId;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    public int getTime() {
        return time;
    }
}
