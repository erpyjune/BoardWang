package com.erpy.boardwang.extrator;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.define.Define;
import com.erpyjune.StdFile;
import com.erpyjune.StdUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 17..
 */
public class BoardExtractor {
    private static Logger logger = Logger.getLogger(BoardExtractor.class.getName());
    public List<Board> extract() throws Exception {
        String filePath;
        StdFile stdFile = new StdFile();
        ArrayList<Board> arrayList = new ArrayList<Board>();

        List<String> listFile =  stdFile.getFileListFromPath(Define.getSaveDir());
        Iterator iter = listFile.iterator();
        while(iter.hasNext()) {
            filePath = (String)iter.next();
            logger.info(filePath);
//            String body = stdFile.fileReadToString(filePath, "utf-8");
//            logger.info(body);
        }

        return arrayList;
    }

    public static void main(String args[]) throws Exception {
        BoardExtractor boardExtractor = new BoardExtractor();
        logger.info("start");
        boardExtractor.extract();
        logger.info("end");
    }
}
