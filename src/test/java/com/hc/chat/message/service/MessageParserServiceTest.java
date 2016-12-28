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
  public void testParseWithTwoMentionsConcatenatedWithoutSpace() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("@bob@john such a cool feature");
    // PSQ: Given that a mention always starts with an '@' and ends when hitting a NON-WORD character,
    //  is this a valid message,  "@luke@mary Hello, how are you"?
    // If so, what should I return in this case given that they are not separated by non-word character?
    assertEquals(
        "",
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
  public void testSuccessfulParseForMinEmoticonLength() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("Good morning! (m) (coffee)");
    assertEquals(
        "{\"emoticons\": [ \"m\",\"coffee\" ]}",
        "");
  }

  @Test
  public void testEmptyParseForMinEmoticonLength() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("Good morning! () (coffee)");
    assertEquals(
        "{\"emoticons\": [ \"coffee\" ]}",
        "");
  }

  @Test
  public void testSuccessfulParseForMaxEmoticonLengthBoundary() throws Exception { //15chars
    ParsedMessage parsedMessage = parserService.parse("Good morning! (123456789012345)");
    assertEquals(
        "{\"emoticons\": [ \"123456789012345\" ]}",
        "");
  }

  @Test
  public void testEmptyParseForMaxEmoticonLengthBoundary() throws Exception { //16chars
    ParsedMessage parsedMessage = parserService.parse("Good morning! (1234567890123456)");
    assertEquals(
        "{\"emoticons\": []}",
        "");
  }

  @Test
  public void testParseWithOneHttpLink() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("Olympics are starting soon; http://www.nbcolympics.com");
    assertEquals(
        "{\"links\": [{\"url\": \"http://www.nbcolympics.com\",\"title\": \"2016 Rio Olympic Games | NBC Olympics\"}]}",
        "");
  }

  @Test
  public void testParseWithOneHttpsLink() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("Olympics are starting soon; http://www.nbcolympics.com");
    assertEquals(
        "{\"links\": [{\"url\": \"https://www.nbcolympics.com\",\"title\": \"2016 Rio Olympic Games | NBC Olympics\"}]}",
        "");
  }

  @Test
  public void testEmptyParseWithOneLinkWithNoDomain() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("bad url https://a");
    assertEquals(
        "{\"links\": []}",
        "");
  }

  @Test
  public void testEmptyParseWithOneLinkWithTooShortDomain() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("bad url https://a.a");
    assertEquals(
        "{\"links\": []}",
        "");
  }

  @Test
  public void testEmptyParseWithOneLinkWithTooShortHostname() throws Exception {
    ParsedMessage parsedMessage = parserService.parse("bad url https://.a");
    assertEquals(
        "{\"links\": []}",
        "");
  }

  @Test
  public void testParseWithTwoMentionsOneEmoticonAndOneLink() throws Exception { //single composite test
    ParsedMessage parsedMessage = parserService.parse("@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016");
    assertEquals(
        "{\"mentions\":[\"bob\",\"john\"], \"emoticons\":[\"success\"],\"links\":[{\"url\":\"https://twitter.com/jdorfman/status/430511497475670016\",\"title\": \"Justin Dorfman on Twitter: &quot;nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq&quot;\"}]}",
        "");
  }


}