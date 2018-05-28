package com.example.listen;

public class Friend {
    public Friend(){

    }
    public Friend(String alias, String uid) {
        this.alias = alias;
        this.uid = uid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String alias, uid;
}
