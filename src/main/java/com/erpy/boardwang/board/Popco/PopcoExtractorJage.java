package com.erpy.boardwang.board.Popco;

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
public class PopcoExtractorJage {
    private static Logger logger = Logger.getLogger(PopcoExtractorJage.class.getName());
    private static final String url = "http://fun.jjang0u.com/chalkadak/list?db=160";

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
        Board boardTemp = null;
        List<Board> list = new ArrayList<Board>();

        CrawlContent crawlContent = new CrawlContent();

        Document doc = Jsoup.parse(sourceMap.get("data"));
        Elements elements = doc.select("div#cont");
        for (Element element : elements) {
            Elements docSubElements = element.select("li");
            for (Element docSubElement : docSubElements) {

                if (!docSubElement.outerHtml().contains("communityView.php")) {
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
                board.setCpNameDisplay("팝코");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("a.link");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl(docLinkElement.attr("href").replace("./communityView.php","http://m.popco.net/communityView.php"));
                    logger.info(" link : "+board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(Define.getHttpDelayTime());
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8"));

                if (boardTemp.getImageUrl().length() > 128) {
                    logger.error(" long image url");
                }

                board.setTitle(boardTemp.getTitle().trim());
                board.setImageUrl(boardTemp.getImageUrl());
                board.setDateTime(boardTemp.getDateTime());
                board.setViewCount(boardTemp.getViewCount());
                board.setSuggestCount(boardTemp.getSuggestCount());
                board.setReplyCount(boardTemp.getReplyCount());

                logger.info(" title : " + board.getTitle());
                logger.info(" imgae : " + board.getImageUrl());
                logger.info(" date  : " + board.getDateTime());
                logger.info(" view count  : " + board.getViewCount());
                logger.info(" suggest count  : " + board.getSuggestCount());
                logger.info(" reply count  : " + board.getReplyCount());

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
//                Elements docViewCountElements = docSubElement.select("td.count");
//                for (Element docViewCountElement : docViewCountElements) {
//                    temp = docViewCountElement.text().trim();
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
//                Elements docSuggestCountElements = docSubElement.select("td.recomm");
//                for (Element docSuggestCountElement : docSuggestCountElements) {
//                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).trim();
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
//                Elements docReplyCountElements = docSubElement.select("td.pl14 span.Comment");
//                for (Element docReplyCountElement : docReplyCountElements) {
//                    temp = stdUtils.removeSpace(docReplyCountElement.text()).trim().replace("(","").replace(")","");
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
         * set doc
         */
        Document doc = Jsoup.parse(body);

        /**
         * image url
         */
        image = subExtractor.subExtract(body,"div.pin_image", "img", "src");
        if (image.length()>200) {
            logger.error(" extract image is long [" + image + "]");
            image="";
        }
        board.setImageUrl(image);

        /**
         * title
         */
        title = subExtractor.subExtract(body,"div.mem_thumb_tit", "h2", "text");
        if (title.length()>100) {
            logger.error(" extract title is long [" + title + "]");
            title="";
        }
        board.setTitle(title);

        /**
         * date time
         * view count
         * suggest count
         */
        date = stdUtils.getFieldData(body, "<p class=\"thumb_if\">", "</p>");
        if (date.trim().length()>0) {
            int startPos = date.indexOf("|");
            String t = date.substring(startPos+1, date.length());
            t = t.replace(" ","");
            int index=0;
            StringTokenizer st = new StringTokenizer(t, "|");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (index==0) {
                    s = "2016" + s.replace("-","").replace(":","").replace(" ","");
                    board.setDateTime(s);
                } else if (index==1) {
                    s = s.replace(" ","").replace("조회수:","").replace(",","");
                    if (stdUtils.isNumeric(s)) {
                        board.setViewCount(Integer.parseInt(s));
                    }
                } else if (index==2) {
                    s = s.replace(" ","").replace("추천:","").replace(",","");
                    if (stdUtils.isNumeric(s)) {
                        board.setSuggestCount(Integer.parseInt(s));
                    }
                }
                index++;
            }
        }

        /**
         * reply count
         */
        String reply = subExtractor.subExtract(body,"div.comm_view_btn", "span", "text");
        if (reply.length()>100) {
            logger.error(" extract reply count is long [" + reply + "]");
            reply="";
        }
        if (stdUtils.isNumeric(reply.trim().replace(",",""))) {
            board.setReplyCount(Integer.parseInt(reply));
        }

        return board;
    }


    void testExtract(String bodyFilePath) throws Exception {
        StdFile stdFile = new StdFile();
        PopcoExtractorJage cp = new PopcoExtractorJage();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString(bodyFilePath, "utf-8");
        sourceMap.put("data", body);
        cp.extractList(sourceMap);
    }


    void testExtractBody(String bodyUrl) throws Exception {
        CrawlContent crawlContent = new CrawlContent();
        PopcoExtractorJage cp = new PopcoExtractorJage();

        Board board = cp.extractContent(crawlContent.execute(bodyUrl, "utf-8"));

        System.out.println("title [" + board.getTitle() + "]");
        System.out.println("image [" + board.getImageUrl() + "]");
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        PopcoExtractorJage cp = new PopcoExtractorJage();
        cp.testExtract("/Users/oj.bae/Work/BoardWang/crawl_data/pomco.html");
//        cp.testExtractBody("http://www.bobaedream.co.kr/view?code=best&No=65629&vdate=");
    }
}
