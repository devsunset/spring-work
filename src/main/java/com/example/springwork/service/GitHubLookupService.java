package com.example.springwork.service;

import java.util.concurrent.CompletableFuture;

import com.example.springwork.domain.AsyncUser;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GitHubLookupService {

  private final RestTemplate restTemplate;

  public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async
  public CompletableFuture<AsyncUser> findUser(String user) throws InterruptedException {
    log.info("Looking up " + user);
    String url = String.format("https://api.github.com/users/%s", user);
    AsyncUser results = restTemplate.getForObject(url, AsyncUser.class);
    // Artificial delay of 1s for demonstration purposes
    Thread.sleep(1000L);
    return CompletableFuture.completedFuture(results);
  }

}