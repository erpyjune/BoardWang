package com.erpy.boardwang.thumbnail;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.service.Service;
import com.erpyjune.StdUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oj.bae on 2016. 2. 9..
 */
public class MakeThumbnail {
    private static Logger logger = Logger.getLogger(MakeThumbnail.class.getName());

    private boolean checkAndMakeDir(String dirPath) throws Exception {
        logger.info(" checkDir [" + dirPath + "]");

        File f = new File(dirPath);
        if (!f.exists()) {
            try {
                if (f.mkdir()) {
                    logger.info(" success make directory [" + dirPath + "]");
                }
            } catch (Exception e) {
                logger.error(" make directory [" + dirPath + "]");
                return false;
            }
        }

        return true;
    }

    public static void main(String args[]) throws Exception {
        String thumbCpDir="";
        String imageCpDir="";
        String thumbDirPrefix = "/home/erpy/tomcat/webapps/boardwang_img/thumb";
        String imageDirPrefix = "/home/erpy/BoardWangWeb/image";
//        String thumbDirPrefix = "/Users/oj.bae/Work/BoardWang/thumb";
//        String imageDirPrefix = "/Users/oj.bae/Work/BoardWang/image";
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-context.xml");
        Service service = (Service) cxt.getBean("boardService");
        StdUtils stdUtils = new StdUtils();
        MakeThumbnail makeThumbnail = new MakeThumbnail();

        List<Board> listAll = service.selectServiceAllBoard();
        Iterator iter = listAll.iterator();
        while (iter.hasNext()) {

            Board board = (Board) iter.next();

            if (board.getImageUrl().trim().length()==0 || board.getThumbUrl().trim().length() > 0) {
                continue;
            }

            thumbCpDir = thumbDirPrefix + File.separator + board.getCpName();
            imageCpDir = imageDirPrefix + File.separator + board.getCpName();
            makeThumbnail.checkAndMakeDir(thumbCpDir);
            makeThumbnail.checkAndMakeDir(imageCpDir);

            String sourceImagePath = imageCpDir + File.separator + FilenameUtils.getName(board.getImageUrl());
            logger.info(" sourceImagePath [" + sourceImagePath + "]");
            String destThumbnailPath = thumbCpDir + File.separator + stdUtils.MD5(board.getImageUrl()) + "." + FilenameUtils.getExtension(board.getImageUrl());
            logger.info(" destThumbnailPath [" + destThumbnailPath + "]");

            try {
                stdUtils.saveImage(board.getImageUrl(), sourceImagePath);
                Thread.sleep(300);
            } catch (Exception e) {
                logger.error(" download image error [" + board.getImageUrl() + "]");
                continue;
            }

            try {
                stdUtils.makeThumbnailator(sourceImagePath, destThumbnailPath, 50, 50);
            } catch (Exception e) {
                logger.error(" make thumbnail error source [" + sourceImagePath + "]");
                logger.error(" make thumbnail error dest   [" + destThumbnailPath + "]");
                continue;
            }

            /**
             * update to db
             */
            board.setThumbUrl(FilenameUtils.getName(destThumbnailPath));
            service.updateServiceBoard(board);
        }

        logger.info(" Make thumbnails end !");
    }
}
