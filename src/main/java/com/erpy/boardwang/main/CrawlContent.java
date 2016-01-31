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
        StdHttpHeaders stdHttpHeaders = new StdHttpHeaders();
        StdHttpUtils stdHttpUtils = new StdHttpUtils();

        stdHttpUtils.setRequestHeader(stdHttpHeaders.getHeader());
        stdHttpUtils.setCrawlEncode(encode);
        stdHttpUtils.setCrawlUrl(url);

        int returnCode = stdHttpUtils.HttpCrawlGetDataTimeout();
        if (returnCode != 200) {
            logger.info(" HTTP return : " + returnCode);
        }

        return stdHttpUtils.getCrawlData();
    }
}
