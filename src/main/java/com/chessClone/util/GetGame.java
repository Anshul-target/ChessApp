package com.chessClone.util;

public class GetGame {

    public boolean getgame(String sessionId){
        for(Game game: GameManager.getGameList()){
            PlayerInfo player1=game.getPlayerOne();
            PlayerInfo player2=game.getPlayerTwo();
            if( player1!=null && player1.getSocketId().equals(sessionId)){
                if (player2!=null)
               return true;
                else
                    return false;


            }

            if (player2!=null && player2.getSocketId().equals(sessionId)){
                if (player1!=null)
                    return true;
                else
                    return false;
//


            }


        }
        return false;
    }
}
