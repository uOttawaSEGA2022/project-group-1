package com.uottawa.seg2105.grp1.mealer.model;

import java.util.Map;

// TODO: Fix up
public class Complaint implements IRepositoryEntity {
    private String _id;
    private String _productname;
    private double _price;

    public Complaint() {
    }
    public Complaint(String id, String productname, double price) {
        _id = id;
        _productname = productname;
        _price = price;
    }
    public Complaint(String productname, double price) {
        _productname = productname;
        _price = price;
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public void setProductName(String productname) {
        _productname = productname;
    }
    public String getProductName() {
        return _productname;
    }
    public void setPrice(double price) {
        _price = price;
    }
    public double getPrice() {
        return _price;
    }

    @Override
    public Map<String, Object> serialise() {
        // TODO: implement serialisation
        throw new UnsupportedOperationException();
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        // TODO: implement deserialisation
        throw new UnsupportedOperationException();
    }
}
