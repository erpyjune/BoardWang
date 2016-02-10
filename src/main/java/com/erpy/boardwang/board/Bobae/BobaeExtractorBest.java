package com.erpy.boardwang.board.Bobae;

import com.erpy.boardwang.Data.Board;
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
 * Created by oj.bae on 2016. 1. 31..
 */
public class BobaeExtractorBest {
    private static Logger logger = Logger.getLogger(BobaeExtractorBest.class.getName());
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
            Elements docSubElements = element.select("tr");
            for (Element docSubElement : docSubElements) {

                if (!docSubElement.outerHtml().contains("class=\"best\"") &&
                        !docSubElement.outerHtml().contains("itemtype=\"http://schema.org/Article\"")) {
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
                board.setCpNameDisplay("보배드림");

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("td a.bsubject");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl(docLinkElement.attr("href").replace("/view?code", "http://www.bobaedream.co.kr/view?code"));
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
                Elements docViewCountElements = docSubElement.select("td.count");
                for (Element docViewCountElement : docViewCountElements) {
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

                /**
                 * suggest count
                 */
                Elements docSuggestCountElements = docSubElement.select("td.recomm");
                for (Element docSuggestCountElement : docSuggestCountElements) {
                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).trim();
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
                Elements docReplyCountElements = docSubElement.select("td.pl14 span.Comment");
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

                /**
                 * whiter
                 */
                Elements docWriterElements = docSubElement.select("td.author02 span.author");
                for (Element docWriterElement : docWriterElements) {
                    board.setWriter(docWriterElement.text());
                    logger.info(" writer : " + board.getWriter());
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
        String image="";
        String title="";
        Board board = new Board();


        /**
         * set doc
         */
        Document doc = Jsoup.parse(body);

        /**
         * image url
         */
        Elements elements = doc.select("div.docuCont03");
        for (Element element : elements) {
            Elements docSubElements = element.select("div#print_area2 a img");
            for (Element docSubElement : docSubElements) {
                image = docSubElement.attr("src");
                if (image.length()>100) {
                    image="";
                }
                board.setImageUrl(image);
                break;
            }
            break;
        }

        /**
         * title
         */
        elements = doc.select("div.docuCont03");
        for (Element element : elements) {
            Elements docSubElements = element.select("dt");
            for (Element docSubElement : docSubElements) {
                title = docSubElement.attr("title");
                if (title.length()>100) {
                    title="";
                }
                board.setTitle(title);
                break;
            }
            break;
        }

        /**
         * date time
         */
        String date="";
        String time="";
        int index=0;
        elements = doc.select("div.docuCont03");
        for (Element element : elements) {
            Elements docSubElements = element.select("span.countGroup");
            for (Element docSubElement : docSubElements) {
                String tmp = docSubElement.text().trim();

                StringTokenizer stringTokenizer = new StringTokenizer(tmp,"|");
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken().trim();
                    if (index==2) {
                        date = token.substring(0,10).replace(".","");
                        time = token.substring(token.length()-5, token.length()).replace(":","");
                        break;
                    }
                    index++;
                }

                board.setDateTime(date+time);
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
        BobaeExtractorBest bobaeExtractorBest = new BobaeExtractorBest();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/BobeaBest_90151331.html", "utf-8");
        sourceMap.put("data", body);
        bobaeExtractorBest.extractList(sourceMap);
    }
}
