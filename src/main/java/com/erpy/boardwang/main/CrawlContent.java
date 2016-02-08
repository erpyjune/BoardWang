package com.erpy.boardwang.main;

import com.erpyjune.StdHttpHeaders;
import com.erpyjune.StdHttpUtils;
import org.apache.log4j.Logger;

/**
 * Created by oj.bae on 2016. 1. 24..
 */
public class CrawlContent {
    private static Logger logger = Logger.getLogger(CrawlContent.class.getName());

    /**
     *
     * @param url
     * @param encode
     * @return
     * @throws Exception
     */
    public String execute(String url, String encode) throws Exception {
        int retryCount=0;
        StdHttpHeaders stdHttpHeaders = new StdHttpHeaders();
        StdHttpUtils stdHttpUtils = new StdHttpUtils();

        if (url.trim().length()==0) {
            logger.info(" Body crawl url is empty");
            return "";
        }

        stdHttpUtils.setRequestHeader(stdHttpHeaders.getHeader());
        stdHttpUtils.setCrawlEncode(encode);
        stdHttpUtils.setCrawlUrl(url);

        while (true) {
            try {
                int returnCode = stdHttpUtils.HttpCrawlGetDataTimeout();
                if (returnCode != 200) {
                    logger.info(" HTTP error return : " + returnCode);
                } else {
                    break;
                }
            } catch (Exception e) {
                logger.info(String.format(" Retry [%d] http crawling [%s]", retryCount, url));
                Thread.sleep(1500);
            }

            if (retryCount>5) {
                logger.info(" Breaking retry count " + retryCount);
                break;
            }
            retryCount++;
        }

        return stdHttpUtils.getCrawlData();
    }
}
