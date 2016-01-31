package com.erpy.boardwang.extrator;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.service.Service;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 31..
 */
public class ProcessDB {
    private static Logger logger = Logger.getLogger(ProcessDB.class.getName());

    public void processingData(List<Board> list, Service service) throws Exception {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Board board = (Board)iter.next();
            Board dbBoard = service.selectServiceBoardUrl(board);

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
