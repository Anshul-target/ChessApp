package com.chessClone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Players {
 public static String white=null;
public static String black=null;
public static String currentPlayer="w";

 private static Map<Integer, List<String>> playerColor=new HashMap<>();



// time -> List[pid,pid]
 public static Map<Integer, List<String>> getPlayerColor() {
  return playerColor;
 }

 public static String getBlack() {
  return black;
 }

 public static String getWhite() {
  return white;
 }

 public static void setBlack(String black) {
  Players.black = black;
 }

 public static void setWhite(String white) {
  Players.white = white;
 }

 public static String getCurrentPlayer() {
  return currentPlayer;
 }

 public static void setCurrentPlayer(String currentPlayer) {
  Players.currentPlayer = currentPlayer;
 }
}
