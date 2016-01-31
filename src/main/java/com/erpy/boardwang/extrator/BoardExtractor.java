package com.erpy.boardwang.extrator;

import com.erpy.boardwang.Data.Board;

import com.erpy.boardwang.board.JjangOu.WootGin;
import com.erpy.boardwang.board.JjangOu.YupGi;
import com.erpy.boardwang.define.Define;
import com.erpy.boardwang.service.Service;
import com.erpyjune.StdFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by oj.bae on 2016. 1. 17..
 */
public class BoardExtractor {
    private static Logger logger = Logger.getLogger(BoardExtractor.class.getName());

    private String getCpName(String filePath) {
        int index=0;
        String cpName=null;
        String ext = FilenameUtils.getName(filePath);
        logger.info("ext:"+ext);
        StringTokenizer st = new StringTokenizer(ext, "_");
        while(st.hasMoreTokens()) {
            if (index==0) {
                cpName = st.nextToken();
            }
            index++;
            if (index>=1) break;
        }
        return cpName;
    }

    public List<Board> extract() throws Exception {
        String body;
        String filePath;
        StdFile stdFile = new StdFile();
        Map<String, String> sourceMap = new HashMap<String, String>();
        List<Board> allList = new ArrayList<Board>();

        // CP class
        YupGi yupGi = new YupGi();
        WootGin wootGin = new WootGin();

        List<String> listFile =  stdFile.getFileListFromPath(Define.getSaveDir());
        Iterator iter = listFile.iterator();
        while(iter.hasNext()) {
            filePath = (String)iter.next();
            logger.info(filePath);
            logger.info(getCpName(filePath));

            // set cp_name
            sourceMap.put("cp", getCpName(filePath));
            // get data
            body = stdFile.fileReadToString(filePath, "utf-8");
            // set data
            sourceMap.put("data",body);

            if (getCpName(filePath).equals("jjang0uYup")) {
                List<Board> arrayList = yupGi.extractList(sourceMap);
                allList.addAll(arrayList);
            } else if (getCpName(filePath).equals("jjang0uWoot")) {
                List<Board> arrayList = wootGin.extractList(sourceMap);
                allList.addAll(arrayList);
            } else if (getCpName(filePath).equals("jjang0uJungchi")) {
                List<Board> arrayList = wootGin.extractList(sourceMap);
                allList.addAll(arrayList);
            } else if (getCpName(filePath).equals("jjang0uYunYe")) {
                List<Board> arrayList = yupGi.extractList(sourceMap);
                allList.addAll(arrayList);
            } else {
                logger.info(" 모르는 CP 입니다.");
            }

            sourceMap.clear();
        }

        return allList;
    }

    public static void main(String args[]) throws Exception {
        List<Board> list = null;
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-context.xml");
        Service service = (Service) cxt.getBean("boardService");


        /**
         * get extrat & list
         * **************************************************
         */
        BoardExtractor boardExtractor = new BoardExtractor();
        list = boardExtractor.extract();


        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Board board = (Board)iter.next();
            Board dbBoard = service.selectServiceBoardUrl(board);

//            if (dbBoard!=null) {
//                logger.info("title:" + dbBoard.getTitle());
//                logger.info("url:" + dbBoard.getUrl());
//                logger.info("view count:" + dbBoard.getViewCount());
//                logger.info("reply count:" + dbBoard.getReplyCount());
//                logger.info("suggest count:" + dbBoard.getSuggestCount());
//                logger.info("=====================================================================");
//            }


            if (dbBoard != null) {
                if (board.getTitle().equals(dbBoard.getTitle()) &&
                        board.getUrl().equals(dbBoard.getUrl())) {

                    // update data
                    service.updateServiceBoard(board);
                    logger.info(" title : " + board.getTitle());
                    logger.info(String.format(" before viewCount[%d], replyCount[%d]", dbBoard.getViewCount(), dbBoard.getReplyCount()));
                    logger.info(String.format(" after  viewCount[%d], replyCount[%d]", board.getViewCount(), board.getReplyCount()));
                    logger.info("=====================================================================");
                    continue;
                }
            }

            // insert to DB
            service.insertServiceBoard(board);
            logger.info(" insert : " + board.getTitle());
            logger.info("=====================================================================");
        }
    }
}

