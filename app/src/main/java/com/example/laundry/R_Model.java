package com.example.laundry;

public class R_Model {
    String id,type,stype,amount;

    public R_Model(String id ,String type, String stype, String amount) {
        this.id= id;
        this.type= type;
        this.stype = stype;
        this.amount = amount;
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

    public String getAmount(){
        return amount;
    }
}
