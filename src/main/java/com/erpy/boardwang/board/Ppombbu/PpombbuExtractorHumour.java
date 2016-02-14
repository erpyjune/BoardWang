package com.erpy.boardwang.board.Ppombbu;

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

import java.util.*;

/**
 * Created by oj.bae on 2016. 2. 14..
 */
public class PpombbuExtractorHumour {
    private static Logger logger = Logger.getLogger(PpombbuExtractorHumour.class.getName());
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
        Elements elements = doc.select("div.bbs");
        for (Element element : elements) {
            Elements docSubElements = element.select("ul.bbsList li");
            for (Element docSubElement : docSubElements) {

                /**
                 * cleate board instance.
                 */
                Board board = new Board();

                /**
                 * set cp name
                 */
                board.setCpName(sourceMap.get("cp"));
                board.setCpNameDisplay("뽐뿌");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl("http://m.ppomppu.co.kr/new/" + docLinkElement.attr("href"));
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

//                board.setTitle(boardTemp.getTitle().trim());
                board.setImageUrl(boardTemp.getImageUrl());

//                logger.info(" title : " + board.getTitle());
                logger.info(" imgae : " + board.getImageUrl());

                /**
                 * title
                 */
                Elements docTitleElements = docSubElement.select("a strong");
                for (Element docTitleElement : docTitleElements) {
                    board.setTitle(docTitleElement.text());
                    logger.info(" title : " + board.getTitle());
                    break;
                }

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
                Elements docViewCountElements = docSubElement.select("span.hi");
                for (Element docViewCountElement : docViewCountElements) {
                    String view = docViewCountElement.text().trim().replace(",","").replace(" ","").replace("[","").replace("]","");
                    if (view.trim().length()>3) {
                        StringTokenizer st = new StringTokenizer(view, "/");
                        while (st.hasMoreTokens()) {
                            temp = st.nextToken();
                            index++;
                            if (index>0) {
                                break;
                            }
                        }
                    }
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
                Elements docSuggestCountElements = docSubElement.select("span.hi");
                for (Element docSuggestCountElement : docSuggestCountElements) {
                    String suggest = docSuggestCountElement.text().trim().replace(",","").replace(" ","").replace("[","").replace("]","");
                    if (suggest.trim().length()>3) {
                        StringTokenizer st = new StringTokenizer(suggest, "/");
                        while (st.hasMoreTokens()) {
                            temp = st.nextToken();
                            index++;
                            if (index>1) {
                                break;
                            }
                        }
                    }
                    if (stdUtils.isNumeric(temp)) {
                        board.setSuggestCount(Integer.parseInt(temp));
                        logger.info("suggest count : " + board.getSuggestCount());
                    } else {
                        logger.error(String.format(" suggest count is not number [%s] ", temp));
                        board.setSuggestCount(0);
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
                 * reply count
                 */
                Elements docReplyCountElements = docSubElement.select("span.rp");
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
                 * date time
                 *
                 */
                Elements docDateTimeElements = docSubElement.select("span.b");
                for (Element docDateTimeElement : docDateTimeElements) {
                    temp = docDateTimeElement.text();
                    if (temp.contains(":")) {
                        String currDate = stdUtils.getCurrDate();
                        temp = currDate + temp.trim().replace(":","").substring(0,4);
                    } else {
                        temp = stdUtils.getMinutesBeforeAfter(0).substring(0, 12);
                    }

                    board.setDateTime(temp);
                    logger.info(" dateTime : " + board.getDateTime());
                    break;
                }

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
        image = subExtractor.subExtract(body,"div#KH_Content", "img", "src");
        if (image.length()>200) {
            logger.error(" extract image is long [" + image + "]");
            image="";
        }
        board.setImageUrl(image);

        return board;
    }

    void testExtract(String bodyFilePath) throws Exception {
        StdFile stdFile = new StdFile();
        PpombbuExtractorHumour ppombbuExtractorHumour = new PpombbuExtractorHumour();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString(bodyFilePath, "utf-8");
        sourceMap.put("data", body);
        ppombbuExtractorHumour.extractList(sourceMap);
    }

    void testExtractBody(String bodyUrl) throws Exception {
        CrawlContent crawlContent = new CrawlContent();
        PpombbuExtractorHumour ppombbuExtractorHumour = new PpombbuExtractorHumour();

        Board board = ppombbuExtractorHumour.extractContent(crawlContent.execute(bodyUrl, "utf-8"));

        System.out.println("title [" + board.getTitle() + "]");
        System.out.println("image [" + board.getImageUrl() + "]");
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        PpombbuExtractorHumour ppombbuExtractorHumour = new PpombbuExtractorHumour();
        ppombbuExtractorHumour.testExtract("/Users/oj.bae/Work/BoardWang/crawl_data/PpombbuHumour_126305483.html");
//        appZzangExtractorJayuGesipan.testExtractBody("http://www.bobaedream.co.kr/view?code=best&No=65629&vdate=");
    }
}
