package com.erpy.boardwang.board.Chuing;

import com.erpy.boardwang.Data.Board;
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
public class ChuingExtractorHumour {
    private static Logger logger = Logger.getLogger(ChuingExtractorHumour.class.getName());
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
        Elements elements = doc.select("table.info_bg");
        for (Element element : elements) {
            Elements docSubElements = element.select("tr[class*=list]");
            for (Element docSubElement : docSubElements) {
                /**
                 * cleate board instance.
                 */
                Board board = new Board();

                /**
                 * set cp name
                 */
                board.setCpName(sourceMap.get("cp"));
                board.setCpNameDisplay("츄잉");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("tbody a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl("http://www.chuing.net/zboard/" + docLinkElement.attr("href"));
                            logger.info(" link : " + board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(300);
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8"));

                if (boardTemp.getImageUrl().length() > 128) {
                    logger.error(" long image url");
                }

                board.setTitle(boardTemp.getTitle().trim());
                board.setImageUrl(boardTemp.getImageUrl());
                board.setDateTime(boardTemp.getDateTime());

                logger.info(" title : " + board.getTitle());
                logger.info(" imgae : " + board.getImageUrl());
                logger.info(" date  : " + board.getDateTime());

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

                /**
                 * view count
                 */
                int index=0;
                Elements docViewCountElements = docSubElement.select("td[class*=text-center en font-11]");
                for (Element docViewCountElement : docViewCountElements) {
                    if (index<1) {
                        index++;
                        continue;
                    }
                    temp = docViewCountElement.text().trim().replace(",","");
                    if (stdUtils.isNumeric(temp)) {
                        board.setViewCount(Integer.parseInt(temp));
                        logger.info("view count : " + board.getViewCount());
                    } else {
                        logger.error(String.format(" view count is not number [%s] ", temp));
                        board.setViewCount(0);
                        for (char c : temp.toCharArray()) {
                            if (Character.isSpaceChar(c)) {
                                logger.info(String.format(" SPACE char[%c]", c));
                            } else {
                                logger.info(String.format(" char[%c]", c));
                            }
                        }
                    }

                    break;
                }

                /**
                 * suggest count
                 */
                index=0;
                Elements docSuggestCountElements = docSubElement.select("td[class*=text-center en font-11]");
                for (Element docSuggestCountElement : docSuggestCountElements) {
                    if (index<2) {
                        index++;
                        continue;
                    }
                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).trim().replace(",","");
                    if (stdUtils.isNumeric(temp)) {
                        board.setSuggestCount(Integer.parseInt(temp));
                        logger.info("suggest count : " + board.getSuggestCount());
                    } else {
                        logger.error(String.format(" suggest count is not number [%s] ", temp));
                        board.setSuggestCount(0);
                    }
                    break;
                }

                /**
                 * reply count
                 */
                Elements docReplyCountElements = docSubElement.select("span.cnt_cmt");
                for (Element docReplyCountElement : docReplyCountElements) {
                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim();
                    if (stdUtils.isNumeric(temp)) {
                        board.setReplyCount(Integer.parseInt(temp));
                        logger.info(" reply count : " + board.getReplyCount());
                    } else {
                        logger.error(String.format(" reply count is not number [%s] ", temp));
                        board.setReplyCount(0);
                    }
                    break;
                }

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
        image = subExtractor.subExtract(body,"table.pic_bg", "img", "src");
        if (image.length()>200) {
            logger.error(" extract image is long [" + image + "]");
            image="";
        }
        board.setImageUrl(image);

        /**
         * title
         */
        title = subExtractor.subExtract(body,"tbody td", "font.view_title2", "text");
        if (title.length()>100) {
            logger.error(" extract title is long [" + title + "]");
            title="";
        }
        board.setTitle(title);

        /**
         * date time
         */
        date = subExtractor.subExtract(body,"div.panel-heading", "span.pull-right", "text").
                replace(" ","").replace("\n","");
        if (date.length()>100) {
            logger.error(" extract date-time is long [" + date + "]");
            date="";
        } else {
            int min=0, hour=0, day=0;
            min = date.indexOf("분전");
            hour = date.indexOf("시간전");
            day = date.indexOf("일전");
            if (min>0) {
                String temp = date.substring(0,min);
                date = stdUtils.getMinutesBeforeAfter(-Integer.parseInt(temp));
            } else if (hour>0) {
                String temp = date.substring(0,hour);
                date = stdUtils.getMinutesBeforeAfter(-Integer.parseInt(temp));
            } else if (day>0) {
                String temp = date.substring(0,day);
                date = stdUtils.getMinutesBeforeAfter(-Integer.parseInt(temp));
            }
        }
        board.setDateTime(date);



        return board;
    }

    void testExtract(String bodyFilePath) throws Exception {
        StdFile stdFile = new StdFile();
        ChuingExtractorHumour chuingExtractorHumour = new ChuingExtractorHumour();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString(bodyFilePath, "utf-8");
        sourceMap.put("data", body);
        chuingExtractorHumour.extractList(sourceMap);
    }

    void testExtractBody(String bodyUrl) throws Exception {
        CrawlContent crawlContent = new CrawlContent();
        ChuingExtractorHumour chuingExtractorHumour = new ChuingExtractorHumour();

        Board board = chuingExtractorHumour.extractContent(crawlContent.execute(bodyUrl, "utf-8"));

        System.out.println("title [" + board.getTitle() + "]");
        System.out.println("image [" + board.getImageUrl() + "]");
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        ChuingExtractorHumour chuingExtractorHumour = new ChuingExtractorHumour();
        chuingExtractorHumour.testExtract("/Users/oj.bae/Work/BoardWang/crawl_data/ChuingHumour_836420955.html");
//        appZzangExtractorJayuGesipan.testExtractBody("http://www.bobaedream.co.kr/view?code=best&No=65629&vdate=");
    }
}
