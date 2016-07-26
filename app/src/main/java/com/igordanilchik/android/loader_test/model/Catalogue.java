package com.igordanilchik.android.loader_test.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "yml_catalog", strict = false)
public class Catalogue {

    @Element(name = "shop")
    private Shop shop;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }


}
