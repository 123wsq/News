package com.yc.wsq.app.news.bean;

import org.litepal.crud.DataSupport;

public class SearchBean extends DataSupport{


    private int id;

    private String keywords;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public SearchBean() {
        super();
    }

    public SearchBean( String keywords) {
        this.keywords = keywords;
    }
    public SearchBean(int id, String keywords) {
        this.id = id;
        this.keywords = keywords;
    }
}
