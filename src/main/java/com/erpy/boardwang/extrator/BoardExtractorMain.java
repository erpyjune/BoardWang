package com.erpy.boardwang.extrator;

import com.erpy.boardwang.Data.Board;

import com.erpy.boardwang.board.Bobae.BobaeExtractorBest;
import com.erpy.boardwang.board.Bobae.BobaeExtractorHumour;
import com.erpy.boardwang.board.Clien.ClienExtractorNews;
import com.erpy.boardwang.board.Clien.ClienExtractorPark;
import com.erpy.boardwang.board.DocDripCom.DocDripExtractorDiGeJoA;
import com.erpy.boardwang.board.DocDripCom.DocDripExtractorHotDog;
import com.erpy.boardwang.board.DpgDripNet.DogDripExtractorDocDrip;
import com.erpy.boardwang.board.DpgDripNet.DpcDripExtractorUserDrip;
import com.erpy.boardwang.board.JjangOu.WootGin;
import com.erpy.boardwang.board.JjangOu.YupGi;
import com.erpy.boardwang.board.PullBbang.PullBbangExtractorGirlGroup;
import com.erpy.boardwang.board.PullBbang.PullBbangExtractorGukNaeYunYe;
import com.erpy.boardwang.board.PullBbang.PullBbangExtractorHumour;
import com.erpy.boardwang.board.PullBbang.PullBbangExtractorIssueSago;
import com.erpy.boardwang.board.WootDae.WootDaeExtractorWootDaeJaRyo;
import com.erpy.boardwang.board.WootDae.WootDaeExtractorWootGinHumour;
import com.erpy.boardwang.board.hungryapp.HungryAppExtractorLoving;
import com.erpy.boardwang.board.hungryapp.HungryAppExtractorSports;
import com.erpy.boardwang.board.hungryapp.HungryExtractorHorror;
import com.erpy.boardwang.board.hungryapp.HungryExtractorHumour;
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
public class BoardExtractorMain {
    private static Logger logger = Logger.getLogger(BoardExtractorMain.class.getName());

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

    public List<Board> extract(Service service, String crawlDataPath) throws Exception {
        String body;
        String filePath;
        StdFile stdFile = new StdFile();
        Map<String, String> sourceMap = new HashMap<String, String>();
        List<Board> allList = new ArrayList<Board>();
        List<Board> arrayList=null;
        ProcessDB processDB = new ProcessDB();

        // CP class
        YupGi yupGi = new YupGi();
        WootGin wootGin = new WootGin();
        HungryAppExtractorLoving hungryAppExtractorLoving = new HungryAppExtractorLoving();
        HungryAppExtractorSports hungryAppExtractorSports = new HungryAppExtractorSports();
        HungryExtractorHorror hungryExtractorHorror = new HungryExtractorHorror();
        HungryExtractorHumour hungryExtractorHumour = new HungryExtractorHumour();
        ClienExtractorPark clienExtractorPark = new ClienExtractorPark();
        ClienExtractorNews clienExtractorNews = new ClienExtractorNews();
        BobaeExtractorHumour bobaeExtractorHumour = new BobaeExtractorHumour();
        BobaeExtractorBest bobaeExtractorBest = new BobaeExtractorBest();
        // DocDrip.com
        DocDripExtractorDiGeJoA docDripExtractorDiGeJoA = new DocDripExtractorDiGeJoA();
        DocDripExtractorHotDog docDripExtractorHotDog = new DocDripExtractorHotDog();
        // DocDrip.net
        DogDripExtractorDocDrip dogDripExtractorDocDrip = new DogDripExtractorDocDrip();
        DpcDripExtractorUserDrip dpcDripExtractorUserDrip = new DpcDripExtractorUserDrip();
        // PullBbang
        PullBbangExtractorGirlGroup pullBbangExtractorGirlGroup = new PullBbangExtractorGirlGroup();
        PullBbangExtractorGukNaeYunYe pullBbangExtractorGukNaeYunYe = new PullBbangExtractorGukNaeYunYe();
        PullBbangExtractorHumour pullBbangExtractorHumour = new PullBbangExtractorHumour();
        PullBbangExtractorIssueSago pullBbangExtractorIssueSago = new PullBbangExtractorIssueSago();
        // WootDae
        WootDaeExtractorWootDaeJaRyo wootDaeExtractorWootDaeJaRyo = new WootDaeExtractorWootDaeJaRyo();
        WootDaeExtractorWootGinHumour wootDaeExtractorWootGinHumour = new WootDaeExtractorWootGinHumour();

        List<String> listFile =  stdFile.getFileListFromPath(crawlDataPath);
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
                arrayList = yupGi.extractList(sourceMap);
            } else if (getCpName(filePath).equals("jjang0uWoot")) {
                arrayList = wootGin.extractList(sourceMap);
            } else if (getCpName(filePath).equals("jjang0uJungchi")) {
                arrayList = wootGin.extractList(sourceMap);
            } else if (getCpName(filePath).equals("jjang0uYunYe")) {
                arrayList = yupGi.extractList(sourceMap);
            } else if (getCpName(filePath).equals("HungryAppHumourCatoon")) {
                arrayList = hungryExtractorHumour.extractList(sourceMap);
            } else if (getCpName(filePath).equals("HungryAppHorror")) {
                arrayList = hungryExtractorHorror.extractList(sourceMap);
            } else if (getCpName(filePath).equals("HungryAppLoving")) {
                arrayList = hungryAppExtractorLoving.extractList(sourceMap);
            } else if (getCpName(filePath).equals("HungryAppSports")) {
                arrayList = hungryAppExtractorSports.extractList(sourceMap);
            } else if (getCpName(filePath).equals("ClienPark")) {
                arrayList = clienExtractorPark.extractList(sourceMap);
            } else if (getCpName(filePath).equals("ClienNews")) {
                arrayList = clienExtractorNews.extractList(sourceMap);
            } else if (getCpName(filePath).equals("BobaeHumour")) {
                arrayList = bobaeExtractorHumour.extractList(sourceMap);
            } else if (getCpName(filePath).equals("BobeaBest")) {
                arrayList = bobaeExtractorBest.extractList(sourceMap);
            } else if (getCpName(filePath).equals("DocDripDocDrip")) {
                arrayList = dogDripExtractorDocDrip.extractList(sourceMap);
            } else if (getCpName(filePath).equals("DocDripUserDrip")) {
                arrayList = dpcDripExtractorUserDrip.extractList(sourceMap);
            } else if (getCpName(filePath).equals("DocDripHotDog")) {
                arrayList = docDripExtractorHotDog.extractList(sourceMap);
            } else if (getCpName(filePath).equals("DocDripDiGeJoA")) {
                arrayList = docDripExtractorDiGeJoA.extractList(sourceMap);
            } else if (getCpName(filePath).equals("PullBbangHumour")) {
                arrayList = pullBbangExtractorHumour.extractList(sourceMap);
            } else if (getCpName(filePath).equals("PullBbangGirlGroup")) {
                arrayList = pullBbangExtractorGirlGroup.extractList(sourceMap);
            } else if (getCpName(filePath).equals("PullBbangGukNaeYunYe")) {
                arrayList = pullBbangExtractorGukNaeYunYe.extractList(sourceMap);
            } else if (getCpName(filePath).equals("PullBbangIssueSago")) {
                arrayList = pullBbangExtractorIssueSago.extractList(sourceMap);
            } else if (getCpName(filePath).equals("WootGinJaRyo")) {
                arrayList = wootDaeExtractorWootDaeJaRyo.extractList(sourceMap);
            } else if (getCpName(filePath).equals("WootGinHumour")) {
                arrayList = wootDaeExtractorWootGinHumour.extractList(sourceMap);
            } else {
                logger.info(" 모르는 CP 입니다.");
            }

            /**
             * 추출된 데이터가 없을경우 사이트가 개편되었을 수 있으므로 error를 남겨서 체크를 한다
             */
            if (arrayList == null || arrayList.size() < 5) {
                logger.error(String.format(" [%s] cp is extract data size is null or small", getCpName(filePath)));
                continue;
            }

            /**
             * process database;
             */
            processDB.processingData(arrayList, service);

            sourceMap.clear();
        }

        return allList;
    }


    public static void main(String args[]) throws Exception {
        List<Board> list = null;
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-context.xml");
        Service service = (Service) cxt.getBean("boardService");

        /**
         * check argument
         */
        if (args.length != 1) {
            logger.error(" Argument error !!");
            logger.error(" (usage) [1]crawl_data_path");
            return;
        }

        logger.info(" Crawl data path [" + args[0] + "]");


        /**
         * get extrat & list
         * **************************************************
         */
        BoardExtractorMain boardExtractor = new BoardExtractorMain();
        list = boardExtractor.extract(service, args[0]);

    }
}

