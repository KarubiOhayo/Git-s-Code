package com.gitscode.gitscode.notifier;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordNotifier {

  private final JDA jda;
  private final String defaultChannelId;

  public DiscordNotifier(
      @Value("${discord.bot.token}") String token,
      @Value("${discord.channel.default-id}") String defaultChannelId
  ) throws Exception {
    if (token == null || token.isBlank() || "change-me".equals(token)) {
      throw new IllegalStateException("Discord bot token not provided");
    }
    // 임시 확인용
    System.out.println("[Discord] Token length= " + token.length());
    this.jda = JDABuilder.createDefault(token).build().awaitReady();
    this.defaultChannelId = defaultChannelId;
  }

  public void send(String content) {
    TextChannel channel = jda.getTextChannelById(defaultChannelId);
    if (channel != null) {
      channel.sendMessage(content).queue();
    }
  }

}
