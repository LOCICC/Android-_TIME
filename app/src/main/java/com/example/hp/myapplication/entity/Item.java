package com.example.hp.myapplication.entity;

public class Item {

    private int iTime;
    private String iName;
    private int todoId;
    private int todoSetId;
    private int teamId;

    public Item(String iName) {
        this.iName = iName;

    }

    public Item(int iTime, String iName) {
        this.iTime = iTime;
        this.iName = iName;
    }

    public Item(int iTime, String iName, int todoId, int todoSetId, int teamId) {
        this.iTime = iTime;
        this.iName = iName;
        this.todoId = todoId;
        this.todoSetId = todoSetId;
        this.teamId = teamId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public int getTodoSetId() {
        return todoSetId;
    }

    public void setTodoSetId(int todoSetId) {
        this.todoSetId = todoSetId;
    }

    public int getiTime() {
        return iTime;
    }

    public void setiTime(int iTime) {
        this.iTime = iTime;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
