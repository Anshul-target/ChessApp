package com.chessClone.service.impl;

import com.chessClone.util.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PlayOnlineService {
    private static boolean isBlackPresent=false;
     private static boolean isWhitePresent=false;

    private static Map<Integer,List<Boolean>>pieceColorMap=new HashMap<>();


    public static void setPieceColorMap(Map<Integer, List<Boolean>> pieceColorMap) {
        PlayOnlineService.pieceColorMap = pieceColorMap;
    }

    public static Map<Integer, List<Boolean>> getPieceColorMap() {
        return pieceColorMap;
    }

    public static void setIsBlackPresent(boolean isBlackPresent) {
        PlayOnlineService.isBlackPresent = isBlackPresent;
    }

    public static void setIsWhitePresent(boolean isWhitePresent) {
        PlayOnlineService.isWhitePresent = isWhitePresent;
    }
    public static boolean getIsBlackPresent(){
        return isBlackPresent;
    }

    public static boolean getIsWhitePresent(){
        return isWhitePresent;
    }

    public static   Map<String,Object>  createRoom(String sessionId,Integer time){
        Map<String, Object> result = new HashMap<>();
        Map<Integer,List<String>> playerRole = Players.getPlayerColor();
        String Black=playerRole.get(time).get(0);
        String White=playerRole.get(time).get(1);
        String roomId=null;
        if(!pieceColorMap.containsKey(time)){
            List<Boolean>colorList=new ArrayList<>();
            colorList.add(0,false);
            colorList.add(1,false);
            pieceColorMap.put(time,colorList);
        }
        if(sessionId!=null){
            PlayerSession playerSession=null;

            if(Black!=null && !pieceColorMap.get(time).get(0)){
                pieceColorMap.get(time).set(0,true);
               playerSession=new PlayerSession(sessionId,"b",time);

            }
            else if(White!=null && !pieceColorMap.get(time).get(1)){
                pieceColorMap.get(time).set(1,true);
                playerSession=new PlayerSession(sessionId,"w",time);

            }

//            PlayerSession playerSession=new PlayerSession(sessionId);
//            Queue<PlayerSession> players= Room.getPlayerQueue();
//            players.offer(playerSession);
            Map<Integer, List<Queue<PlayerSession>>>map=Room.getMap();
            if (!map.containsKey(time)) {
                // Initialize the list with two queues (one for "b" and one for "w")
                List<Queue<PlayerSession>> queues = new ArrayList<>();
                queues.add(new ArrayDeque<>()); // Queue for "b"
                queues.add(new ArrayDeque<>()); // Queue for "w"
                map.put(time, queues);
            }
            if (playerSession.getRole()=="b"){
                map.get(time).get(0).offer(playerSession);
            }

            if (playerSession.getRole()=="w"){
                map.get(time).get(1).offer(playerSession);
            }
          if (map.get(time).get(0).size()>0 && map.get(time).get(1).size()>0){

//              Create a game
           PlayerSession playerSession1=map.get(time).get(0).poll();
              PlayerSession playerSession2=map.get(time).get(1).poll();
              PlayerInfo playerInfo1=new PlayerInfo(playerSession1.getSessionId(),playerSession1.getRole(),playerSession1.getTime());
              PlayerInfo playerInfo2=new PlayerInfo(playerSession2.getSessionId(),playerSession2.getRole(),playerSession2.getTime());
              Game game=new Game(playerInfo1,playerInfo2);
//              Genetate a gameId
              roomId=playerSession1.getSessionId()+playerSession2.getSessionId();

//              Put inside a gameManager
              GameManager.getGameList().add(game);
//              set the black and white to null
              playerRole.get(time).set(0,null);
              playerRole.get(time).set(1,null);
              pieceColorMap.get(time).set(0,false);
              pieceColorMap.get(time).set(1,false);
//              Stat the game


//              while(!players.isEmpty()){
//                  PlayerSession player=players.poll();
//
//              }
          }


        }
        result.put("roomId",roomId);
        return result;
    }

}
