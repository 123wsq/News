package com.yc.wsq.app.news.bean;

import org.litepal.crud.DataSupport;

public class CatTitleBean extends DataSupport{


    int cat_id;

    String cat_name;

    int cat_type;

    String parent_id;

    String show_in_nav;

    String sort_order;

    String cat_desc;

    String keywords;

    String cat_alias;

    String has_children;

    String level;

    String c_id;

    String name;



    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getCat_type() {
        return cat_type;
    }

    public void setCat_type(int cat_type) {
        this.cat_type = cat_type;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getShow_in_nav() {
        return show_in_nav;
    }

    public void setShow_in_nav(String show_in_nav) {
        this.show_in_nav = show_in_nav;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public void setCat_desc(String cat_desc) {
        this.cat_desc = cat_desc;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCat_alias() {
        return cat_alias;
    }

    public void setCat_alias(String cat_alias) {
        this.cat_alias = cat_alias;
    }

    public String getHas_children() {
        return has_children;
    }

    public void setHas_children(String has_children) {
        this.has_children = has_children;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {
        return c_id;
    }

    public void setId(String id) {
        this.c_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public CatTitleBean() {

    }

    public CatTitleBean(int cat_id, String cat_name,String id, String sort_order , int cat_type) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.c_id = id;
        this.sort_order =sort_order;
        this.cat_type = cat_type;
    }


}
