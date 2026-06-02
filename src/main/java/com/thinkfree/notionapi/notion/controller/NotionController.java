package com.thinkfree.notionapi.notion.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.thinkfree.notionapi.notion.service.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionController {

    private final NotionService notionService;

    @GetMapping("/pages/{pageId}")
    public JsonNode getPage(@PathVariable String pageId, @RequestParam String email){
        return notionService.retrievePage(email, pageId);
    }

    @GetMapping("/pages/{pageId}/content")
    public JsonNode getPageContent(@PathVariable String pageId, @RequestParam String email){

        return notionService.retrievePageContent(pageId, email);
    }

    @GetMapping("/pages/{pageId}/markdown")
    public JsonNode getPageMarkdown(@PathVariable String pageId, @RequestParam String email){

        return notionService.retrieveMarkdownContent(pageId, email
        );
    }

}
