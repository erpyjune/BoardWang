package com.erpy.boardwang.board;

import com.erpy.boardwang.Data.Board;

/**
 * Created by oj.bae on 2016. 1. 17..
 */
public class CPJjangOu extends Board {
    private static final String url = "http://fun.jjang0u.com/chalkadak/list?db=160";
    private static final String encode = "utf-8";
    private String orgData = "";

    public static String getUrl() {
        return url;
    }
}
