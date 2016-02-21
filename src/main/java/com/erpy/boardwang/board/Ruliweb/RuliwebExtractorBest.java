package com.erpy.boardwang.board.Ruliweb;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oj.bae on 2016. 2. 14..
 */
public class RuliwebExtractorBest {
    private static Logger logger = Logger.getLogger(RuliwebExtractorBest.class.getName());
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

                if (!docSubElement.outerHtml().contains("<a href=\"http://bbs2.ruliweb.daum.net")) {
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
                board.setCpNameDisplay("루리웹");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl(docLinkElement.attr("href"));
                    logger.info(" link : "+board.getUrl());
                    break;
                }

                if (board.getUrl().trim().length() == 0) {
                    continue;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(Define.getHttpDelayTime());
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8"));

                board.setTitle(boardTemp.getTitle().trim());
                board.setImageUrl(boardTemp.getImageUrl());
                board.setDateTime(boardTemp.getDateTime());
                board.setReplyCount(boardTemp.getReplyCount());
                board.setSuggestCount(boardTemp.getSuggestCount());
                board.setViewCount(boardTemp.getViewCount());

                logger.info("title: " + board.getTitle());
                logger.info("image: " + board.getImageUrl());
                logger.info("date : " + board.getDateTime());
                logger.info("reply: " + board.getReplyCount());
                logger.info("suggest: " + board.getSuggestCount());
                logger.info("view : " + board.getViewCount());

//                /**
//                 * title
//                 */
//                Elements docTitleElements = docSubElement.select("td a.bsubject");
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

//                /**
//                 * view count
//                 */
//                Elements docViewCountElements = docSubElement.select("td.hit");
//                for (Element docViewCountElement : docViewCountElements) {
//                    temp = docViewCountElement.text().trim().replace(",","");
//                    if (stdUtils.isNumeric(temp)) {
//                        board.setViewCount(Integer.parseInt(temp));
//                        logger.info("view count : " + board.getViewCount());
//                    } else {
//                        logger.error(String.format(" view count is not number [%s] ", temp));
//                        board.setViewCount(0);
//                        for (char c : temp.toCharArray()) {
//                            if (Character.isSpaceChar(c)) {
//                                logger.info(String.format(" SPACE char[%c]", c));
//                            } else {
//                                logger.info(String.format(" char[%c]", c));
//                            }
//                        }
//                    }
//
//                    break;
//                }
//
//                /**
//                 * suggest count
//                 */
//                Elements docSuggestCountElements = docSubElement.select("td.recomd");
//                for (Element docSuggestCountElement : docSuggestCountElements) {
//                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).trim().replace(",","");
//                    if (stdUtils.isNumeric(temp)) {
//                        board.setSuggestCount(Integer.parseInt(temp));
//                        logger.info("suggest count : " + board.getSuggestCount());
//                    } else {
//                        logger.error(String.format(" suggest count is not number [%s] ", temp));
//                        board.setSuggestCount(0);
//                    }
//                    break;
//                }
//
//                /**
//                 * reply count
//                 */
//                Elements docReplyCountElements = docSubElement.select("span.num");
//                for (Element docReplyCountElement : docReplyCountElements) {
//                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim();
//                    if (stdUtils.isNumeric(temp)) {
//                        board.setReplyCount(Integer.parseInt(temp));
//                        logger.info(" reply count : " + board.getReplyCount());
//                    } else {
//                        logger.error(String.format(" reply count is not number [%s] ", temp));
//                        board.setReplyCount(0);
//                    }
//                    break;
//                }

//                /**
//                 * date time
//                 * 날짜 형식 : 01/30
//                 * 오늘 날짜 형식 : 22:16
//                 */
//                Elements docDateTimeElements = docSubElement.select("td.date");
//                for (Element docDateTimeElement : docDateTimeElements) {
//                    temp = docDateTimeElement.text();
//                    if (temp.contains(":")) {
//                        temp = stdUtils.getCurrDate();
//                    } else if (temp.contains("/")) {
//                        StringTokenizer st = new StringTokenizer(temp,"/");
//                        int index = 0;
//                        StringBuffer sb = new StringBuffer("2016");
//                        while (st.hasMoreTokens()) {
//                            String token = st.nextToken();
//                            if (index==0) {
//                                sb.append(token);
//                            } else if (index==1) {
//                                sb.append(token);
//                            }
//                            index++;
//                        }
//                        temp = sb.toString();
//                    } else {
//                        temp = stdUtils.getCurrDate();
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
        String image="";
        String title="";
        String date="";
        Board board = new Board();
        StdUtils stdUtils = new StdUtils();
        SubExtractor subExtractor = new SubExtractor();

        /**
         * image url
         */
        image = subExtractor.subExtract(body,"table.read_cont_table", "img", "src");
        if (image.length()>200) {
            logger.error(" extract image is long [" + image + "]");
            image="";
        }
        board.setImageUrl(image);

        /**
         * title
         */
        title = subExtractor.subExtract(body,"div.view_title", "div.tit_user", "text");
        if (title.length()>100) {
            logger.error(" extract title is long [" + title + "]");
            title="";
        }
        board.setTitle(title);

        /**
         * date time
         */
        date = subExtractor.subExtract(body,"div.box_title", "li.time", "text");
        if (date.length()>100) {
            logger.error(" extract date-time is long [" + date + "]");
            date="";
        } else {
            String temp = date.replace(" ","").replace("일시","").replace(".","").replace("(","").replace(")","").replace(":","");
            date = temp.substring(0, 12);
        }
        board.setDateTime(date);

        /**
         * reply count
         */
        String reply = subExtractor.subExtract(body,"div.tit_user", "span.num_reply", "text").trim().
                replace("[","").replace("]", "").replace(",", "").replace(" ","");
        if (reply.length()>0) {
            if (stdUtils.isNumeric(reply)) {
                board.setReplyCount(Integer.parseInt(reply));
            } else {
                logger.error(" reply is not Number [" + reply + "]");
            }
        }

        /**
         * suggest count
         */
        String suggest = subExtractor.subExtract(body, "ul.list_report", "li.recommand", "text").trim().
                replace("|", "").replace("추천", "").replace(" ","");
        if (suggest.length()>0) {
            if (stdUtils.isNumeric(suggest)) {
                board.setSuggestCount(Integer.parseInt(suggest));
            } else {
                logger.error(" suggest is not Number [" + suggest + "]");
            }
        }

        /**
         * view count
         */
        String view = subExtractor.subExtract(body, "ul.list_report", "li.hit", "text").trim().
                replace("|", "").replace("조회", "").replace(" ", "");
        if (view.length()>0) {
            if (stdUtils.isNumeric(view)) {
                board.setViewCount(Integer.parseInt(view));
            } else {
                logger.error(" view is not Number [" + view + "]");
            }
        }

        return board;
    }

    void testExtract(String bodyFilePath) throws Exception {
        StdFile stdFile = new StdFile();
        RuliwebExtractorBest cp = new RuliwebExtractorBest();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString(bodyFilePath, "utf-8");
        sourceMap.put("data", body);
        cp.extractList(sourceMap);
    }

    void testExtractBody(String bodyUrl) throws Exception {
        CrawlContent crawlContent = new CrawlContent();
        RuliwebExtractorBest cp = new RuliwebExtractorBest();

        Board board = cp.extractContent(crawlContent.execute(bodyUrl, "utf-8"));

        logger.info("title: " + board.getTitle());
        logger.info("image: " + board.getImageUrl());
        logger.info("date : " + board.getDateTime());
        logger.info("reply: " + board.getReplyCount());
        logger.info("suggest: " + board.getSuggestCount());
        logger.info("view : " + board.getViewCount());
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        RuliwebExtractorBest cp = new RuliwebExtractorBest();
        cp.testExtract("/Users/oj.bae/Work/BoardWang/crawl_data/ruli.html");
//        cp.testExtractBody("http://bbs1.ruliweb.daum.net/gaia/do/ruliweb/default/hobby/1475/read?articleId=358837&objCate1=946&bbsId=G002&platformId=4");
    }
}
