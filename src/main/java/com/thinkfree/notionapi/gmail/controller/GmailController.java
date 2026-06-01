package com.thinkfree.notionapi.gmail.controller;

import com.thinkfree.notionapi.gmail.service.GmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gmail")
public class GmailController {

    private final GmailService gmailService;

    @GetMapping("/messages")
    public ResponseEntity<?> messages(
            @RequestParam(defaultValue = "100") int maxResult,
            @RequestParam String token,
            @RequestParam String userId
    ) {

        return ResponseEntity.ok(
                gmailService.getMessages(
                        userId,
                        maxResult,
                        token
                )
        );
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<?> getMessage(@PathVariable String messageId){

        return new ResponseEntity<>();
    }

}
