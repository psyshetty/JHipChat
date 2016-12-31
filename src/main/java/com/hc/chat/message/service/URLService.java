package com.hc.chat.message.service;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by ps on 12/31/16.
 */
@Service
public class URLService {
  private UrlValidator urlValidator;
  public HashMap<String, String> urlTitleMap;

  public URLService() {
    String[] schemes = {"http","https"};
    urlValidator = new UrlValidator(schemes);
    urlTitleMap = new HashMap<>();
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
        Document doc = Jsoup.connect(url).validateTLSCertificates(false).timeout(5000).get();
        title = StringEscapeUtils.escapeHtml(doc.title());
      } catch (SocketTimeoutException e) {
        title = "Website down";
      } catch (UnknownHostException e) {
        title = "Unknown website host";
      } catch (IOException e) {
        title = url;
      }
      urlTitleMap.put(url, title);
    }
    return title;
  }


}
