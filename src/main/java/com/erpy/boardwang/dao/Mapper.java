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
    /**
     *
     * @return
     */
    @Select("SELECT id, title, writer, url, thumb_url, image_url, cp_name, cp_name_display, date, view_count, suggest_count, reply_count FROM board")
    @Results(value = {
            @Result(property = "id",           column = "id"),
            @Result(property = "title",        column = "title"),
            @Result(property = "writer",       column = "writer"),
            @Result(property = "url",          column = "url"),
            @Result(property = "thumbUrl",     column = "thumb_url"),
            @Result(property = "imageUrl",     column = "image_url"),
            @Result(property = "cpName",       column = "cp_name"),
            @Result(property = "cpNameDisplay",column = "cp_name_display"),
            @Result(property = "dateTime",     column = "date"),
            @Result(property = "viewCount",    column = "view_count"),
            @Result(property = "suggestCount", column = "suggest_count"),
            @Result(property = "imageCount",   column = "image_count"),
            @Result(property = "videoCount",   column = "video_count"),
            @Result(property = "replyCount",   column = "reply_count")
    })
    List<Board> selectAllBoard();

    /**
     *
     * @param id
     * @return
     */
    @Select("SELECT id, title, writer, url, thumb_url, image_url, cp_name, cp_name_display, date, view_count, suggest_count, reply_count FROM board WHERE id=#{id}")
    @Results(value = {
            @Result(property = "id",           column = "id"),
            @Result(property = "title",        column = "title"),
            @Result(property = "writer",       column = "writer"),
            @Result(property = "url",          column = "url"),
            @Result(property = "thumbUrl",     column = "thumb_url"),
            @Result(property = "imageUrl",     column = "image_url"),
            @Result(property = "cpName",       column = "cp_name"),
            @Result(property = "cpNameDisplay",column = "cp_name_display"),
            @Result(property = "dateTime",     column = "date"),
            @Result(property = "viewCount",    column = "view_count"),
            @Result(property = "suggestCount", column = "suggest_count"),
            @Result(property = "imageCount",   column = "image_count"),
            @Result(property = "videoCount",   column = "video_count"),
            @Result(property = "replyCount",   column = "reply_count")
    })
    Board selectBoardId(@Param("id") int id);

    /**
     *
     * @param board
     * @return
     */
    @Select("SELECT title, writer, url, thumb_url, image_url, cp_name, cp_name_display, date, view_count, suggest_count, reply_count FROM board WHERE url=#{url}")
    @Results(value = {
            @Result(property = "title",        column = "title"),
            @Result(property = "writer",       column = "writer"),
            @Result(property = "url",          column = "url"),
            @Result(property = "thumbUrl",     column = "thumb_url"),
            @Result(property = "imageUrl",     column = "image_url"),
            @Result(property = "cpName",       column = "cp_name"),
            @Result(property = "cpNameDisplay",column = "cp_name_display"),
            @Result(property = "dateTime",     column = "date"),
            @Result(property = "viewCount",    column = "view_count"),
            @Result(property = "suggestCount", column = "suggest_count"),
            @Result(property = "imageCount",   column = "image_count"),
            @Result(property = "videoCount",   column = "video_count"),
            @Result(property = "replyCount",   column = "reply_count")
    })
    Board selectBoardUrl(Board board);

    /**
     *
     * @param board
     */
    @Select("INSERT INTO board (title,    writer,    url,    thumb_url,   image_url,   cp_name,   cp_name_display,  date,        view_count,   suggest_count,   reply_count,   image_count,   video_count) " +
            "VALUES (           #{title}, #{writer}, #{url}, #{thumbUrl}, #{imageUrl}, #{cpName}, #{cpNameDisplay}, #{dateTime}, #{viewCount}, #{suggestCount}, #{replyCount}, #{imageCount}, #{videoCount} )")
    void insertBoard(Board board);

    /**
     *
     * @param board
     */
    @Select("DELETE FROM board where url=#{url}")
    void deleteBoard(Board board);

    /**
     *
     * @param board
     */
    @Select("UPDATE board SET title=#{title}, writer=#{writer}, url=#{url}, thumb_url=#{thumbUrl}, image_url=#{imageUrl}, cp_name=#{cpName}, cp_name_display=#{cpNameDisplay}," +
            "date=#{dateTime}, view_count=#{viewCount}, suggest_count=#{suggestCount}, reply_count=#{replyCount}, image_count=#{imageCount}, video_count=#{videoCount} " +
            "WHERE url=#{url}")
    void updateBoard(Board board);

    @Select("UPDATE board SET view_count=#{viewCount}, suggest_count=#{suggestCount}, reply_count=#{replyCount}, thumb_url=#{thumbUrl} " +
            "WHERE url=#{url}")
    void updateBoardCount(Board board);
}
