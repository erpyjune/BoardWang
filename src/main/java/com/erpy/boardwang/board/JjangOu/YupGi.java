package com.erpy.boardwang.board.JjangOu;

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
 * Created by oj.bae on 2016. 1. 30..
 */
public class YupGi extends Board {
    private static Logger logger = Logger.getLogger(YupGi.class.getName());
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
        Elements elements = doc.select("div#ckd_list_set");
        for (Element element : elements) {
            Elements docSubElements = element.select("div[id^=ckd_list0]");
            for (Element docSubElement : docSubElements) {
                /**
                 * cleate board instance.
                 */
                Board board = new Board();

                /**
                 * set cp name
                 */
                board.setCpName(sourceMap.get("cp"));

                /**
                 * link
                 */
                Elements docLinkElements = docSubElement.select("li.img_sum a");
                for (Element docLinkElement : docLinkElements) {
                    board.setUrl(docLinkElement.attr("href").replace("./view?db","http://fun.jjang0u.com/chalkadak/view?db"));
                    logger.info(" link : "+board.getUrl());
                    break;
                }

                /**
                 * 본문 내용에서 추가로 뽑을 데이터 가져온다.
                 * ********************************
                 */
                Thread.sleep(300);
                boardTemp = extractContent(crawlContent.execute(board.getUrl(), "utf-8"));

                /**
                 * title
                 */
                Elements docTitleElements = docSubElement.select("li.ckd_ttl a");
                for (Element docTitleElement : docTitleElements) {
                    if (boardTemp.getTitle().length()>0) {
                        // 본문 내용에서 추출한 것으로 대체
                        board.setTitle(boardTemp.getTitle());
                    } else {
                        board.setTitle(docTitleElement.text());
                    }
                    logger.info(" title : " + board.getTitle());
                    break;
                }

                /**
                 * image url
                 */
                Elements docThumbElements = docSubElement.select("li.img_sum a img");
                for (Element docThumbElement : docThumbElements) {
                    if (boardTemp.getThumbUrl().length()>0) {
                        board.setImageUrl(boardTemp.getImageUrl());
                    } else {
                        board.setImageUrl(docThumbElement.attr("src"));
                    }
                    logger.info(" image : "+board.getThumbUrl());
                    break;
                }

                /**
                 * view count
                 */
                Elements docViewCountElements = docSubElement.select("li.ckd_redit01 span.rdt02");
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

                /**
                 * suggest count
                 */
                Elements docSuggestCountElements = docSubElement.select("li.ckd_redit01 span.rdt03");
                for (Element docSuggestCountElement : docSuggestCountElements) {
                    temp = stdUtils.removeSpace(docSuggestCountElement.text()).replace("&nbsp;","").trim();
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
                Elements docReplyCountElements = docSubElement.select("li.ckd_ttl span.ckd_list_dut");
                for (Element docReplyCountElement : docReplyCountElements) {
                    temp = stdUtils.removeSpace(docReplyCountElement.text()).replace("[","").replace("]","").trim();
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
                Elements docDateTimeElements = docSubElement.select("li.ckd_redit02 span.rdt01");
                for (Element docDateTimeElement : docDateTimeElements) {
                    temp = docDateTimeElement.text().trim();
//                    logger.info(" dateTime:"+temp);

                    int index=0;
                    String token="";
                    StringBuffer sb = new StringBuffer(16);
                    StringTokenizer st = new StringTokenizer(temp,".");
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        if (index==0) {
                            sb.append("20").append(token);
                        } else if (index==1) {
                            sb.append(token);
                        } else if (index==2) {
                            sb.append(token);
                        }
//                        logger.info(String.format(" [%d] %s", index, token));
                        index++;
                    }

                    token = sb.toString();
                    if (token.length() == 8) {
                        board.setDateTime(sb.toString());
                        logger.info(" date time : " + board.getDateTime());
                    } else {
                        board.setDateTime("");
                        logger.error(String.format(" date time error [%s][%d]", token, token.length()));
                    }

                    break;
                }

                /**
                 * whiter
                 */
                Elements docWriterElements = docSubElement.select("li.ckd_redit02 span.rdt02");
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
        StdUtils stdUtils = new StdUtils();

        Board board = new Board();

        String title = stdUtils.getFieldData(body, "<meta property=\"og:title\" content=\"", "\" />");
        String image = stdUtils.getFieldData(body, "<meta property=\"og:image\" content=\"","\" />");

        board.setTitle(title);
        board.setImageUrl(image);

        return board;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        StdFile stdFile = new StdFile();
        YupGi yupGi = new YupGi();
        Map<String, String> sourceMap = new HashMap<String, String>();

        sourceMap.put("cp", "test");
        String body = stdFile.fileReadToString("/Users/oj.bae/Work/BoardWang/crawl_data/jjang0uYunYe_457420157.html", "utf-8");
        sourceMap.put("data", body);
        yupGi.extractList(sourceMap);
    }
}