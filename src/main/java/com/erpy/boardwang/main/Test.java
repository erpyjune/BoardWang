package com.erpy.boardwang.main;

import com.erpyjune.StdUtils;

/**
 * Created by oj.bae on 2016. 2. 9..
 */
public class Test {
    public static void main(String args[]) throws Exception {
        StdUtils stdUtils = new StdUtils();

        for(int i=0; i<50; i++) {
            System.out.println(stdUtils.getRandomNumber(60));
        }
    }
}
