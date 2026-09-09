package com.example.llmchat.controller;

import com.example.llmchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String userMsg = request.get("message");
        String reply = chatService.callLLM(userMsg);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", reply);
        return result;
    }
}
