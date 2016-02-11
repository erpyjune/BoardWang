package com.erpy.boardwang.board.TodayHumour;

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
 * Created by oj.bae on 2016. 2. 11..
 */
public class TodayHumourExtractorBest {
    private static Logger logger = Logger.getLogger(TodayHumourExtractorBest.class.getName());
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
        String cookieValue="__cfduid=ded67f332d77a70f69ad8bbb182a61cfd1455200544; __utmc=186020908; __utmz=186020908.1455200552.1.1.utmccn=(referral)|utmcsr=google.co.kr|utmcct=/|utmcmd=referral; outc=l6elmZeqytmgp9GPkZCVpaOZlquYpaKlqA%3D%3D; siteUniqId=STU_56bc9a8d3a75c; __utmb=186020908; __utma=186020908.938494754.1455200552.1455200552.1455200552.1";
        String temp="";
        StdUtils stdUtils = new StdUtils();
        Board boardTemp = null;
        List<Board> list = new ArrayList<Board>();

        CrawlContent crawlContent = new CrawlContent();

        Document doc = Jsoup.parse(sourceMap.get("data"));
        Elements elements = doc.select("table.table_list");
        for (Element element : elements) {
            Elements docSubElements = element.select("tr[class*=view list_tr_]");
            for (Element docSubElement : docSubElements) {
                if (!docSubElement.outerHtml().contains("/board/view.php")) {
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
                board.setCpNameDisplay("오늘의유머");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td.subject a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl("http://www.todayhumor.co.kr" + docLinkElement.attr("href"));
                    logger.info(" link : " + board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(300);
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8", "Cookie:", cookieValue));

                board.setTitle(boardTemp.getTitle().trim());
                board.setImageUrl(boardTemp.getImageUrl());

                logger.info(" title : " + board.getTitle());
                logger.info(" imgae : " + board.getImageUrl());


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
                Elements docViewCountElements = docSubElement.select("td.hits");
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
                Elements docSuggestCountElements = docSubElement.select("td.oknok");
                for (Element docSuggestCountElement : docSuggestCountElements) {
                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).trim().replace(" ","").replace("[","").replace("]","");
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
                Elements docReplyCountElements = docSubElement.select("span.list_memo_count_span");
                for (Element docReplyCountElement : docReplyCountElements) {
                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim().replace(" ","").replace("[","").replace("]","");
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
                 * date time
                 */
                Elements docDateTimeElements = docSubElement.select("td.date");
                for (Element docDateTimeElement : docDateTimeElements) {
                    temp = "20" + docDateTimeElement.text().trim().replace(" ","").replace("/","").replace(":","");
                    board.setDateTime(temp);
                    logger.info(" dateTime : " + board.getDateTime());
                    break;
                }

                /**
                 * whiter
                 */
                Elements docWriterElements = docSubElement.select("td.name a");
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
        Elements elements = doc.select("div#containerInner");
        for (Element element : elements) {
            Elements docSubElements = element.select("div.viewSubjectDiv div");
            for (Element docSubElement : docSubElements) {
                title = docSubElement.text().trim();
                if (title.length()>128) {
                    logger.error(" extract title length is long");
                    title="";
                }
                board.setTitle(title);
                break;
            }
            break;
        }

        /**
         * image
         */
        Elements imageElements = doc.select("div.contentContainer");
        for (Element element : imageElements) {
            Elements docSubElements = element.select("img");
            for (Element docSubElement : docSubElements) {
                image = docSubElement.attr("src").trim();
                if (image.contains("icon_count") || !image.contains("http://"))
                    continue;

                if (image.length()>128) {
                    logger.error(" extract image length is long");
                    image="";
                }
                board.setImageUrl(image);
                break;
            }
            break;
        }

//        /**
//         * date time
//         */
//        Elements dateElements = doc.select("div.writerInfoContainer");
//        for (Element element : dateElements) {
//            Elements docDateElements = element.select("div");
//            for (Element docDateElement : docDateElements) {
//                if (!docDateElement.outerHtml().contains("원글작성시간 :"))
//                    continue;
//
//                dateTime = docDateElement.text().trim().replace("원글작성시간 :","").
//                        replace(" ","").replace("/","").replace(":","");
//                if (dateTime.length()>100) {
//                    logger.error(" extract date time length is long");
//                    dateTime = "";
//                }
//                board.setDateTime(dateTime);
//                break;
//            }
//            break;
//        }

//        /**
//         * reply count
//         */
//        elements = doc.select("div.writerInfoContents");
//        for (Element element : elements) {
//            Elements docSubElements = element.select("div");
//            for (Element docSubElement : docSubElements) {
//                if (!docSubElement.outerHtml().contains("댓글 :"))
//                    continue;
//
//                String reply = docSubElement.text().trim().replace("댓글 :","").replace(" ","").replace("개","");
//                if (reply.length()>10) {
//                    logger.error(" extract reply count length is long");
//                } else {
//                    if (stdUtils.isNumeric(reply)) {
//                        board.setReplyCount(Integer.parseInt(reply));
//                    } else {
//                        logger.error(String.format(" reply count is not number [%s] ", reply));
//                    }
//                    break;
//                }
//                break;
//            }
//            break;
//        }
//
//        /**
//         * view count
//         */
//        elements = doc.select("div.wrap_info");
//        for (Element element : elements) {
//            Elements docSubElements = element.select("span.count em");
//            for (Element docSubElement : docSubElements) {
//                String view = docSubElement.text().trim();
//                if (view.length()>10) {
//                    logger.error(" extract view count length is long");
//                } else {
//                    if (stdUtils.isNumeric(view)) {
//                        board.setViewCount(Integer.parseInt(view));
//                    } else {
//                        logger.error(String.format(" view count is not number [%s] ", view));
//                    }
//                    break;
//                }
//                break;
//            }
//            break;
//        }
//
//        /**
//         * suggest count
//         */
//        elements = doc.select("div.likeButton");
//        for (Element element : elements) {
//            Elements docSubElements = element.select("span[style*=display:inline-block]");
//            for (Element docSubElement : docSubElements) {
//                String suggest = docSubElement.text().trim();
//                if (suggest.length()>10) {
//                    logger.error(" extract suggest count length is long");
//                } else {
//                    if (stdUtils.isNumeric(suggest)) {
//                        board.setSuggestCount(Integer.parseInt(suggest));
//                    } else {
//                        logger.error(String.format(" view suggest is not number [%s] ", suggest));
//                    }
//                    break;
//                }
//                break;
//            }
//            break;
//        }
//
//        /**
//         * writer
//         */
//        elements = doc.select("div.wrap_title");
//        for (Element element : elements) {
//            Elements docSubElements = element.select("p.name a");
//            for (Element docSubElement : docSubElements) {
//                String writer = docSubElement.text().trim();
//                if (writer.length()>100) {
//                    logger.error(" extract writer length is long");
//                } else {
//                    board.setWriter(writer);
//                }
//                break;
//            }
//            break;
//        }

        return board;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        StdFile stdFile = new StdFile();
        TodayHumourExtractorBest todayHumourExtractorBest = new TodayHumourExtractorBest();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/TodayHumourBest_667012738.html", "utf-8");
        sourceMap.put("data", body);
        todayHumourExtractorBest.extractList(sourceMap);
    }
}
