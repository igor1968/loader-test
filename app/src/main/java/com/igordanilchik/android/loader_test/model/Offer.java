package com.igordanilchik.android.loader_test.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.HashMap;

@Root(name = "offer", strict = false)
public class Offer implements Serializable {

    @Attribute(name = "id")
    private int id;
    @Element(name = "url")
    private String url;
    @Element(name = "categoryId")
    private int categoryId;
    @Element(name = "name")
    private String name;
    @Element(name = "picture", required = false)
    private String pictureUrl;
    @Element(name = "price")
    private String price;
    @Element(name = "description", required = false)
    private String description;
    @ElementMap(entry = "param", key = "name", attribute = true, inline = true, required = false)
    private HashMap<String, String> param;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, String> getParam() {
        return param;
    }

    public void setParam(HashMap<String, String> param) {
        this.param = param;
    }
}
