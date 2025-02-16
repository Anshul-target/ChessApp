package com.chessClone.util;

public class PlayerInfo {
    private  String socketId;
    private  String role;

    private  Integer time;

    public PlayerInfo(String socketId, String role,Integer time) {
        this.socketId = socketId;
        this.role = role;
        this.time=time;
    }

    public String getRole() {
        return role;
    }

    public String getSocketId() {
        return socketId;
    }

    public Integer getTime() {
        return time;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
