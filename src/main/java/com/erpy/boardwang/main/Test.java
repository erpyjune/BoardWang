package com.erpy.boardwang.main;

import com.erpyjune.StdUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by oj.bae on 2016. 2. 9..
 */
public class Test {
    public static void main(String args[]) throws Exception {
        String url="http://i1.media.daumcdn.net/uf/image/U01/agora/56BAF37B4F4B730037";
        System.out.println("getName[" + FilenameUtils.getName(url)+"]");
        System.out.println("getExt[" + FilenameUtils.getExtension(url)+"]");
    }
}
