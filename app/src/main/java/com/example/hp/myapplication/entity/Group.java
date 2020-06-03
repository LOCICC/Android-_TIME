package com.example.hp.myapplication.entity;


public class Group {
    private String gName;
    private int todoSetId;

    public Group() {
    }

    public Group(String gName) {
        this.gName = gName;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public int getTodoSetId() {
        return todoSetId;
    }

    public void setTodoSetId(int todoSetId) {
        this.todoSetId = todoSetId;
    }
}