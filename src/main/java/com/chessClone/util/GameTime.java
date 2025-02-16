package com.chessClone.util;

import java.util.ArrayDeque;
import java.util.Queue;

public class GameTime {
    private static Integer playTime;

    public static Integer getPlayTime() {
        return playTime;
    }

    public static void setPlayTime(Integer playTime) {
        GameTime.playTime = playTime;
    }
}
