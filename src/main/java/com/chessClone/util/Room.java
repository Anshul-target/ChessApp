package com.chessClone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;


public class Room {
    private static Queue<PlayerSession> playerQueue=new ArrayDeque<>();
    private static Map<Integer, List<Queue<PlayerSession>>> map=new HashMap<>();

    public static Queue<PlayerSession> getPlayerQueue() {
        return playerQueue;
    }
    public static Map<Integer, List<Queue<PlayerSession>>>getMap(){
        return map;
    }
}
