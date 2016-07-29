package com.example.editdemo;

/**
 * Created by guozhk on 16-7-26.
 */
public class CommentConfig {
    public enum Type {
        PUBLIC("public"), REPLY("reply");

        private String value;

        private Type(String value) {
            this.value = value;
        }

    }
    
    public int itemId;//列表item Id
    public int commentItemId;//评论列表位置 id
    public int itemPosition;//列表item位置
    public int commentItemPosition;//评论列表位置
    public Type commentType;//类型


    @Override
    public String toString() {
        String replyUserStr = "";

        return "itemPosition = " + itemPosition
                + "; commentItemPosition = " + commentItemPosition
                + "; commentType ＝ " + commentType
                + "; replyUser = " + replyUserStr;
    }
}
