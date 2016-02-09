package com.erpy.boardwang.extrator;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.service.Service;
import com.erpy.boardwang.thumbnail.MakeThumbnail;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 31..
 */
public class ProcessDB {
    private static Logger logger = Logger.getLogger(ProcessDB.class.getName());

    private boolean validChecker(Board board) throws Exception {
        if (board.getTitle().trim().length()==0) {
            logger.error(" title is empty [" + board.getUrl()+"]");
            return false;
        }

//        if (board.getWriter().trim().length()==0) {
//            logger.error(" writer is empty [" + board.getUrl()+"]");
//            return false;
//        }

        if (board.getCpName().trim().length()==0) {
            logger.error(" cp name is empty [" + board.getUrl()+"]");
            return false;
        }

        if (board.getUrl().trim().length()==0) {
            logger.error(" url is empty [" + board.getUrl()+"]");
            return false;
        }

        return true;
    }

    public void processingData(List<Board> list, Service service) throws Exception {
        MakeThumbnail makeThumbnail = new MakeThumbnail();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Board dbBoard = null;
            Board board = (Board)iter.next();

            if (!validChecker(board)) {
                continue;
            }

            logger.info(" Processing [" + board.getUrl() + "]");

            /**
             * get db data from url
             */
            try {
                dbBoard = service.selectServiceBoardUrl(board);
            } catch (Exception e) {
                logger.error(String.format(" Select data is over 2 count, url[%s]", board.getUrl()));
                logger.error(Arrays.toString(e.getStackTrace()));
                continue;
            }

            /**
             * check same url && title
             */
            if (dbBoard != null) {
                if (board.getTitle().equals(dbBoard.getTitle()) &&
                        board.getUrl().equals(dbBoard.getUrl())) {

                    /**
                     * thumbnail checker && make thumbnail
                     */
                    if (board.getImageUrl().trim().length()>0) {
                        if (board.getThumbUrl().trim().length()==0) {
                            board.setThumbUrl(makeThumbnail.thumbnailProcess(board));
                        }
                    }

                    // update data
                    service.updateServiceBoard(board);
                    logger.info(" title : " + board.getTitle());
                    logger.info(String.format(" before viewCount[%d], replyCount[%d]", dbBoard.getViewCount(), dbBoard.getReplyCount()));
                    logger.info(String.format(" after  viewCount[%d], replyCount[%d]", board.getViewCount(), board.getReplyCount()));
                    logger.info("=====================================================================");
                    continue;
                }
            }

            /**
             * thumbnail checker && make thumbnail
             */
            if (board.getImageUrl().trim().length()>0) {
                if (board.getThumbUrl().trim().length()==0) {
                    board.setThumbUrl(makeThumbnail.thumbnailProcess(board));
                }
            }

            // insert to DB
            service.insertServiceBoard(board);
            logger.info(" insert : " + board.getTitle());
            logger.info("=====================================================================");
        }
    }
}
