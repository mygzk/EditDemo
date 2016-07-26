package com.example.editdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guozhk on 16-7-26.
 */
public class TestEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
