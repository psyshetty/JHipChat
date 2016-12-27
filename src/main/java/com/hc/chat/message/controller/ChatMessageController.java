package com.hc.chat.message.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ps on 12/26/16.
 */
@RestController
@RequestMapping("/chat/message")
public class ChatMessageController {
  @RequestMapping("/parse")
  public String parse() {
    return "Greetings from Spring Boot!. This is the parse method";
  }
}
