package com.hc.chat.message.pojo;

import java.util.List;

/**
 * Created by ps on 12/27/16.
 */
public class ParsedMessage {
  private List<String> mentions;
  private List<String> emoticons;
  private List<Link> links;

  public ParsedMessage() {}

  public ParsedMessage(List<String> mentions, List<String> emoticons, List<Link> links) {
    this.mentions = mentions;
    this.emoticons = emoticons;
    this.links = links;
  }

  public List<String> getMentions() {
    return mentions;
  }

  public void setMentions(List<String> mentions) {
    this.mentions = mentions;
  }

  public List<String> getEmoticons() {
    return emoticons;
  }

  public void setEmoticons(List<String> emoticons) {
    this.emoticons = emoticons;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  @Override
  public String toString() {
    return "ParsedMessage{" +
        "mentions=" + mentions +
        ", emoticons=" + emoticons +
        ", links=" + links +
        '}';
  }
}

