package com.gitscode.gitscode.controller;

import com.gitscode.gitscode.router.GithubEventRouter;
import com.gitscode.gitscode.verifier.GithubSignatureVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/github")
@RequiredArgsConstructor
@Slf4j
public class GithubWebhookController {

  private final GithubSignatureVerifier verifier;
  private final GithubEventRouter router;

  @PostMapping
  public ResponseEntity<Void> handle(
      @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
      @RequestHeader("X-GitHub-Event") String event,
      @RequestBody String payload
  ) {

    log.info("Github Webhook received event: {}", event);
    if (!verifier.verify(signature, payload)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    router.route(event, payload);
    return ResponseEntity.ok().build();
  }


}
