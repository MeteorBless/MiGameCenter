package com.example.migamecenter.bean;

import java.util.List;

public class Page<T> {
    public List<T> records;

    public int total; //总数

    public int size; // 当前页大小

    public int current; // 当前页

    public int pages; // 总页数

}
