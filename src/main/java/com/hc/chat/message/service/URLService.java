package com.hc.chat.message.service;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by ps on 12/31/16.
 */
@Service
@PropertySource("classpath:jhipchat.properties")
public class URLService {

  @Autowired
  private Environment env;

  private UrlValidator urlValidator;
  public HashMap<String, String> urlTitleMap;

  public URLService() {
    urlTitleMap = new HashMap<>();
  }

  @PostConstruct
  public void init() {
    String[] schemes = env.getProperty("VALID_URL_SCHEMES").split(",");
    urlValidator = new UrlValidator(schemes);
  }

  public boolean isValidURL(String url) {
    return urlValidator.isValid(url);
  }

  public String getTitle(String url) {
    String title = null;
    if (urlTitleMap.containsKey(url)) {
      title = urlTitleMap.get(url);
    } else {
      try {
        // 5 second timeout
        Document doc = Jsoup.connect(url).validateTLSCertificates(false).timeout(Integer.parseInt(env.getProperty("TITLE_TIMEOUT_IN_MILLIS"))).get();
        title = StringEscapeUtils.escapeHtml(doc.title());
      } catch (SocketTimeoutException e) {
        title = env.getProperty("WEBSITE_DOWN_TITLE");
      } catch (UnknownHostException e) {
        title = env.getProperty("UNKNOWN_WEBSITE_HOST_TITLE");
      } catch (IOException e) {
        title = url;
      }
      urlTitleMap.put(url, title);
    }
    return title;
  }


}
