package com.thinkfree.notionapi.atlassian;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/atlassian")
public class AtlassianController {

    private final AtlassianService atlassianService;

    @GetMapping("/refresh/test")
    public ResponseEntity<?> refresh(
            @RequestParam String email,
            @RequestParam(defaultValue = "10") int size
    ) {

        int failedCount = atlassianService.requestByConcurrent(email, size);

        return ResponseEntity.ok(failedCount);
    }

    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPage(
            @PathVariable String pageId,
            @RequestParam String email
    ){

        return ResponseEntity.ok(
                atlassianService.getPage(email, pageId)
        );
    }

    @GetMapping("/page/test/{pageId}")
    public ResponseEntity<?> getPageTest(@PathVariable String pageId, @RequestParam String email){

        return ResponseEntity.ok(
                atlassianService.getResourcesByOldAccessToken(email, pageId)
        );
    }


}
