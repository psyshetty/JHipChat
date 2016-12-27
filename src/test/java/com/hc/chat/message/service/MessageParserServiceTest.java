package com.hc.chat.message.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ps on 12/26/16.
 */
public class MessageParserServiceTest {

  private MessageParserService parserService;

  @Before
  public void setUp() throws Exception {
    parserService = new MessageParserService();
  }

  @Test
  public void testGetMentions() throws Exception {
  }
}
