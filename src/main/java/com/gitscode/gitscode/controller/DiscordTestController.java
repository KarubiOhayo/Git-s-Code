package com.gitscode.gitscode.controller;

import com.gitscode.gitscode.notifier.DiscordNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscordTestController {
  private final DiscordNotifier discord;

  @PostMapping("/test/discord")
  public String testDiscord(@RequestParam(defaultValue = "테스트") String message) {
    discord.send("TEST: " + message);
    return "OK";
  }
}
