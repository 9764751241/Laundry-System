package com.example.laundry.Admin;

public class Model2 {
    String id2,name2,type2,stype2,price2,status2;

    public Model2(String id2 ,String name2, String type2, String stype2, String price2,String status2 ) {
        this.id2= id2;
        this.name2= name2;
        this.type2 = type2;
        this.stype2 = stype2;
        this.price2 = price2;
        this.status2 = status2;
    }

    public String getId2(){
        return id2;
    }
    public String getName2(){
        return name2;
    }
    public String getType2(){
        return type2;
    }

    public String getStype2(){
        return stype2;
    }

    public String getPrice2(){
        return price2;
    }
    public String getStatus2(){return status2;   }
}