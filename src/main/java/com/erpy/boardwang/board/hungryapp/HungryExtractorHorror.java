package com.erpy.boardwang.board.hungryapp;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.define.Define;
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
public class HungryExtractorHorror extends Board {
    private static Logger logger = Logger.getLogger(HungryExtractorHorror.class.getName());
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
        Elements elements = doc.select("table.tablef");
        for (Element element : elements) {
            Elements docSubElements = element.select("tr.deftr");
            for (Element docSubElement : docSubElements) {
                /**
                 * cleate board instance.
                 */
                Board board = new Board();

                /**
                 * set cp name
                 */
                board.setCpName(sourceMap.get("cp"));
                board.setCpNameDisplay("헝그리앱");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td.td02 a");
                for (Element docLinkElement : docLinkElements) {
                    String scriptStr = docLinkElement.attr("href");
                    String boardNum = stdUtils.getFieldData(scriptStr, "sendView(", ",'')");
                    board.setUrl("http://www.hungryapp.co.kr/bbs/bbs_view.php?pid=" + boardNum + "&bcode=horror");
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
                Elements docViewCountElements = docSubElement.select("td.td05 span.viewnum");
                for (Element docViewCountElement : docViewCountElements) {
                    temp = stdUtils.removeSpace(docViewCountElement.text()).replace(" ","").trim();
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
//
                /**
                 * reply count
                 */
                Elements docReplyCountElements = docSubElement.select("td.td02 span.renum");
                for (Element docReplyCountElement : docReplyCountElements) {
                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim().replace("(","").replace(")","");
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
//                Elements docDateTimeElements = docSubElement.select("td.td04 span.time");
//                for (Element docDateTimeElement : docDateTimeElements) {
//                    temp = docDateTimeElement.text().trim();
//
//                    // 오늘날짜임
//                    if (temp.indexOf(':')>0) {
//                        board.setDateTime(stdUtils.getCurrDate());
//                    } else {
//                        board.setDateTime(temp.replace(".",""));
//                    }
//
//                    logger.info(" dateTime : " + board.getDateTime());
//
//                    break;
//                }

                /**
                 * whiter
                 */
                Elements docWriterElements = docSubElement.select("td.td03 a");
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
        String title="";
        String image="";
        String dateTime="";
        Board board = new Board();

        /**
         * set doc
         */
        Document doc = Jsoup.parse(body);

        Elements elements = doc.select("div.board-view");
        for (Element element : elements) {
            /**
             * title
             */
            Elements docSubElements = element.select("div.info-area font");
            for (Element docSubElement : docSubElements) {
                title = docSubElement.text();
                if (title.length()>100) {
                    logger.error(" extract title length is long");
                    title="";
                }
                board.setTitle(title);
                break;
            }

            /**
             * image
             */
            Elements docImageElements = element.select("div.bbs_cont img");
            for (Element docImageElement : docImageElements) {
                image = docImageElement.attr("src");
                if (image.length()>100) {
                    logger.error(" extract image length is long");
                    image = "";
                }
                board.setImageUrl(image);
                break;
            }

            /**
             * date time
             */
            Elements docDateTimeElements = element.select("ul.info02");
            for (Element docDateTimeElement : docDateTimeElements) {
                dateTime = docDateTimeElement.text().replace("등록일","").
                        replace(" ","").replace("-","").replace(":","").substring(0,12);
                if (dateTime.length()>100) {
                    logger.error(" extract date time length is long");
                    dateTime = "";
                }
                board.setDateTime(dateTime);
                break;
            }
            break;
        }

        return board;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        StdFile stdFile = new StdFile();
        HungryExtractorHorror hungryExtractorHorror = new HungryExtractorHorror();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/HungryAppHorror_924826020.html", "utf-8");
        sourceMap.put("data", body);
        hungryExtractorHorror.extractList(sourceMap);
    }
}
