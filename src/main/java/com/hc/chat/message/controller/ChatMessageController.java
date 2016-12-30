package com.hc.chat.message.controller;

import com.hc.chat.message.pojo.Message;
import com.hc.chat.message.pojo.ParsedMessage;
import com.hc.chat.message.service.MessageParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ps on 12/26/16.
 */
@RestController
@RequestMapping("/chat/message")
public class ChatMessageController {

  @Autowired
  MessageParserService messageParserService;

  @RequestMapping(value="/parse", method= RequestMethod.POST)
  @ResponseBody
  public ParsedMessage parse(@RequestBody Message message) throws Exception {
    System.out.println("inside controller: message: " + message.getMessage());
    ParsedMessage parsedMessage = messageParserService.parse(message.getMessage());
    return parsedMessage;
  }
}
