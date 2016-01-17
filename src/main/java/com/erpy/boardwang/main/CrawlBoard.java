package com.erpy.boardwang.main;

import com.erpy.boardwang.board.CPJjangOu;
import com.erpyjune.StdHttpHeaders;
import com.erpyjune.StdHttpUtils;
import org.apache.log4j.Logger;

/**
 * Created by oj.bae on 2016. 1. 17..
 */
public class CrawlBoard {
    private static Logger logger = Logger.getLogger(CrawlBoard.class.getName());
    public static void main(String args[]) throws Exception {
        StdHttpHeaders stdHttpHeaders = new StdHttpHeaders();
        StdHttpUtils stdHttpUtils = new StdHttpUtils();

        /**
         * set http request headers
         */
        stdHttpUtils.setRequestHeader(stdHttpHeaders.getHeader());

        /**
         * set http request info
         */
        stdHttpUtils.setCrawlEncode("utf-8");
        stdHttpUtils.setCrawlUrl(CPJjangOu.getUrl());

        /**
         * crawling data
         */
        int returnCode = stdHttpUtils.HttpCrawlGetDataTimeout();
        if (returnCode != 200) {
            logger.info("error return code : " + returnCode );
            return;
        }

        /**
         * extract data
         */

        /**
         * insert to db
         */

        logger.info(stdHttpUtils.getCrawlData());

        System.out.println("End program!!");
    }
}
