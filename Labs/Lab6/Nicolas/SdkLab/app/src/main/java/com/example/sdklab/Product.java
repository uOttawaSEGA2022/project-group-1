package com.example.sdklab;

public class Product {
    private int id;
    private String productname;
    private int sku;

    public int getId() { return id; }
    public String getProductName() { return productname; }
    public int getSku() { return sku; }

    public void setId(int newID) { id = newID; }
    public void setProductName(String newProductName) { productname = newProductName; }
    public void setSku(int newSku) { sku = newSku; }
}
