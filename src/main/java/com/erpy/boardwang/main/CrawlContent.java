package com.erpy.boardwang.main;

import com.erpy.boardwang.board.CPJjangOu;
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


    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        CPJjangOu cpJjangOu = new CPJjangOu();
        String url = "http://fun.jjang0u.com/chalkadak/view?db=160&no=245251";
        CrawlContent crawlContent = new CrawlContent();

        String body = crawlContent.execute(url, "utf-8");
        cpJjangOu.extractContent(body);
    }
}
