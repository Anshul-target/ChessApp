package com.chessClone.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
    private  PlayerInfo playerOne;
    private  PlayerInfo playerTwo;

    public Game(PlayerInfo playerOne, PlayerInfo playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public PlayerInfo getPlayerOne() {
        return playerOne;
    }

    public PlayerInfo getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerOne(PlayerInfo playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(PlayerInfo playerTwo) {
        this.playerTwo = playerTwo;
    }
}
