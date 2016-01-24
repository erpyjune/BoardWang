package com.erpy.boardwang.extrator;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.board.CPJjangOu;
import com.erpy.boardwang.define.Define;
import com.erpy.boardwang.service.Service;
import com.erpyjune.StdFile;
import com.erpyjune.StdUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

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
        List<Board> arrayList=null;
        CPJjangOu cpJjangOu = new CPJjangOu();

        List<String> listFile =  stdFile.getFileListFromPath(Define.getSaveDir());
        Iterator iter = listFile.iterator();
        while(iter.hasNext()) {
            filePath = (String)iter.next();
            logger.info(filePath);
            logger.info(getCpName(filePath));
            body = stdFile.fileReadToString(filePath, "utf-8");

            arrayList = cpJjangOu.extractList(body);
        }

        return arrayList;
    }

    public static void main(String args[]) throws Exception {
        List<Board> list = null;
        BoardExtractor boardExtractor = new BoardExtractor();
        logger.info("start");
        list = boardExtractor.extract();
        logger.info("end");

        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-context.xml");
        Service service = (Service) cxt.getBean("boardService");

        Board board=null;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            board = (Board)iter.next();
            logger.info(" insert : " + board.getTitle());
            service.insertServiceBoard(board);
        }
    }
}

