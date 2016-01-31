package com.erpy.boardwang.dao;

import com.erpy.boardwang.Data.Board;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 24..
 */
public interface Mapper {

    @Select("SELECT id, title, writer, url, thumb_url, image_url, cp_name, date, view_count, suggest_count, reply_count FROM board")
    @Results(value = {
            @Result(property = "id",           column = "id"),
            @Result(property = "title",        column = "title"),
            @Result(property = "writer",       column = "writer"),
            @Result(property = "url",          column = "url"),
            @Result(property = "thumbUrl",     column = "thumb_url"),
            @Result(property = "imageUrl",     column = "image_url"),
            @Result(property = "cpName",       column = "cp_name"),
            @Result(property = "dateTime",     column = "date"),
            @Result(property = "viewCount",    column = "view_count"),
            @Result(property = "suggestCount", column = "suggest_count"),
            @Result(property = "replyCount",   column = "reply_count")
    })
    List<Board> selectAllBoard();

    @Select("SELECT id, title, writer, url, thumb_url, image_url, cp_name, date, view_count, suggest_count, reply_count FROM board WHERE id=#{id}")
    @Results(value = {
            @Result(property = "id",           column = "id"),
            @Result(property = "title",        column = "title"),
            @Result(property = "writer",       column = "writer"),
            @Result(property = "url",          column = "url"),
            @Result(property = "thumbUrl",     column = "thumb_url"),
            @Result(property = "imageUrl",     column = "image_url"),
            @Result(property = "cpName",       column = "cp_name"),
            @Result(property = "dateTime",     column = "date"),
            @Result(property = "viewCount",    column = "view_count"),
            @Result(property = "suggestCount", column = "suggest_count"),
            @Result(property = "replyCount",   column = "reply_count")
    })
    Board selectBoardId(@Param("id") int id);

    @Select("SELECT title, writer, url, thumb_url, image_url, cp_name, date, view_count, suggest_count, reply_count FROM board WHERE url=#{url}")
    @Results(value = {
            @Result(property = "title",        column = "title"),
            @Result(property = "writer",       column = "writer"),
            @Result(property = "url",          column = "url"),
            @Result(property = "thumbUrl",     column = "thumb_url"),
            @Result(property = "imageUrl",     column = "image_url"),
            @Result(property = "cpName",       column = "cp_name"),
            @Result(property = "dateTime",     column = "date"),
            @Result(property = "viewCount",    column = "view_count"),
            @Result(property = "suggestCount", column = "suggest_count"),
            @Result(property = "replyCount",   column = "reply_count")
    })
    Board selectBoardUrl(Board board);

    @Select("INSERT INTO board (title,    writer,    url,    thumb_url,   image_url,   cp_name,   date,        view_count,   suggest_count,   reply_count) " +
            "VALUES (           #{title}, #{writer}, #{url}, #{thumbUrl}, #{imageUrl}, #{cpName}, #{dateTime}, #{viewCount}, #{suggestCount}, #{replyCount})")
    void insertBoard(Board board);

    @Select("UPDATE board SET title=#{title}, writer=#{writer}, url=#{url}, thumb_url=#{thumbUrl}, image_url=#{imageUrl}, cp_name=#{cpName}, " +
            "date=#{dateTime}, view_count=#{viewCount}, suggest_count=#{suggestCount}, reply_count=#{replyCount} " +
            "WHERE url=#{url}")
    void updateBoard(Board board);
}
