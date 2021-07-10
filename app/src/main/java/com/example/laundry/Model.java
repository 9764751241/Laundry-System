package com.example.laundry;

public class Model {
    String id,name,type,stype,price,status;

    public Model(String id ,String name, String type, String stype, String price,String status ) {
        this.id= id;
        this.name= name;
        this.type = type;
        this.stype = stype;
        this.price = price;
        this.status = status;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
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
    public String getStatus(){return status;   }
}
