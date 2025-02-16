package com.chessClone.controller;

import com.chessClone.util.GameTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html"; // Serve static/index.html
    }

    @GetMapping("/game")
    public String serveGamePage() {
        return "forward:/game.html"; // Serve static/game.html at /game URL
    }

    @PostMapping("/game")
    public String handleGame(@RequestBody Map<String, Integer> timerData) {
        Integer time = timerData.get("time");
        GameTime.setPlayTime(time);
        return "redirect:/game"; // Redirect to /game URL after processing
    }

    @PostMapping("/newGame")
    public String handlenewGame(@RequestBody Map<String, Integer> timerData) {
        Integer time = timerData.get("time");
        GameTime.setPlayTime(time);
        return "redirect:/game"; // Redirect to /game URL after processing
    }
}