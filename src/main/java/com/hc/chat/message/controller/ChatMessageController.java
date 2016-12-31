package com.hc.chat.message.controller;

import com.hc.chat.message.pojo.Message;
import com.hc.chat.message.pojo.ParsedMessage;
import com.hc.chat.message.service.MessageParserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    ParsedMessage parsedMessage = messageParserService.parse(message.getMessage());
    return parsedMessage;
  }
}
