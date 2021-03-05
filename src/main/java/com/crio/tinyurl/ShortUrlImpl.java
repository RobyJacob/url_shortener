package com.crio.shorturl;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javax.print.attribute.standard.RequestingUserName;

public class ShortUrlImpl implements ShortUrl {
    private Map<String, Integer> linkHitCount = new HashMap<>();
    private Map<String, String> shortUrlMap = new HashMap<>();
    private static final int RANDOM_STRING_LENGTH = 9;

    private String generateRandomString() {
        byte[] bytes = new byte[256];
        new Random().nextBytes(bytes);

        String byteString = new String(bytes, Charset.forName("UTF-8"));

        StringBuffer randomString = new StringBuffer();
        int len = RANDOM_STRING_LENGTH;

        for (int i = 0; i < byteString.length(); i++) {
            char chr = byteString.charAt(i);

            if (((chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || 
                (chr >= '0' && chr <= '9')) && (len > 0)) {
                    randomString.append(chr);
                    len--;
            }
        }

        return randomString.toString();
    }

    @Override
    public String registerNewUrl(String longUrl) {
        if (shortUrlMap.containsKey(longUrl)) return shortUrlMap.get(longUrl);

        String shortUrl = "http://short.url/" + generateRandomString();
        shortUrlMap.put(longUrl, shortUrl);

        return shortUrl;
    }

    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        if (shortUrlMap.containsValue(shortUrl)) return null;

        shortUrlMap.put(longUrl, shortUrl);

        return shortUrl;
    }

    @Override
    public String getUrl(String shortUrl) {
        if (!shortUrlMap.containsValue(shortUrl)) return null;

        String longUrl = "";

        for (Map.Entry<String, String> entry: shortUrlMap.entrySet()) {
            if (entry.getValue().equals(shortUrl)) {
                longUrl = entry.getKey();
                if (linkHitCount.containsKey(longUrl)) 
                    linkHitCount.put(longUrl, linkHitCount.get(longUrl)+1);
                else
                    linkHitCount.put(longUrl, 1);
                break;
            }
        }

        return longUrl;
    }

    @Override
    public Integer getHitCount(String longUrl) {
        return linkHitCount.containsKey(longUrl) ? linkHitCount.get(longUrl) : 0;
    }

    @Override
    public String delete(String longUrl) {
        String url = "";
        if (shortUrlMap.containsKey(longUrl)) {
            url = shortUrlMap.get(longUrl);
            shortUrlMap.remove(longUrl);
        }

        return url;
    }
}