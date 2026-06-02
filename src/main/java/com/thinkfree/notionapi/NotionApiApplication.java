package com.thinkfree.notionapi;

import com.thinkfree.notionapi.atlassian.config.AtlassianOAuthProperties;
import com.thinkfree.notionapi.gmail.config.GmailProperties;
import com.thinkfree.notionapi.google.GoogleOAuthProperties;
import com.thinkfree.notionapi.notion.config.NotionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({NotionProperties.class, GoogleOAuthProperties.class, GmailProperties.class, AtlassianOAuthProperties.class})
public class NotionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotionApiApplication.class, args);
    }

}
