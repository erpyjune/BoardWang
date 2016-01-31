package com.erpy.boardwang.service;

import com.erpy.boardwang.Data.Board;
import com.erpy.boardwang.dao.Mapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 24..
 */
public class Service {
    private static Logger logger = Logger.getLogger(Service.class.getName());

    @Autowired
    private Mapper mapper;

    public List<Board> selectServiceAllBoard() {
        return mapper.selectAllBoard();
    }
    public Board selectServiceBoardId(int id) {
        return mapper.selectBoardId(id);
    }
    public Board selectServiceBoardUrl(Board board) {return mapper.selectBoardUrl(board);}
    public void insertServiceBoard(Board board) {
        mapper.insertBoard(board);
    }
    public void updateServiceBoard(Board borad) {
        mapper.updateBoard(borad);
    }

    ///////////////////////////////////////////////////////////////
    // test
    public static void main(String args[]) throws Exception {
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-context.xml");
        Service service = (Service) cxt.getBean("boardService");

        Board searchBorad = new Board();
        searchBorad.setUrl("http://fun.jjang0u.com/chalkadak/view?db=160&no=245278");
        Board board = service.selectServiceBoardUrl(searchBorad);

        logger.info(" title : " + board.getTitle());
        logger.info(" url : " + board.getUrl());
    }
}
