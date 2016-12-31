package com.hc.chat.message.service;

import com.hc.chat.message.pojo.Link;
import com.hc.chat.message.pojo.ParsedMessage;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ps on 12/27/16.
 */
@Service
public class MessageParserService {

  @Autowired
  URLService urlService;

  public ParsedMessage parse(String s) throws Exception {
    ParsedMessage pm = new ParsedMessage(getMentions(s), getEmoticons(s), getLinks(s));
    return pm;
  }

  public List<String> getMentions(String s) {
    List<String> mentionList = new ArrayList<>();
    Pattern pattern = Pattern.compile("@\\w+");
    Matcher matcher = pattern.matcher(s);
    while (matcher.find()) {
      String grp = matcher.group();
      mentionList.add(grp.substring(1));
    }
    return mentionList;
  }

  public List<String> getEmoticons(String s) {
    List<String> emoticonList = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\([a-zA-Z0-9]{1,15}\\)");
    Matcher matcher = pattern.matcher(s);
    while (matcher.find()) {
      String grp = matcher.group();
      emoticonList.add(grp.substring(1, grp.length() - 1));
    }
    return emoticonList;
  }

  public List<Link> getLinks(String s) {
    List<Link> links = new ArrayList<>();
    Pattern pattern = Pattern.compile("http(s?)://\\S+");
    Matcher matcher = pattern.matcher(s);
    while (matcher.find()) {
      String url = matcher.group();
      if (urlService.isValidURL(url)) {
        String title = urlService.getTitle(url);
        if (title != null) {
          links.add(new Link(url, title));
        }
      }

    }
    return links;
  }

}
