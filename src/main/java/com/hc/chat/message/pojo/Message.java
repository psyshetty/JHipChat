package com.hc.chat.message.pojo;

/**
 * Created by ps on 12/29/16.
 */
public class Message {
  String message;

  public Message() {
  }

  public Message(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
