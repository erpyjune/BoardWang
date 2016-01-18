package com.erpy.boardwang.define;

/**
 * Created by oj.bae on 2016. 1. 18..
 */
public class Define {
    private static final String saveDir="/Users/oj.bae/Work/BoardWang/crawl_data";
    private static final String seedFilePath="/Users/oj.bae/Documents/workspace-intellij/BoardWang/src/main/resources/seed.txt";


    public static String getSeedFilePath() {
        return seedFilePath;
    }

    public static String getSaveDir() {
        return saveDir;
    }
}
