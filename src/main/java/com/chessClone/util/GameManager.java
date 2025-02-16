package com.chessClone.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameManager {
    private static List<Game> gameList=new ArrayList<>();

    public static List<Game> getGameList() {
        return gameList;
    }
}
