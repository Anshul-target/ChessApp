package com.chessClone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PlayerSession {

    private final String sessionId;  // WebSocket session ID
    private final String role;
    private final Integer time;

//    todo for the future improvements
//    private final String username;  // Player's username (optional)
//    private final int rating;       // Player's rating (optional)
    public PlayerSession(String sessionId,String role,Integer time) {
        this.sessionId = sessionId;
        this.role=role;
        this.time=time;

//        this.username = username;
//        this.rating = rating;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getRole() {
        return role;
    }

    public Integer getTime() {
        return time;
    }
}

