package com.thinkfree.notionapi.notion.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.thinkfree.notionapi.notion.service.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionController {

    private final NotionService notionService;

    @GetMapping("/pages/{pageId}")
    public JsonNode getPage(@PathVariable String pageId){
        return notionService.retrievePage(pageId);
    }

    @GetMapping("/pages/{pageId}/content")
    public JsonNode getPageContent(@PathVariable String pageId){

        return notionService.retrievePageContent(pageId);
    }

    @GetMapping("/pages/{pageId}/markdown")
    public JsonNode getPageMarkdown(@PathVariable String pageId){

        return notionService.retrieveMarkdownContent(pageId);
    }

}
