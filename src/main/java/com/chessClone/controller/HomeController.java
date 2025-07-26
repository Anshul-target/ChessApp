package com.chessClone.controller;

import com.chessClone.util.GameTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Looks for templates/index.html
    }

    @GetMapping("/game")
    public String serveGamePage(Model model) {
        // Pass game time to the template if needed
        Integer currentTime = GameTime.getPlayTime();
        if (currentTime != null) {
            model.addAttribute("gameTime", currentTime);
        }
        return "game"; // Looks for templates/game.html
    }

    // Use ResponseEntity for AJAX requests to avoid redirect issues
    @PostMapping("/game")
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleGame(@RequestBody Map<String, Integer> timerData) {
        try {
            Integer time = timerData.get("time");
            if (time == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Time parameter is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            GameTime.setPlayTime(time);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("redirectUrl", "/game");

            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to process request");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/newGame")
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleNewGame(@RequestBody Map<String, Integer> timerData) {
        try {
            Integer time = timerData.get("time");
            if (time == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Time parameter is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            GameTime.setPlayTime(time);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("redirectUrl", "/game");

            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to process request");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}