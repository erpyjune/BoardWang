package com.erpy.boardwang.board.PullBbang;

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
 * Created by oj.bae on 2016. 2. 6..
 */
public class PullBbangExtractorGirlGroup {
    private static Logger logger = Logger.getLogger(PullBbangExtractorGirlGroup.class.getName());
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
        Elements elements = doc.select("div#textList");
        for (Element element : elements) {
            Elements docSubElements = element.select("tr");
            for (Element docSubElement : docSubElements) {
                if (!docSubElement.outerHtml().contains("javascript:goPage")) {
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
                board.setCpNameDisplay("풀빵닷컴");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td.title a");
                for (Element docLinkElement : docLinkElements) {
                    temp = docLinkElement.attr("href");
                    String id = stdUtils.getFieldData(temp, "goPage('", "');");
                    board.setUrl(String.format("http://fun.pullbbang.com/fun/funView.pull?category2_code=1&category3_code=0&order_type=new&list_type=text&pageNum=1&key_select=title&code=%s", id.trim()));
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
                board.setWriter(boardTemp.getWriter());

                logger.info(" title : " + board.getTitle());
                logger.info(" imgae : " + board.getImageUrl());
                logger.info(" date : " + board.getDateTime());
                logger.info(" writer : " + board.getWriter());

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
                Elements docViewCountElements = docSubElement.select("td span[style*=color:black]");
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
                Elements docSuggestCountElements = docSubElement.select("td span[style*=color:red]");
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
//
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
//
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
        StdUtils stdUtils = new StdUtils();
        Board board = new Board();

        /**
         * set data
         */
        Document doc = Jsoup.parse(body);

        /**
         * title
         */
        Elements elements = doc.select("div.fun_list_contentsL_box");
        for (Element element : elements) {
            // title
            Elements docSubElements = element.select("div.fun_view_title");
            for (Element docSubElement : docSubElements) {
                String title = docSubElement.text();
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
        Elements imageElements = doc.select("div.fun_list_contents_box");
        for (Element element : imageElements) {
            Elements docImageElements = element.select("div#articles img");
            for (Element docImageElement : docImageElements) {
                String image = docImageElement.attr("src");
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
         * date
         */
        Elements dateElements = doc.select("div.fun_list_contents_box");
        for (Element element : dateElements) {
            Elements docDateElements = element.select("div.fun_article_info li[class*=f11]");
            for (Element docDateElement : docDateElements) {
                String s = docDateElement.text();
                String date = stdUtils.getFieldData(s, "[", "]").replace("/","");
                if (date.length()>100) {
                    logger.error(" extract date length is long");
                    date = "";
                }
                board.setDateTime(date);
                break;
            }
            break;
        }

        /**
         * writer
         */
        Elements writerElements = doc.select("div.fun_list_contents_box");
        for (Element element : writerElements) {
            Elements docWriterElements = element.select("strong.f14");
            for (Element docWriterElement : docWriterElements) {
                String writer = docWriterElement.text();
                if (writer.length()> 100) {
                    logger.error(" extract writer length is long");
                    writer = "";
                }
                board.setWriter(writer);
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
        PullBbangExtractorGirlGroup pullBbangExtractorGirlGroup = new PullBbangExtractorGirlGroup();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/PullBbangGirlGroup_870544888.html", "utf-8");
        sourceMap.put("data", body);
        pullBbangExtractorGirlGroup.extractList(sourceMap);
    }
}
