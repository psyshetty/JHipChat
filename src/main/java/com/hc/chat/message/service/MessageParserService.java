package com.hc.chat.message.service;

import com.hc.chat.message.pojo.Link;
import com.hc.chat.message.pojo.ParsedMessage;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

  private UrlValidator urlValidator;
  public HashMap<String, String> urlTitleMap;

  public MessageParserService() {
    String[] schemes = {"http","https"};
    urlValidator = new UrlValidator(schemes);
    urlTitleMap = new HashMap<>();
  }

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
      System.out.println(grp.substring(1));
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
      System.out.println(grp.substring(1, grp.length() - 1));
      emoticonList.add(grp.substring(1, grp.length() - 1));
    }
    return emoticonList;
  }

  public String getTitle(String url) {
    String title = null;
    if (urlTitleMap.containsKey(url)) {
      title = urlTitleMap.get(url);
    } else {
      try {
        Document doc = Jsoup.connect(url).validateTLSCertificates(false).timeout(5000).get();
        title = StringEscapeUtils.escapeHtml(doc.title());
      } catch (HttpStatusException e) {
        e.printStackTrace();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      urlTitleMap.put(url, title);
    }
    return title;
  }

  public List<Link> getLinks(String s) {
    List<Link> links = new ArrayList<>();
    Pattern pattern = Pattern.compile("http(s?)://\\S+");
    Matcher matcher = pattern.matcher(s);
    while (matcher.find()) {
      String url = matcher.group();
      if (urlValidator.isValid(url)) {
        System.out.println("URL: " + url);
        String title = getTitle(url);
        if (title != null) {
          links.add(new Link(url, title));
        }
      }

    }
    return links;
  }

}
