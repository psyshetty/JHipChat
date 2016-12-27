package com.hc.chat.message.service;

import com.hc.chat.message.pojo.ParsedMessage;
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
  public void testParseWithOneMention() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("@chris you around?");
    assertEquals(
        "{\"mentions\": [\"chris\"]}",
        "");
  }

  @Test
  public void testParseWithTwoEmoticons() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("Good morning! (megusta) (coffee)");
    assertEquals(
        "{\"emoticons\": [ \"megusta\",\"coffee\" ]}",
        "");
  }

  @Test
  public void testParseWithOneLink() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("Olympics are starting soon; http://www.nbcolympics.com");
    assertEquals(
        "{\"links\": [{\"url\": \"http://www.nbcolympics.com\",\"title\": \"2016 Rio Olympic Games | NBC Olympics\"}]}",
        "");
  }


}
