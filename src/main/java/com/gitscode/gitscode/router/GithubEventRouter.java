package com.gitscode.gitscode.router;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitscode.gitscode.notifier.DiscordNotifier;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubEventRouter {

  private final ObjectMapper om;
  private final DiscordNotifier discord;

  public void route(String event, String payload) {
    switch (event) {
      case "pull_request" -> prOpened(payload);
      case "issues" -> issueOpened(payload);
      case "push" -> pushMain(payload);
      case "ping" -> discord.send("Github Ping 확인! Webhook 연결 OK");
      default -> {}
    }
  }

  private void prOpened(String json) {
    try {
      JsonNode node = om.readTree(json);
      String action = node.path("action").asText();

      if (!List.of("opened", "reopened", "ready_for_review").contains(action)) {
        return;
      }

      String title = node.path("pull_request").path("title").asText();
      String url = node.path("pull_request").path("html_url").asText();
      String author = node.path("pull_request").path("user").path("login").asText();
      String repo = node.path("pull_request").path("full_name").asText();

      discord.send("📣 **PR " + action + "** - " + repo + "\n" +
          "• " + title + " by " + author + "\n" + url);

    } catch (Exception ignored) {}
  }

  private void issueOpened(String json) {

    try {
      JsonNode node = om.readTree(json);
      if(!"opened".equals(node.path("action").asText())) {
        return;
      }

      String title = node.path("issue").path("title").asText();
      String url = node.path("issue").path("html_url").asText();
      String author = node.path("issue").path("user").path("login").asText();
      String repo = node.path("repository").path("full_name").asText();

      discord.send("🧩 **Issue opened** - " + repo + "\n" +
          "• " + title + " by " + author + "\n" + url);
    } catch (Exception ignored) {}
  }

  private void pushMain(String json) {
    try {
      JsonNode node = om.readTree(json);
      String ref = node.path("ref").asText();
      String branch = ref.replace("refs/heads/", "");
      if (!"main".equals(branch)) {
        return;
      }

      String repo = node.path("repository").path("full_name").asText();
      String pusher = node.path("pull_request").path("name").asText();
      String compareUrl = node.path("compare").asText();

      discord.send("✅ **Push to main** - " + repo + "\n" +
          "• by " + pusher + "\n" + compareUrl);

    } catch (Exception ignored) {}
  }
}
