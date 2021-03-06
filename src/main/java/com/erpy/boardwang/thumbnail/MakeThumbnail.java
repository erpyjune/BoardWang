package com.erpy.boardwang.thumbnail;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.define.Define;
import com.erpy.boardwang.service.Service;
import com.erpyjune.StdUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by oj.bae on 2016. 2. 9..
 */
public class MakeThumbnail {
    private static Logger logger = Logger.getLogger(MakeThumbnail.class.getName());

    /**
     *
     * @param dirPath
     * @return
     * @throws Exception
     */
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

    /**
     *
     * @param board
     * @return
     * @throws Exception
     */
    public String thumbnailProcess(Board board, Map<String,String> requestHeader) throws Exception {
        String thumbCpDir="";
        String imageCpDir="";
        StdUtils stdUtils = new StdUtils();
        MakeThumbnail makeThumbnail = new MakeThumbnail();


        thumbCpDir = Define.getThumbDirPrefix() + File.separator + board.getCpName();
        imageCpDir = Define.getImageDirPrefix() + File.separator + board.getCpName();
        checkAndMakeDir(thumbCpDir);
        checkAndMakeDir(imageCpDir);

        String sourceImagePath = imageCpDir + File.separator + FilenameUtils.getName(board.getImageUrl());
        logger.info(" sourceImagePath [" + sourceImagePath + "]");

        String destThumbnailPath;
        if (FilenameUtils.getExtension(board.getImageUrl()).trim().length()>0) {
            destThumbnailPath = thumbCpDir + File.separator + stdUtils.MD5(board.getImageUrl()) + "." + FilenameUtils.getExtension(board.getImageUrl());
        } else {
            destThumbnailPath = thumbCpDir + File.separator + stdUtils.MD5(board.getImageUrl()) + ".jpg";
        }

        logger.info(" destThumbnailPath [" + destThumbnailPath + "]");

        try {
            stdUtils.saveImage(board.getImageUrl(), sourceImagePath, requestHeader);
            Thread.sleep(300);
        } catch (Exception e) {
            logger.error(" download image error [" + board.getImageUrl() + "]");
            return "";
        }

        try {
            stdUtils.makeThumbnailator(sourceImagePath, destThumbnailPath, 50, 50);
        } catch (Exception e) {
            logger.error(" make thumbnail error source [" + sourceImagePath + "]");
            logger.error(" make thumbnail error dest   [" + destThumbnailPath + "]");
            return "";
        }

        return FilenameUtils.getName(destThumbnailPath);
    }

    private void testMakethumbnail() throws Exception {
        String thumbCpDir="";
        String imageCpDir="";
//        String thumbDirPrefix = "/home/erpy/tomcat/webapps/boardwang_img/thumb";
//        String imageDirPrefix = "/home/erpy/BoardWangWeb/image";
        String thumbDirPrefix = "/Users/oj.bae/Work/BoardWang/thumb";
        String imageDirPrefix = "/Users/oj.bae/Work/BoardWang/image";
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-context.xml");
        Service service = (Service) cxt.getBean("boardService");
        StdUtils stdUtils = new StdUtils();
        MakeThumbnail makeThumbnail = new MakeThumbnail();
        Map<String,String> requestHeader = new HashMap<String, String>();

        // request header
        requestHeader.put("Referer","http://m.ppomppu.co.kr/new/bbs_list.php?id=humor");

        List<Board> listAll = service.selectServiceAllBoard();
        Iterator iter = listAll.iterator();
        while (iter.hasNext()) {

            Board board = (Board) iter.next();

            if (board.getImageUrl().trim().length()==0 || board.getThumbUrl().trim().length() > 0) {
                continue;
            }

            thumbCpDir = Define.getThumbDirPrefix() + File.separator + board.getCpName();
            imageCpDir = Define.getThumbDirPrefix() + File.separator + board.getCpName();
            checkAndMakeDir(thumbCpDir);
            checkAndMakeDir(imageCpDir);

            String sourceImagePath = imageCpDir + File.separator + FilenameUtils.getName(board.getImageUrl());
            logger.info(" sourceImagePath [" + sourceImagePath + "]");

            String destThumbnailPath;
            if (FilenameUtils.getExtension(board.getImageUrl()).trim().length()>0) {
                destThumbnailPath = thumbCpDir + File.separator + stdUtils.MD5(board.getImageUrl()) + "." + FilenameUtils.getExtension(board.getImageUrl());
            } else {
                destThumbnailPath = thumbCpDir + File.separator + stdUtils.MD5(board.getImageUrl()) + ".jpg";
            }

            logger.info(" destThumbnailPath [" + destThumbnailPath + "]");

            try {
                stdUtils.saveImage(board.getImageUrl(), sourceImagePath, requestHeader);
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


    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        MakeThumbnail makeThumbnail = new MakeThumbnail();
        StdUtils stdUtils = new StdUtils();
        Map<String, String> header = new HashMap<String, String>();

        header.put("Referer","http://m.ppomppu.co.kr/new/bbs_list.php?id=humor");

        String imageSourceUrl = "http://cache1.ppomppu.co.kr/zboard/data3/2016/0220/m_1455946811_UeFzD6jmp8.jpg";
        String imageSavePath = "/Users/oj.bae/Work/test/ppombbu.jpg";
        stdUtils.saveImage(imageSourceUrl, imageSavePath, header);

//        makeThumbnail.testMakethumbnail();
    }
}
