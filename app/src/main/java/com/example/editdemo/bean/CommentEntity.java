package com.example.editdemo.bean;

/**
 * Created by guozhk on 16-7-29.
 */
public class CommentEntity {
    public static final int COMMENT_TYPE_OTHER = 1;
    public static final int COMMENT_TYPE_SELF = 0;
    private int id;
    private String name;//评论着
    private String content;//内容
    private int type;//类型 0：自己回复 1：别人评论


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
