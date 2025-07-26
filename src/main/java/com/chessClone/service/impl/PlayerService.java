package com.chessClone.service.impl;

import com.chessClone.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

private static boolean isModified=false;
   static boolean Player1=true;
   static boolean Player2=true;

    public static synchronized String assignPlayer(String sessionId,Integer time){
        System.out.println("Printing the time "+time);
        Map<Integer,List<String>> playerColor=Players.getPlayerColor();
        if(!playerColor.containsKey(time)){
            List<String>colorList=new ArrayList<>();
            colorList.add(0,null);
            colorList.add(1,null);
            playerColor.put(time,colorList);

        }
        System.out.println("Checking time "+ playerColor.containsKey(time));

         if(playerColor.get(time).get(0)==null) {
             playerColor.get(time).set(0, sessionId);
             System.out.println("Cheking the list "+playerColor.get(time));
             return "b";
         }
       else if(playerColor.get(time).get(1)==null){
             playerColor.get(time).set(1,sessionId);
             System.out.println("Cheking the list "+playerColor.get(time));
            return "w";
        }
        else{
           return "spectator";
        }


    }


    public static boolean removePlayer(String sessionId,Integer time)  {

        Game removeGame=null;
        int playerNo=0;
        boolean isPresentInGame=false;

        for (Game game: GameManager.getGameList()){
            Game removeGame1=game;

            System.out.println(removeGame1.toString());
            System.out.println(game.toString());
            if (game.getPlayerOne()!=null && game.getPlayerOne().getSocketId().equals(sessionId)){
               removeGame=removeGame1;
               playerNo=1;
                isPresentInGame=true;
                break;
            }
            if ( game.getPlayerTwo()!=null && game.getPlayerTwo().getSocketId().equals(sessionId)){
                removeGame=removeGame1;
                playerNo=2;
                isPresentInGame=true;
                break;
            }

        }
        if (isPresentInGame){

//            GameManager.getGameList().remove(removeGame);
            if (playerNo==1){
                removeGame.setPlayerOne(null);
                Player1=false;
            }
            else if (playerNo==2){
                removeGame.setPlayerTwo(null);
                Player2=false;
            }
            if (!Player2 && !Player1){
                GameManager.getGameList().remove(removeGame);
                Player1=true;
                Player2=true;
            }
            return  true;

        }

        if (!isPresentInGame){
            Map<Integer, List<Queue<PlayerSession>>> map=Room.getMap();

            for (Map.Entry<Integer,List<Queue<PlayerSession>>> entry : map.entrySet())
            {
                List<Queue<PlayerSession>>li=entry.getValue();
                Queue<PlayerSession>pq= findAndReplace(li.get(0),sessionId);
                if (isModified){
                    li.set(0,pq);
                    isModified=false;
                    break;
                }
                else{
                    li.set(0,pq);
                    pq= findAndReplace(li.get(1),sessionId);
                    if (isModified){
                        li.set(1,pq);
                        isModified=false;
                        break;
                    }
                    else {
                        li.set(1,pq);
                    }
                }

            }


            Map<Integer,List<Boolean>> pieceColr=PlayOnlineService.getPieceColorMap();
            Map<Integer,List<String>> playerRole=Players.getPlayerColor();
            System.out.println("here it is"+playerRole.get(time));
            System.out.println(playerRole.get(time).get(0));
            System.out.println(playerRole.get(time).get(1));
         try {
             if (playerRole.get(time).get(0)!=null && playerRole.get(time).get(0).equals(sessionId)){
                 playerRole.get(time).set(0,null);
                 pieceColr.get(time).set(0,false);
             }
         }
         catch (Exception e){
             System.out.println(e.getMessage());
             System.out.println(e.getStackTrace());
         }

            if ( playerRole.get(time).get(1)!=null && playerRole.get(time).get(1).equals(sessionId)){
                pieceColr.get(time).set(1,false);
                playerRole.get(time).set(1,null);
            }

return  true;
        }
        else {
            isPresentInGame=false;
            return  false;
        }

    }

    private static Queue<PlayerSession> findAndReplace(Queue<PlayerSession>playerSessions,String sessonId){
        Queue<PlayerSession>pq=new ArrayDeque<>();
        while (!playerSessions.isEmpty()){
            if (playerSessions.peek()!=null && !playerSessions.peek().getSessionId().equals(sessonId)){
                pq.offer(playerSessions.poll());

            } else if (playerSessions.peek()!=null && playerSessions.peek().getSessionId().equals(sessonId)) {
                playerSessions.poll();
                isModified=true;
            }
        }

        return pq;

    }
}
