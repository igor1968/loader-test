package com.igordanilchik.android.loader_test.data;

import org.parceler.Parcel;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;

@Parcel
@Root(name = "offer", strict = false)
public class Offer {

    @Attribute(name = "id")
    int id;
    @Element(name = "url")
    String url;
    @Element(name = "categoryId")
    int categoryId;
    @Element(name = "name")
    String name;
    @Element(name = "picture", required = false)
    String pictureUrl;
    @Element(name = "price")
    String price;
    @Element(name = "description", required = false)
    String description;
    @ElementMap(entry = "param", key = "name", attribute = true, inline = true, required = false)
    HashMap<String, String> param;

    public Offer() {
    }

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
