package com.erpy.boardwang.board.WootDae;

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
 * Created by oj.bae on 2016. 2. 6..
 */
public class WootDaeExtractorWootDaeJaRyo {
    private static Logger logger = Logger.getLogger(WootDaeExtractorWootDaeJaRyo.class.getName());
    private static final String url = "";
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
        Elements elements = doc.select("div#cnts_list_new");
        for (Element element : elements) {
            Elements docSubElements = element.select("tr");
            for (Element docSubElement : docSubElements) {
                if (!docSubElement.outerHtml().contains("li_sbj")) {
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
                board.setCpNameDisplay("웃긴대학");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td.li_sbj a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl("http://web.humoruniv.com/board/humor/" + docLinkElement.attr("href"));
                            logger.info(" link : " + board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(Define.getHttpDelayTime());
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "euc-kr"));

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

                /**
                 * view count
                 */
                Elements docViewCountElements = docSubElement.select("td.li_und");
                for (Element docViewCountElement : docViewCountElements) {
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
                Elements docSuggestCountElements = docSubElement.select("span.o");
                for (Element docSuggestCountElement : docSuggestCountElements) {
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
                Elements docReplyCountElements = docSubElement.select("span.list_comment_num");
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
//                Elements docDateTimeElements = docSubElement.select("span.w_date");
//                for (Element docDateTimeElement : docDateTimeElements) {
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
                Elements docWriterElements = docSubElement.select("span.hu_nick_txt");
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
        StdUtils stdUtils = new StdUtils();
        Board board = new Board();

        /**
         * set data
         */
        Document doc = Jsoup.parse(body);

        /**
         * title
         */
        Elements elements = doc.select("table");
        for (Element element : elements) {
            if (!element.outerHtml().contains("ai_cm_title")) {
                continue;
            }
            // title
            Elements docSubElements = element.select("span#ai_cm_title");
            for (Element docSubElement : docSubElements) {
                title = docSubElement.text();
                if (title.length()>100) {
                    logger.error(" extract title length is long");
                    title="";
                }
                board.setTitle(title);
                break;
            }
            break;
        }

        /**
         * image url
         */
        Elements imageElements = doc.select("div#cnts");
        for (Element element : imageElements) {
            if (!element.outerHtml().contains("wrap_img")) {
                continue;
            }
            Elements docImageElements = element.select("div#wrap_img a img");
            for (Element docImageElement : docImageElements) {
                image = docImageElement.attr("src");
                if (image.length()>100) {
                    logger.error(" extract image length is long");
                    image = "";
                }
                board.setImageUrl(image);
                break;
            }
            break;
        }

        /**
         * date time
         */
        Elements dateElements = doc.select("div#if_date");
        for (Element element : dateElements) {
            Elements docDateElements = element.select("span[style*=font-size:11px]");
            for (Element docDateElement : docDateElements) {
                dateTime = docDateElement.text().trim().
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
        WootDaeExtractorWootDaeJaRyo wootDaeExtractorWootDaeJaRyo = new WootDaeExtractorWootDaeJaRyo();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/WootGinJaRyo_965616187.html", "utf-8");
        sourceMap.put("data", body);
        wootDaeExtractorWootDaeJaRyo.extractList(sourceMap);
    }
}
