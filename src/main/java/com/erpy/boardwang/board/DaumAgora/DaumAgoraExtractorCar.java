package com.erpy.boardwang.board.DaumAgora;

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
 * Created by oj.bae on 2016. 2. 10..
 */
public class DaumAgoraExtractorCar {
    private static Logger logger = Logger.getLogger(DaumAgoraExtractorCar.class.getName());
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
        Elements elements = doc.select("div.wrap_photo_list");
        for (Element element : elements) {
            Elements docSubElements = element.select("li");
            for (Element docSubElement : docSubElements) {
                if (!docSubElement.outerHtml().contains("count_read")) {
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
                board.setCpNameDisplay("다음아고라");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("a.thumb");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl("http://bbs2.agora.media.daum.net/gaia/do/kin/" + docLinkElement.attr("href"));
                    logger.info(" link : " + board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(300);
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8"));

                board.setTitle(boardTemp.getTitle().trim());
                board.setImageUrl(boardTemp.getImageUrl());
                board.setDateTime(boardTemp.getDateTime());
                board.setReplyCount(boardTemp.getReplyCount());
                board.setViewCount(boardTemp.getViewCount());
                board.setSuggestCount(boardTemp.getSuggestCount());
                board.setWriter(boardTemp.getWriter());

                logger.info(" title : " + board.getTitle());
                logger.info(" imgae : " + board.getImageUrl());
                logger.info(" date  : " + board.getDateTime());
                logger.info(" reply  : " + board.getReplyCount());
                logger.info(" view  : " + board.getViewCount());
                logger.info(" suggest  : " + board.getSuggestCount());
                logger.info(" writer  : " + board.getWriter());

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

//                /**
//                 * view count
//                 */
//                Elements docViewCountElements = docSubElement.select("td.li_und");
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
//                Elements docSuggestCountElements = docSubElement.select("span.o");
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
//                Elements docReplyCountElements = docSubElement.select("span.list_comment_num");
//                for (Element docReplyCountElement : docReplyCountElements) {
//                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim().replace("[","").replace("]","");
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

//                /**
//                 * whiter
//                 */
//                Elements docWriterElements = docSubElement.select("span.hu_nick_txt");
//                for (Element docWriterElement : docWriterElements) {
//                    board.setWriter(docWriterElement.text());
//                    logger.info(" writer : "+board.getWriter());
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
        Elements elements = doc.select("div.wrap_title");
        for (Element element : elements) {
            // title
            Elements docSubElements = element.select("p.title");
            for (Element docSubElement : docSubElements) {
                title = docSubElement.attr("title").trim();
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
         * image url
         */
        image = stdUtils.getFieldData(body,"<meta property=\"og:image\" content=\"", "\" />");
        board.setImageUrl(image.trim());


        /**
         * date time
         */
        Elements dateElements = doc.select("div.wrap_info");
        for (Element element : dateElements) {
            Elements docDateElements = element.select("span.date");
            for (Element docDateElement : docDateElements) {
                dateTime = "20" + docDateElement.text().trim().
                        replace(" ","").replace(".","").replace(":","");
                if (dateTime.length()>100) {
                    logger.error(" extract date time length is long");
                    dateTime = "";
                }
                board.setDateTime(dateTime);
                break;
            }
            break;
        }

        /**
         * reply count
         */
        elements = doc.select("div.wrap_title");
        for (Element element : elements) {
            Elements docSubElements = element.select("span#sub_cmt");
            for (Element docSubElement : docSubElements) {
                String reply = docSubElement.text().trim();
                if (reply.length()>10) {
                    logger.error(" extract reply count length is long");
                } else {
                    if (stdUtils.isNumeric(reply)) {
                        board.setReplyCount(Integer.parseInt(reply));
                    } else {
                        logger.error(String.format(" reply count is not number [%s] ", reply));
                    }
                    break;
                }
                break;
            }
            break;
        }

        /**
         * view count
         */
        elements = doc.select("div.wrap_info");
        for (Element element : elements) {
            Elements docSubElements = element.select("span.count em");
            for (Element docSubElement : docSubElements) {
                String view = docSubElement.text().trim();
                if (view.length()>10) {
                    logger.error(" extract view count length is long");
                } else {
                    if (stdUtils.isNumeric(view)) {
                        board.setViewCount(Integer.parseInt(view));
                    } else {
                        logger.error(String.format(" view count is not number [%s] ", view));
                    }
                    break;
                }
                break;
            }
            break;
        }

        /**
         * suggest count
         */
        elements = doc.select("div.likeButton");
        for (Element element : elements) {
            Elements docSubElements = element.select("span[style*=display:inline-block]");
            for (Element docSubElement : docSubElements) {
                String suggest = docSubElement.text().trim();
                if (suggest.length()>10) {
                    logger.error(" extract suggest count length is long");
                } else {
                    if (stdUtils.isNumeric(suggest)) {
                        board.setSuggestCount(Integer.parseInt(suggest));
                    } else {
                        logger.error(String.format(" view suggest is not number [%s] ", suggest));
                    }
                    break;
                }
                break;
            }
            break;
        }

        /**
         * writer
         */
        elements = doc.select("div.wrap_title");
        for (Element element : elements) {
            Elements docSubElements = element.select("p.name a");
            for (Element docSubElement : docSubElements) {
                String writer = docSubElement.text().trim();
                if (writer.length()>100) {
                    logger.error(" extract writer length is long");
                } else {
                        board.setWriter(writer);
                }
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
        DaumAgoraExtractorCar daumAgoraExtractorCar = new DaumAgoraExtractorCar();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/DaumAgoraCar_768963312.html", "utf-8");
        sourceMap.put("data", body);
        daumAgoraExtractorCar.extractList(sourceMap);
    }
}
