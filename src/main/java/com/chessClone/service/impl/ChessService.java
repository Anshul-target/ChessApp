package com.chessClone.service.impl;

import com.chessClone.util.Game;
import com.chessClone.util.GameManager;
import com.chessClone.util.PlayerInfo;
import com.chessClone.util.Players;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChessService {




//    private static final Board board = new Board(); // Singleton Board instance
//    private static String currentFen = ""; // Store the current FEN
//
//    static {
//        // Initialize the board with the starting position
//        board.loadFromFen(Board.STARTING_POSITION_FEN);
//        currentFen = board.getFen();
//    }

    /**
     * Validates a move and returns the updated game state if the move is valid.
     *
     * @param fen       The current board state in FEN format.
     * @param source    The source square (e.g., "e2").
     * @param target    The target square (e.g., "e4").
     * @param sessionId The session ID of the player making the move.
     * @return A map containing:
     *         - "valid": true if the move is valid, otherwise false.
     *         - "fen": The updated FEN string (if the move is valid).
     *         - "turn": The new turn ("w" for white, "b" for black).
     */
    public static Map<String, Object> moveValidation(String fen, String source, String target, String sessionId) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", false); // Default to invalid move

        try {
            // Create a new board and load the FEN position
            Board board = new Board();
            board.loadFromFen(fen);

            // Check whose turn it is
            System.out.println(fen);
            Side currentTurn = board.getSideToMove(); // Get the current turn (Side.WHITE or Side.BLACK)
            System.out.println(currentTurn);
//            System.out.println((currentTurn == Side.WHITE && !Players.getWhite().equals(sessionId)));
            // Check if it's the correct player's turn
           String playerRole=null;

            for(Game game: GameManager.getGameList()){
                PlayerInfo player1=game.getPlayerOne();
                PlayerInfo player2=game.getPlayerTwo();
                if( player1!=null && player1.getSocketId().equals(sessionId)){
                    playerRole=player1.getRole();

                    break;
                }

                if (player2!=null && player2.getSocketId().equals(sessionId)){
                    playerRole=player2.getRole();
//
                    break;

                }


            }
            if ((currentTurn == Side.WHITE && !playerRole.equals("w")) ||
                    (currentTurn == Side.BLACK && !playerRole.equals("b"))) {
                System.out.println("Not your turn!");
                result.put("turn",currentTurn);
                return result;
            }
//            if(currentTurn==Side.WHITE &&  ){
//
//                result.put("turn",currentTurn);
//                return result;
//            }

//            if (currentTurn==Side.BLACK && Players.getWhite()==null){
//                result.put("turn",currentTurn);
//                return result;
//            }


            // Convert source and target squares from string to Square objects
            Square fromSquare = Square.fromValue(source.toUpperCase()); // e.g., "e2" -> Square.E2
            Square toSquare = Square.fromValue(target.toUpperCase());   // e.g., "e4" -> Square.E4

            // Create a Move object
            Move move = new Move(fromSquare, toSquare);

            // Check if the move is legal
            boolean isLegal = board.isMoveLegal(move, true);

            if (isLegal) {
                // Apply the move to the board
                board.doMove(move);
                System.out.println(board.getFen());
                // Update the current player after the move
                Side newTurn = board.getSideToMove();
                if (newTurn == Side.BLACK) {
                    Players.setCurrentPlayer("b");
                } else if (newTurn == Side.WHITE) {
                    Players.setCurrentPlayer("w");
                }

                // Prepare the result
                result.put("valid", true);
                result.put("fen", board.getFen()); // Updated FEN
                result.put("turn", newTurn == Side.WHITE ? "w" : "b"); // New turn
                result.put("from",source);
                result.put("to",target);
            }

            return result;

        } catch (MoveException e) {
            // Handle invalid move creation
            System.out.println("Invalid move: " + e.getMessage());
            return result;
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("Error during move validation: " + e.getMessage());
            return result;
        }
    }
}