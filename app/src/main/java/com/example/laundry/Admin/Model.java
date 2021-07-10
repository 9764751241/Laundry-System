package com.example.laundry.Admin;

public class Model {
    String id,type,stype,price;

    public Model(String id ,String type, String stype, String price ) {
        this.id = id;
        this.type = type;
        this.stype = stype;
        this.price = price;
    }

    public String getId(){
        return id;
    }
    public String getType(){
        return type;
    }
    public String getStype(){
        return stype;
    }

    public String getPrice(){
        return price;
    }
}
