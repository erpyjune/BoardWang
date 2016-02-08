#!/bin/bash

rm -f /Users/oj.bae/Work/BoardWang/crawl_data/*
java -cp copywangbatch-jar-with-dependencies.jar com.erpy.boardwang.main.CrawlBoard
java -cp copywangbatch-jar-with-dependencies.jar com.erpy.boardwang.extrator.BoardExtractorMain
