package com.erpy.boardwang.extrator;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by oj.bae on 2016. 2. 13..
 */
public class SubExtractor {
    private static Logger logger = Logger.getLogger(SubExtractor.class.getName());

    public String subExtract(String body, String areaPart, String dataPart, String extractField) throws Exception {
        String value="";
        Document doc = Jsoup.parse(body);
        Elements elements = doc.select(areaPart);
        for (Element element : elements) {
            Elements docSubElements = element.select(dataPart);
            for (Element docSubElement : docSubElements) {
                if ("text".equals(extractField)) {
                    value = docSubElement.text();
                } else if ("html".equals(extractField)) {
                    value = docSubElement.outerHtml();
                } else {
                    value = docSubElement.attr(extractField);
                }
                break;
            }
            break;
        }
        return value;
    }

    public String subExtract(String body, String areaPart, String dataPart, String extractField, String skipTag) throws Exception {
        String value="";
        Document doc = Jsoup.parse(body);
        Elements elements = doc.select(areaPart);
        for (Element element : elements) {
            Elements docSubElements = element.select(dataPart);
            for (Element docSubElement : docSubElements) {
                if ("text".equals(extractField)) {
                    value = docSubElement.text();
                } else if ("html".equals(extractField)) {
                    value = docSubElement.outerHtml();
                } else {
                    value = docSubElement.attr(extractField);
                }
                if (skipTag.length()>0) {
                    if (value.contains(skipTag)) {
                        continue;
                    }
                }
                break;
            }
            break;
        }
        return value;
    }
}
