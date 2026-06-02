package com.thinkfree.notionapi.atlassian;

import com.thinkfree.notionapi.atlassian.config.AtlassianOAuthProperties;
import com.thinkfree.notionapi.atlassian.dto.AtlassianAccessibleResponse;
import com.thinkfree.notionapi.atlassian.dto.AtlassianOAuthResponse;
import com.thinkfree.notionapi.atlassian.dto.ConfluencePageResponse;
import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class AtlassianService {

    private final AuthenticationRepository authenticationRepository;
    private final RestClient atlassianOAuthRestClient;
    private final AtlassianOAuthProperties properties;

    public int requestByConcurrent(String email, int size) {

        AtomicInteger failedCount = new AtomicInteger(0);

        CountDownLatch count = new CountDownLatch(2);
        CountDownLatch result = new CountDownLatch(3);
        Authentication authentication = authenticationRepository.find(email, ProviderType.ATLASSIAN);
        long id = authentication.getId();
        String refreshToken = authentication.getRefreshToken();

        Runnable task = () -> {
            doRefresh(id, refreshToken, failedCount);
            count.countDown();
            result.countDown();
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(() -> {
            try {
                count.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            doRefresh(id, refreshToken, failedCount);
            result.countDown();
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            result.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("failed = {}", failedCount);
        return Integer.parseInt(failedCount.toString());
    }

    public ConfluencePageResponse getResourcesByOldAccessToken(String email, String pageId) {
        Authentication authentication = authenticationRepository.find(email, ProviderType.ATLASSIAN);
        String accessToken = authentication.getAccessToken();

        doRefresh(authentication.getId(), authentication.getRefreshToken(), new AtomicInteger(0));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ConfluencePageResponse page = getPageByAccessToken(accessToken, pageId);
        log.info("page = {}", page);

        return page;
    }

    public ConfluencePageResponse getPage(String email, String pageId) {
        Authentication authentication = authenticationRepository.find(email, ProviderType.ATLASSIAN);
        String accessToken = authentication.getAccessToken();

        String cloudId = "731d6666-db1b-41ed-b782-df2bd259765c";

        ConfluencePageResponse response = atlassianOAuthRestClient.get()
                .uri(properties.getPageUri(), cloudId, pageId)
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(ConfluencePageResponse.class);

        return response;
    }

    private ConfluencePageResponse getPageByAccessToken(String accessToken, String pageId) {
        String cloudId = "731d6666-db1b-41ed-b782-df2bd259765c";

        ConfluencePageResponse response = atlassianOAuthRestClient.get()
                .uri(properties.getPageUri(), cloudId, pageId)
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(ConfluencePageResponse.class);

        return response;
    }

    public String getCloudId(String email) {
        Authentication authentication = authenticationRepository.find(email, ProviderType.ATLASSIAN);
        String accessToken = authentication.getAccessToken();

        AtlassianAccessibleResponse[] response = atlassianOAuthRestClient.get()
                .uri(properties.accessibleResourcesUri())
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                .retrieve()
                .body(AtlassianAccessibleResponse[].class);

        return response[0].id();
    }

    private void doRefresh(long id, String refreshToken, AtomicInteger failedCount) {
        AtlassianOAuthResponse response = null;
        log.info("Thread {} is start, my refreshToken is {}", Thread.currentThread().getName(), getShortRefresh(refreshToken));
        try {
            response = refresh(refreshToken);
            log.info("Thread {} makes refreshToken is {}", Thread.currentThread().getName(), getShortRefresh(response.refreshToken()));
        } catch (Exception e) {
            failedCount.incrementAndGet();
            log.error(e.getMessage());
        }
        authenticationRepository.refresh(id, response.refreshToken());
    }

    private @NonNull String getShortRefresh(String refreshToken) {
        return refreshToken.substring(0, 10) + "..." + refreshToken.substring(refreshToken.length() - 11, refreshToken.length() - 1);
    }

    private AtlassianOAuthResponse refresh(String refreshToken) {
        Map<String, String> requestBody = Map.of(
                "grant_type", "refresh_token",
                "client_id", properties.clientId(),
                "client_secret", properties.clientSecret(),
                "refresh_token", refreshToken
        );

        AtlassianOAuthResponse response = atlassianOAuthRestClient.post()
                .uri(properties.tokenUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .retrieve()
                .body(AtlassianOAuthResponse.class);

        return response;
    }

}
