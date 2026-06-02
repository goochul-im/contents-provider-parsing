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
            @RequestParam String email
    ) {

        return ResponseEntity.ok(
                gmailService.getMessages(
                        email,
                        maxResult
                )
        );
    }

    @GetMapping("/message/{email}")
    public ResponseEntity<?> getMessage(
            @PathVariable String email,
            @RequestParam String messageId
    ){

        return ResponseEntity.ok(
                gmailService.getMessage(email, messageId)
        );
    }

}
