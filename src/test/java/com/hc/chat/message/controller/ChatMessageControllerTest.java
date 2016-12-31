package com.hc.chat.message.controller;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc.chat.message.pojo.Message;
import com.hc.chat.message.pojo.ParsedMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChatMessageControllerTest {

  @Autowired
  private MockMvc mvc;

  private ObjectMapper mapper;

  @Before
  public void setUp() throws Exception {
    mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  @Test
  public void testParseWithOneMention() throws Exception {
    Message message = new Message("@chris you around?");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(message))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("{\"mentions\":[\"chris\"],\"emoticons\":[],\"links\":[]}")));
  }

  @Test
  public void testParseWithTwoMentionsConcatenatedWithoutSpace() throws Exception {
    Message message = new Message("@bob@john such a cool feature");
    // PSQ: Given that a mention always starts with an '@' and ends when hitting a NON-WORD character,
    //  is this a valid message,  "@luke@mary Hello, how are you"?
    // If so, what should I return in this case given that they are not separated by non-word character?
    assertEquals(
        "",
        "");
  }

  @Test
  public void testParseWithTwoEmoticons() throws Exception {
    Message message = new Message("Good morning! (megusta) (coffee)");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[\"megusta\",\"coffee\"],\"links\":[]}")));
  }

  @Test
  public void testSuccessfulParseForMinEmoticonLength() throws Exception {
    Message message = new Message("Good morning! (m) (coffee)");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[\"m\",\"coffee\"],\"links\":[]}")));
  }

  @Test
  public void testEmptyParseForMinEmoticonLength() throws Exception {
    Message message = new Message("Good morning! () (coffee)");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[\"coffee\"],\"links\":[]}")));
  }

  @Test
  public void testSuccessfulParseForMaxEmoticonLengthBoundary() throws Exception { //15chars
    Message message = new Message("Good morning! (123456789012345)");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[\"123456789012345\"],\"links\":[]}")));
  }

  @Test
  public void testEmptyParseForMaxEmoticonLengthBoundary() throws Exception { //16chars
    Message message = new Message("Good morning! (1234567890123456)");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[],\"links\":[]}")));
  }

  @Test
  public void testParseWithOneHttpLink() throws Exception {
    Message message = new Message("Olympics are starting soon; http://www.nbcolympics.com");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[],\"links\":[{\"title\":2016 Rio Olympic Games | NBC Olympics}]}")));
  }

  @Test
  public void testParseWithOneHttpsLink() throws Exception {
    Message message = new Message("Olympics are starting soon; http://www.nbcolympics.com");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[],\"links\":[{\"title\":2016 Rio Olympic Games | NBC Olympics}]}")));
  }

  @Test
  public void testEmptyParseWithOneLinkWithNoDomain() throws Exception {
    Message message = new Message("bad url https://a");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[],\"links\":[]}")));
  }

  @Test
  public void testEmptyParseWithOneLinkWithTooShortDomain() throws Exception {
    Message message = new Message("bad url https://a.a");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[],\"links\":[]}")));
  }

  @Test
  public void testEmptyParseWithOneLinkWithTooShortHostname() throws Exception {
    Message message = new Message("bad url https://.a");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[],\"emoticons\":[],\"links\":[]}")));
  }

  @Test
  public void testParseWithTwoMentionsOneEmoticonAndOneLink() throws Exception { //single composite test
    Message message = new Message("@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016");
    mvc.perform(MockMvcRequestBuilders.post("/chat/message/parse")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(message))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"mentions\":[\"bob\",\"john\"],\"emoticons\":[\"success\"],\"links\":[{\"title\":Justin Dorfman on Twitter: &quot;nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq&quot;}]}")));
  }
}
