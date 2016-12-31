package com.hc.chat.message.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc.chat.message.pojo.Message;
import com.hc.chat.message.pojo.ParsedMessage;
import com.hc.chat.message.service.MessageParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ps on 12/26/16.
 */
@RestController
@RequestMapping("/chat/message")
@PropertySource("classpath:jhipchat.properties")
public class ChatMessageController {

  @Autowired
  MessageParserService messageParserService;
  private ObjectMapper mapper;

  public ChatMessageController() {
    mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  @Autowired
  private Environment env;

  @RequestMapping(value="/parse", method= RequestMethod.POST)
  public ResponseEntity<String> parse(@RequestBody Message message) throws Exception {
    if (message.getMessage().length() > Integer.parseInt(env.getProperty("MAX_MESSAGE_LENGTH"))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(env.getProperty("ERR_MSG_MESSAGE_TOO_LONG"));
    }
    ParsedMessage parsedMessage = messageParserService.parse(message.getMessage());
    return ResponseEntity.ok(mapper.writeValueAsString(parsedMessage));
  }
}
