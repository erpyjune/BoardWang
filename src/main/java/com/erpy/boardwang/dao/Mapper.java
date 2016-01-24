package com.erpy.boardwang.dao;

import com.erpy.boardwang.Data.Board;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 24..
 */
public interface Mapper {
    @Select("SELECT id, title, writer, url, thumb_url, date, view_count, suggest_count, reply_count FROM board")
    public List<Board> selectAllBoard();

    @Select("SELECT id, title, writer, url, thumb_url, date, view_count, suggest_count, reply_count FROM board WHERE id=#{id}")
    public Board selectBoardId(@Param("id") int id);

    @Select("SELECT title, writer, url, thumb_url, date, view_count, suggest_count, reply_count FROM board WHERE url=#{url}")
    public Board selectBoardUrl(Board board);

    @Select("INSERT INTO board (title,    writer,    url,    thumb_url,   date,        view_count,   suggest_count,   reply_count) " +
            "VALUES (           #{title}, #{writer}, #{url}, #{thumbUrl}, #{dateTime}, #{viewCount}, #{suggestCount}, #{replyCount})")
    public void insertBoard(Board board);

}
