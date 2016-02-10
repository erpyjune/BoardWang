package com.erpy.boardwang.define;

/**
 * Created by oj.bae on 2016. 1. 18..
 */
public class Define {
    private static final String saveDir="/Users/oj.bae/Work/BoardWang/crawl_data";
    private static final String seedFilePath="/Users/oj.bae/Documents/workspace-intellij/BoardWang/src/main/resources/seed.txt";

//    private static final String thumbDirPrefix = "/home/erpy/tomcat/webapps/boardwang_img/thumb";
//    private static final String imageDirPrefix = "/home/erpy/BoardWangWeb/image";
    private static final String thumbDirPrefix = "/Users/oj.bae/Work/BoardWang/thumb";
    private static final String imageDirPrefix = "/Users/oj.bae/Work/BoardWang/image";


    public static String getSeedFilePath() {
        return seedFilePath;
    }

    public static String getSaveDir() {
        return saveDir;
    }

    public static String getThumbDirPrefix() {
        return thumbDirPrefix;
    }

    public static String getImageDirPrefix() {
        return imageDirPrefix;
    }
}
