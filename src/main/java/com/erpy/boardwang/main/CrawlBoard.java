package com.erpy.boardwang.main;

import com.erpy.boardwang.define.Define;
import com.erpyjune.StdFile;
import com.erpyjune.StdHttpHeaders;
import com.erpyjune.StdHttpUtils;
import com.erpyjune.StdUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by oj.bae on 2016. 1. 17..
 */
public class CrawlBoard {
    private static Logger logger = Logger.getLogger(CrawlBoard.class.getName());
    public static void main(String args[]) throws Exception {
        int index=0;
        String saveFilePath, cpName="", seedUrl="";
        StdHttpHeaders stdHttpHeaders = new StdHttpHeaders();
        StdHttpUtils stdHttpUtils = new StdHttpUtils();
        StdUtils stdUtils = new StdUtils();
        StdFile stdFile = new StdFile();

        /**
         * set http request headers
         */
        stdHttpUtils.setRequestHeader(stdHttpHeaders.getHeader());

        /**
         * set http request info
         */
        stdHttpUtils.setCrawlEncode("utf-8");

        /**
         * read seed file
         */
        List<String> seedList = stdFile.fileReadLineToList(Define.getSeedFilePath());
        for (String seed : seedList) {

            System.out.println("seed:" + seed);

            if (seed.startsWith("#")) continue;

            index=0;
            StringTokenizer st = new StringTokenizer(seed);
            while(st.hasMoreTokens()) {
                if (index==0) {
                    cpName = st.nextToken();
                } else if (index == 1) {
                    seedUrl = st.nextToken();
                }
                index++;
            }

            /**
             * crawling data
             */
            stdHttpUtils.setCrawlUrl(seedUrl);

            /**
             * crawling data
             */
            int returnCode = stdHttpUtils.HttpCrawlGetDataTimeout();
            if (returnCode != 200) {
                logger.info("error return code : " + returnCode );
                return;
            }

            /**
             * save crawling data
             */
            saveFilePath = String.format("%s/%s_%d.html", Define.getSaveDir(), cpName, stdUtils.getRandomNumber(999999999));
            stdFile.fileWriteFromString(stdHttpUtils.getCrawlData(), saveFilePath);
            logger.info("save : " + saveFilePath);
        }

        System.out.println("End program!!");
    }
}
