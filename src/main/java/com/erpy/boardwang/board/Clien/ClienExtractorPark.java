package com.erpy.boardwang.board.Clien;

import com.erpy.boardwang.Data.Board;
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
 * Created by oj.bae on 2016. 1. 31..
 */
public class ClienExtractorPark {
    private static Logger logger = Logger.getLogger(ClienExtractorPark.class.getName());
    private static final String url = "http://fun.jjang0u.com/chalkadak/list?db=160";
    private static final String encode = "utf-8";
    private String orgData = "";

    public String getUrl() {
        return url;
    }


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
            Elements docSubElements = element.select("tr.mytr");
            for (Element docSubElement : docSubElements) {
                /**
                 * cleate board instance.
                 */
                Board board = new Board();

                /**
                 * set cp name
                 */
                board.setCpName(sourceMap.get("cp"));
                board.setCpNameDisplay("클리앙");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td.post_subject a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl(docLinkElement.attr("href").replace("../bbs", "http://www.clien.net/cs2/bbs"));
                    logger.info(" link : "+board.getUrl());
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
//                Elements docTitleElements = docSubElement.select("td.td02 a");
//                for (Element docTitleElement : docTitleElements) {
//                    if (boardTemp.getTitle().length()>0) {
//                        // 본문 내용에서 추출한 것으로 대체
//                        board.setTitle(boardTemp.getTitle());
//                    } else {
//                        board.setTitle(docTitleElement.text());
//                    }
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
//
                /**
                 * view count
                 */
                int index=0;
                Elements docViewCountElements = docSubElement.select("td");
                for (Element docViewCountElement : docViewCountElements) {
                    if (index<4) {
                        index++;
                        continue;
                    }

                    temp = docViewCountElement.text().trim();
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

//                /**
//                 * suggest count
//                 */
//                Elements docSuggestCountElements = docSubElement.select("li.ckd_redit01 span.rdt03");
//                for (Element docSuggestCountElement : docSuggestCountElements) {
//                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).replace("&nbsp;","").trim();
//                    if (stdUtils.isNumeric(temp)) {
//                        board.setSuggestCount(Integer.parseInt(temp));
//                        logger.info("suggest count : " + board.getSuggestCount());
//                    } else {
//                        logger.error(String.format(" suggest count is not number [%s] ", temp));
//                        board.setSuggestCount(0);
//                    }
//                    break;
//                }

                /**
                 * reply count
                 */
                Elements docReplyCountElements = docSubElement.select("td.post_subject span");
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

//                /**
//                 * date time
//                 */
//                index = 0;
//                Elements docDateTimeElements = docSubElement.select("td");
//                for (Element docDateTimeElement : docDateTimeElements) {
//                    if (index<3) {
//                        index++;
//                        continue;
//                    }
//
//                    temp = docDateTimeElement.text();
//
//                    // 오늘날짜임
//                    if (temp.indexOf(':')>0) {
//                        board.setDateTime(stdUtils.getCurrDate());
//                    } else {
//                        board.setDateTime(temp.replace("-",""));
//                    }
//
//                    logger.info(" dateTime : " + board.getDateTime());
//
//                    break;
//                }

                /**
                 * whiter
                 */
                Elements docWriterElements = docSubElement.select("span.member");
                for (Element docWriterElement : docWriterElements) {
                    board.setWriter(docWriterElement.text());
                    logger.info(" writer : "+board.getWriter());
                    break;
                }

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

        /**
         * set doc
         */
        Document doc = Jsoup.parse(body);

        /**
         * image url
         */
        String image="";
        Elements imageElements = doc.select("div#resContents");
        for (Element element : imageElements) {
            Elements docSubElements = element.select("img");
            for (Element docSubElement : docSubElements) {
                image = docSubElement.attr("src");
                board.setImageUrl(image);
                break;
            }
            break;
        }


        /**
         * date time
         */
        String date="";
        Elements elements = doc.select("div.board_main");
        for (Element element : elements) {
            Elements docSubElements = element.select("p.post_info");
            for (Element docSubElement : docSubElements) {
                date = docSubElement.text().trim().substring(0, 16).
                        replace("-","").replace(" ","").replace(":","");
                board.setDateTime(date);
                break;
            }
            break;
        }

        /**
         * title
         */
        elements = doc.select("div.board_main");
        for (Element element : elements) {
            Elements docSubElements = element.select("div.view_title");
            for (Element docSubElement : docSubElements) {
                String title = docSubElement.text().trim();
                board.setTitle(title);
                break;
            }
            break;
        }

        return board;
    }

    void testExtract(String bodyFilePath) throws Exception {
        StdFile stdFile = new StdFile();
        ClienExtractorPark clienExtractorPark = new ClienExtractorPark();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString(bodyFilePath, "utf-8");
        sourceMap.put("data", body);
        clienExtractorPark.extractList(sourceMap);
    }

    void testExtractBody(String bodyUrl) throws Exception {
        CrawlContent crawlContent = new CrawlContent();
        ClienExtractorPark clienExtractorPark = new ClienExtractorPark();

        Board board = clienExtractorPark.extractContent(crawlContent.execute(bodyUrl, "utf-8"));

        System.out.println("title [" + board.getTitle() + "]");
        System.out.println("image [" + board.getImageUrl() + "]");
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        ClienExtractorPark clienExtractorPark = new ClienExtractorPark();
//        bobaeExtractorHumour.testExtract("/Users/oj.bae/Work/BoardWang/crawl_data/BobaeHumour_98370181.html");
        clienExtractorPark.testExtractBody("http://www.clien.net/cs2/bbs/board.php?bo_table=park&wr_id=44366789");
    }
}
