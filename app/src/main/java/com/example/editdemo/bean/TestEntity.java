package com.example.editdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guozhk on 16-7-26.
 */
public class TestEntity {
    private int id;
    private String name;
    private String title;
    private List<CommentEntity> commentList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommentEntity> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentEntity> commentList) {
        this.commentList = commentList;
    }

    public List<String> getPinglunList() {
        if(pinglunList==null){
            return pinglunList = new ArrayList<>();
        }
        return pinglunList;
    }

    public void setPinglunList(List<String> pinglunList) {
        this.pinglunList = pinglunList;
    }

    private List<String> pinglunList;
}
