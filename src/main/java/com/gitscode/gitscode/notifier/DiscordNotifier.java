package com.gitscode.gitscode.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DiscordNotifier {

  private final JDA jda;
  private final String defaultChannelId;

  public DiscordNotifier(
      @Value("${discord.bot.token}") String token,
      @Value("${discord.channel.default-id}") String defaultChannelId
  ) throws Exception {
    log.info("Discord default channel id = {}", defaultChannelId);
    if (token == null || token.isBlank() || "change-me".equals(token)) {
      throw new IllegalStateException("Discord bot token not provided");
    }
    // 임시 확인용
    System.out.println("[Discord] Token length= " + token.length());
    this.jda = JDABuilder.createDefault(token).build().awaitReady();
    this.defaultChannelId = defaultChannelId;

    log.info("Discord ready. Self user = {}", jda.getSelfUser().getAsTag());
    jda.getGuilds().forEach(g ->
        log.info("Guild: {} ({})", g.getName(), g.getId()));

  }

  public void send(String content) {
    TextChannel channel = jda.getTextChannelById(defaultChannelId);

    if (channel == null) {
      log.error("Discord channel not found by id {}", defaultChannelId);
      return;
    }
    channel.sendMessage(content).queue(
        ok -> log.info("[Discord] Sent to #{} ({})", channel.getName(), channel.getId()),
        err -> log.error("Discord send failed", err)
    );
  }

}
