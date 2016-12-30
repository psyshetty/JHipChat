package com.hc.chat.message.pojo;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Created by ps on 12/27/16.
 */
public class Link {
  private String url;
  @JsonRawValue
  private String title;

  public Link() {}

  public Link(String url, String title) {
    this.url = url;
    this.title = title;
  }
}
