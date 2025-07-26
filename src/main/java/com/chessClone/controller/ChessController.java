package com.chessClone.controller;

import com.chessClone.service.impl.ChessService;
import com.chessClone.service.impl.PlayOnlineService;
import com.chessClone.service.impl.PlayerService;
import com.chessClone.util.GameManager;
import com.chessClone.util.GameTime;
import com.chessClone.util.GetGame;
import com.chessClone.util.Players;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;


@Controller
public class ChessController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
//    public String roomId

    @Autowired
    private static PlayerService playerService;
    @Autowired
    private static PlayOnlineService playOnlineService;

@MessageMapping("/hello")
    @SendTo("/topic/greetings")
public String handleHello(String message) {
    System.out.println("Received message: " + message);
    return "Server says: Hello, " + message;
}
@MessageMapping("/join")
    @SendTo("/topic/playerRole")
    public static   Map<String,Object> handleJoin(StompHeaderAccessor headerAccessor){
    String sessionId=headerAccessor.getSessionId();
    System.out.println(headerAccessor.getSessionId());
    String role=null;
    if(GameTime.getPlayTime()!=null){
Integer time=GameTime.getPlayTime();

         role =playerService.assignPlayer(sessionId,time);
        System.out.println("printing the role "+role);
    }

        Map<String,Object> result=new HashMap<>();
        if(GameTime.getPlayTime()!=null)
       result= playOnlineService.createRoom(sessionId, GameTime.getPlayTime());
       result.put("time",GameTime.getPlayTime());
       result.put("role",role);
        return result;


}

@MessageMapping("/leave")

public  void handleLeave(StompHeaderAccessor headerAccessor,Map<String,Object> timemap) {
    Integer time=(Integer) timemap.get("time");
    String room=(String) timemap.get("roomId");
    String playerRole=(String) timemap.get("playerRole");
    Map<String,Object> result=new HashMap<>();

//     const send = {
//            time: this.time,
//            roomId:this.roomId,
//            playerRole:this.playerRole
//            };
    System.out.println(headerAccessor.getSessionId());
    System.out.println("One player left");
     boolean isRemoved= playerService.removePlayer(headerAccessor.getSessionId(),time);
     if(isRemoved){
//         " sucess && removed player "
         result.put("sucess",true);

         result.put("removedPlayer",playerRole=="b"?"b":"w");
//         /stopGame/${this.roomId}
         String roomId=("/stopGame/"+room);
         System.out.println(roomId);
         messagingTemplate.convertAndSend(roomId,result);
     }

}

//Any move
    @MessageMapping("/move")
    public void checkValidMove(Map<String, String> moveData, StompHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
//        get the roomid;
        String roomId=moveData.get("roomId");
        String time=moveData.get("time");
        String fen = moveData.get("fen");
        String source = moveData.get("from");
        String target = moveData.get("to");
        System.out.println(source);
        System.out.println(target);
//        System.out.println(fen);

        // Call the validation method
        GetGame isGameOn=new GetGame();
        Map<String, Object> result=null;

        if(isGameOn.getgame(sessionId)){
             result = ChessService.moveValidation(fen, source, target, sessionId);
            System.out.println(result);
            // Return the response entity

//            return ResponseEntity.ok(result);
//            send to the specific room

        }
        else{
             result=new HashMap<>();
            result.put("valid",false);
//            return ResponseEntity.ok(result);
        }
        String room=("/topic/game/"+time+"/"+roomId);
//        String staticId="ex5irpwaeun5b2j1";
//        String room1=("/topic/game/"+staticId);
//        String room1=("/topic/game/"+staticId);
//
        messagingTemplate.convertAndSend(room,result);
    }

}
