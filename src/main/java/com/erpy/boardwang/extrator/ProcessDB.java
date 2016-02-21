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
import java.util.Map;

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

    public void processingData(List<Board> list, Service service, Map<String,String> requestHeader) throws Exception {
        MakeThumbnail makeThumbnail = new MakeThumbnail();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Board dbBoard = null;
            Board newBoard = (Board)iter.next();

            /**
             * data delete check && delete
             */
            if (newBoard.isDeleted()) {
                service.deleteServiceBoard(newBoard);
                logger.info(" delete board data [" + newBoard.getUrl() + "]");
                return;
            }

            /**
             * data valid check
             */
            if (!validChecker(newBoard)) {
                continue;
            }

            logger.info(" Processing [" + newBoard.getUrl() + "]");

            /**
             * get db data from url
             */
            try {
                dbBoard = service.selectServiceBoardUrl(newBoard);
            } catch (Exception e) {
                logger.error(String.format(" Select data is over 2 count, url[%s]", newBoard.getUrl()));
                logger.error(Arrays.toString(e.getStackTrace()));
                continue;
            }

            /**
             * check same url && title
             */
            if (dbBoard != null) {
                if (newBoard.getTitle().equals(dbBoard.getTitle()) && newBoard.getUrl().equals(dbBoard.getUrl())) {

                    dbBoard.setReplyCount(newBoard.getReplyCount());
                    dbBoard.setSuggestCount(newBoard.getSuggestCount());
                    dbBoard.setViewCount(newBoard.getViewCount());

                    /**
                     * thumbnail checker && make thumbnail
                     */
                    if (dbBoard.getThumbUrl().trim().length()==0 && newBoard.getImageUrl().trim().length() > 0) {
                        dbBoard.setThumbUrl(makeThumbnail.thumbnailProcess(newBoard, requestHeader));
                    }

                    // update data
                    service.updateServiceBoardCount(dbBoard);
                    logger.info(" title : " + dbBoard.getTitle());
                    logger.info(String.format(" update viewCount[%d], replyCount[%d]", dbBoard.getViewCount(), dbBoard.getReplyCount()));
                    logger.info("=====================================================================");
                    continue;
                }
            }

            /**
             * thumbnail checker && make thumbnail
             */
            if (newBoard.getImageUrl().trim().length()>0) {
                if (newBoard.getThumbUrl().trim().length()==0) {
                    newBoard.setThumbUrl(makeThumbnail.thumbnailProcess(newBoard, requestHeader));
                }
            }

            // insert to DB
            service.insertServiceBoard(newBoard);
            logger.info(" insert : " + newBoard.getTitle());
            logger.info("=====================================================================");

        }
    }
}
