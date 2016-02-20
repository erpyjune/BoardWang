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
         * checker argument
         */
        if (args.length != 2) {
            logger.error(" Argument error !!");
            logger.error(" (usage) [1]crawl_data_path [2]crawl_seed_file_path ");
            return;
        }

        String crawlDataPath = args[0];
        String seedPath = args[1];
        logger.info(" crawl data path [" + crawlDataPath + "]");
        logger.info(" seed  data path [" + seedPath + "]");

        /**
         * set http request headers
         */
        stdHttpUtils.setRequestHeader(stdHttpHeaders.getHeader());

        /**
         * read seed file
         */
        List<String> seedList = stdFile.fileReadLineToList(seedPath);
        for (String seed : seedList) {

            System.out.println("seed:" + seed);

            if (seed.startsWith("#") || seed.trim().length()==0) continue;

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
             * set encoding
             */
            if ("WootGinJaRyo".equals(cpName) || "WootGinHumour".equals(cpName) ||
                    ("PpombbuHumour".equals(cpName)) || ("PpombbuJageRecency".equals(cpName)) || ("PpombbuJageIngi".equals(cpName)) || ("PpombbuJageHot".equals(cpName))) {
                stdHttpUtils.setCrawlEncode("euc-kr");
            } else {
                stdHttpUtils.setCrawlEncode("utf-8");
            }

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
            saveFilePath = String.format("%s/%s_%d.html", crawlDataPath, cpName, stdUtils.getRandomNumber(999999999));
            stdFile.fileWriteFromString(stdHttpUtils.getCrawlData(), saveFilePath);
            logger.info("save : " + saveFilePath);
        }

        System.out.println("End program!!");
    }
}
