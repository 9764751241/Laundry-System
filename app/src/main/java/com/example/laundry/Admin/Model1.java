package com.example.laundry.Admin;

public class Model1 {
    String id1,name1,type1,stype1,price1,status1;

    public Model1(String id1 ,String name1, String type1, String stype1, String price1,String status1 ) {
        this.id1= id1;
        this.name1= name1;
        this.type1 = type1;
        this.stype1 = stype1;
        this.price1 = price1;
        this.status1 = status1;
    }

    public String getId1(){
        return id1;
    }
    public String getName1(){
        return name1;
    }
    public String getType1(){
        return type1;
    }

    public String getStype1(){
        return stype1;
    }

    public String getPrice1(){
        return price1;
    }
    public String getStatus1(){return status1;   }
}