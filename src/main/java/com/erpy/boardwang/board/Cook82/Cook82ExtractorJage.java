package com.erpy.boardwang.board.Cook82;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.define.Define;
import com.erpy.boardwang.extrator.SubExtractor;
import com.erpy.boardwang.main.CrawlContent;
import com.erpyjune.StdFile;
import com.erpyjune.StdUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by oj.bae on 2016. 2. 20..
 */
public class Cook82ExtractorJage {
    private static Logger logger = Logger.getLogger(Cook82ExtractorJage.class.getName());
    private static final String url = "http://fun.jjang0u.com/chalkadak/list?db=160";
    private static final String encode = "utf-8";
    private String orgData = "";

    public String getUrl() {
        return url;
    }

    private enum DateTime {MIN, HOUR, DAY}


    /**
     *
     * @param sourceMap
     * @return
     * @throws Exception
     */
    public List<Board> extractList(Map<String, String> sourceMap) throws Exception {
        int index=0;
        String temp="";
        StdUtils stdUtils = new StdUtils();
        Board boardTemp = null;
        List<Board> list = new ArrayList<Board>();

        CrawlContent crawlContent = new CrawlContent();

        Document doc = Jsoup.parse(sourceMap.get("data"));
        Elements elements = doc.select("tbody");
        for (Element element : elements) {
            Elements docSubElements = element.select("tr");
            for (Element docSubElement : docSubElements) {

                if (!docSubElement.outerHtml().contains("<td class=\"numbers\">")) {
                    continue;
                }

                /**
                 * cleate board instance.
                 */
                Board board = new Board();

                /**
                 * set cp name
                 */
                board.setCpName(sourceMap.get("cp"));
                board.setCpNameDisplay("82쿡");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td.title a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl(docLinkElement.attr("href").replace("read.php","http://www.82cook.com/entiz/read.php"));
                    logger.info(" link : "+board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(Define.getHttpDelayTime());
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8"));

                board.setTitle(boardTemp.getTitle().trim());
                board.setDateTime(boardTemp.getDateTime());

                logger.info(" title : " + board.getTitle());
                logger.info(" date  : " + board.getDateTime());

//                /**
//                 * title
//                 */
//                Elements docTitleElements = docSubElement.select("td.title a");
//                for (Element docTitleElement : docTitleElements) {
//                    board.setTitle(docTitleElement.text());
//                    logger.info(" title : " + board.getTitle());
//                    break;
//                }

//                /**
//                 * image url
//                 */
//                Elements docThumbElements = docSubElement.select("li.img_sum a img");
//                for (Element docThumbElement : docThumbElements) {
//                    if (boardTemp.getThumbUrl().length()>0) {
//                        board.setImageUrl(boardTemp.getImageUrl());
//                    } else {
//                        board.setImageUrl(docThumbElement.attr("src"));
//                    }
//                    logger.info(" image : "+board.getThumbUrl());
//                    break;
//                }

                /**
                 * view count
                 */
                index=0;
                Elements docViewCountElements = docSubElement.select("td.numbers");
                for (Element docViewCountElement : docViewCountElements) {
                    if (index<2) {
                        index++;
                        continue;
                    }

                    String view = docViewCountElement.text().trim().replace(",", "");
                    if (stdUtils.isNumeric(view)) {
                        board.setViewCount(Integer.parseInt(view));
                        logger.info("view count : " + board.getViewCount());
                    } else {
                        logger.error(String.format(" view count is not number [%s] ", temp));
                    }
                    break;
                }

                /**
                 * reply count
                 */
                Elements docReplyCountElements = docSubElement.select("td.title em");
                for (Element docReplyCountElement : docReplyCountElements) {
                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim().replace("[","").replace("]","");
                    if (stdUtils.isNumeric(temp)) {
                        board.setReplyCount(Integer.parseInt(temp));
                        logger.info(" reply count : " + board.getReplyCount());
                    } else {
                        logger.error(String.format(" reply count is not number [%s] ", temp));
                        board.setReplyCount(0);
                    }
                    break;
                }

                /**
                 * suggest count
                 */
                board.setSuggestCount(board.getReplyCount() * 2);

//                /**
//                 * date time
//                 *
//                 */
//                Elements docDateTimeElements = docSubElement.select("span.b");
//                for (Element docDateTimeElement : docDateTimeElements) {
//                    temp = docDateTimeElement.text();
//                    if (temp.contains(":")) {
//                        String currDate = stdUtils.getCurrDate();
//                        temp = currDate + temp.trim().replace(":","").substring(0,4);
//                    } else {
//                        temp = stdUtils.getMinutesBeforeAfter(0).substring(0, 12);
//                    }
//
//                    board.setDateTime(temp);
//                    logger.info(" dateTime : " + board.getDateTime());
//                    break;
//                }

//                /**
//                 * whiter
//                 */
//                Elements docWriterElements = docSubElement.select("td.author02 span.author");
//                for (Element docWriterElement : docWriterElements) {
//                    board.setWriter(docWriterElement.text());
//                    logger.info(" writer : " + board.getWriter());
//                    break;
//                }

                list.add(board);
                logger.info(" total ##################################################### : " + list.size());
            }
        }

        return list;
    }


    /**
     *
     * @param body
     * @return
     * @throws Exception
     */
    public Board extractContent(String body) throws Exception {
        Board board = new Board();
        StdUtils stdUtils = new StdUtils();
        SubExtractor subExtractor = new SubExtractor();


        /**
         * title
         */
        String title = subExtractor.subExtract(body,"div#column2", "h4[class*=title bbstitle]", "text");
        if (title.length()>200) {
            logger.error(" extract title is long [" + title + "]");
            title="";
        }
        board.setTitle(title.replace("제 목 : ",""));

//        /**
//         * image url
//         */
//        String image = subExtractor.subExtract(body,"div#KH_Content", "img", "src");
//        if (image.length()>200) {
//            logger.error(" extract image is long [" + image + "]");
//            image="";
//        }
//        board.setImageUrl(image);

        /**
         * date time
         */
        String dateTime = subExtractor.subExtract(body,"div#readHead", "div.readRight", "text");
        if (dateTime.length()>200) {
            logger.error(" extract date time is long [" + dateTime + "]");
            dateTime="";
        }
        dateTime = dateTime.replace("작성일 : ","").replace(" ","").replace("-","").replace(":","");
        board.setDateTime(dateTime.substring(0,12));


        return board;
    }

    void testExtract(String bodyFilePath) throws Exception {
        StdFile stdFile = new StdFile();
        Cook82ExtractorJage ppombbuExtractorJage = new Cook82ExtractorJage();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString(bodyFilePath, "utf-8");
        sourceMap.put("data", body);
        ppombbuExtractorJage.extractList(sourceMap);
    }

    void testExtractBody(String bodyUrl) throws Exception {
        CrawlContent crawlContent = new CrawlContent();
        Cook82ExtractorJage ppombbuExtractorJage = new Cook82ExtractorJage();

        Board board = ppombbuExtractorJage.extractContent(crawlContent.execute(bodyUrl, "utf-8"));

        System.out.println("title [" + board.getTitle() + "]");
        System.out.println("image [" + board.getImageUrl() + "]");
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        Cook82ExtractorJage ppombbuExtractorJage = new Cook82ExtractorJage();
        ppombbuExtractorJage.testExtract("/Users/oj.bae/Work/BoardWang/crawl_data/82cook.html");
//        appZzangExtractorJayuGesipan.testExtractBody("http://www.bobaedream.co.kr/view?code=best&No=65629&vdate=");
    }
}
