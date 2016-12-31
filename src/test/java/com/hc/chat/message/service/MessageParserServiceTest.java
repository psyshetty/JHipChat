package com.hc.chat.message.service;

import com.hc.chat.message.Application;
import com.hc.chat.message.pojo.Link;
import com.hc.chat.message.pojo.ParsedMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;

/**
 * Created by ps on 12/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class MessageParserServiceTest {

  @Autowired
  MessageParserService parserService;

  @Test
  public void testParseWithOneMention() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(Arrays.asList("chris"), new ArrayList<String>(), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("@chris you around?")));
  }

  @Test
  public void testParseWithTwoEmoticons() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), Arrays.asList("megusta","coffee"), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("Good morning! (megusta) (coffee)")));
  }

  @Test
  public void testSuccessfulParseForMinEmoticonLength() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), Arrays.asList("me","coffee"), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("Good morning! (me) (coffee)")));
  }

  @Test
  public void testEmptyParseForMinEmoticonLength() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), Arrays.asList("coffee"), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("Good morning! () (coffee)")));
  }

  @Test
  public void testSuccessfulParseForMaxEmoticonLengthBoundary() throws Exception { //15chars
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), Arrays.asList("123456789012345"), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("Good morning! () (123456789012345)")));
  }

  @Test
  public void testEmptyParseForMaxEmoticonLengthBoundary() throws Exception { //16chars
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("Good morning! () (1234567890123456)")));
  }

  @Test
  public void testParseWithOneHttpLink() throws Exception {
    List<Link> links = new ArrayList<Link>();
    links.add(new Link("http://www.nbcolympics.com", "2016 Rio Olympic Games | NBC Olympics"));
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), links);
    ParsedMessage actualMessage = parserService.parse("Olympics are starting soon; http://www.nbcolympics.com");
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getMentions(),actualMessage.getMentions()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getLinks(),actualMessage.getLinks()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getEmoticons(),actualMessage.getEmoticons()));
  }

  @Test
  public void testParseWithOneHttpsLink() throws Exception {
    List<Link> links = new ArrayList<Link>();
    links.add(new Link("https://www.yahoo.com", "Yahoo"));
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), links);
    ParsedMessage actualMessage = parserService.parse("Olympics are starting soon; https://www.yahoo.com");
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getMentions(),actualMessage.getMentions()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getLinks(),actualMessage.getLinks()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getEmoticons(),actualMessage.getEmoticons()));
  }

  @Test
  public void testParseWithOneNOTFOUNDWebsiteLink() throws Exception {
    List<Link> links = new ArrayList<Link>();
    links.add(new Link("https://www.yahoo.com/ererrreer", "Unknown website host"));
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), links);
    ParsedMessage actualMessage = parserService.parse("bad host url https://a.com");
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getMentions(),actualMessage.getMentions()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getLinks(),actualMessage.getLinks()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getEmoticons(),actualMessage.getEmoticons()));
  }

  @Test
  public void testParseWithOneInvalidWebsiteLink() throws Exception {
    List<Link> links = new ArrayList<Link>();
    links.add(new Link("https://a.com", "Unknown website host"));
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), links);
    ParsedMessage actualMessage = parserService.parse("bad host url https://a.com");
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getMentions(),actualMessage.getMentions()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getLinks(),actualMessage.getLinks()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getEmoticons(),actualMessage.getEmoticons()));
  }

  @Test
  public void testEmptyParseWithOneLinkWithNoDomain() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("bad url https://a")));
  }

  @Test
  public void testEmptyParseWithOneLinkWithTooShortDomain() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("bad url https://a.a")));
  }

  @Test
  public void testEmptyParseWithOneLinkWithTooShortHostname() throws Exception {
    ParsedMessage expectedParsedMessage = new ParsedMessage(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Link>());
    Assert.assertTrue(reflectionEquals(expectedParsedMessage,parserService.parse("bad url https://.a")));
  }

  @Test
  public void testParseWithTwoMentionsOneEmoticonAndOneLink() throws Exception { //single composite test
    List<String> mentions = new ArrayList<String>();
    mentions.add("bob");
    mentions.add("john");
    List<String> emoticons = new ArrayList<String>();
    emoticons.add("success");
    List<Link> links = new ArrayList<Link>();
    links.add(new Link("https://twitter.com/jdorfman/status/430511497475670016", "Justin Dorfman on Twitter: &quot;nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq&quot;"));
    ParsedMessage expectedParsedMessage = new ParsedMessage(mentions, emoticons, links);
    ParsedMessage actualMessage = parserService.parse("@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016");
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getMentions(),actualMessage.getMentions()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getLinks(),actualMessage.getLinks()));
    Assert.assertTrue(reflectionEquals(expectedParsedMessage.getEmoticons(),actualMessage.getEmoticons()));
  }


}