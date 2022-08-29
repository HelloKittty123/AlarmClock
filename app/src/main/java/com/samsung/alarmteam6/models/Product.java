package com.samsung.alarmteam6.models;

public class Product {
    public int ProductID;
    public String timer;
    public String buff;


    public Product(int productID, String buff, String timer) {
        ProductID = productID;
        this.timer = timer;
        this.buff = buff;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getBuff() {
        return buff;
    }

    public void setBuff(String buff) {
        this.buff = buff;
    }
}
