package com.example.listen;

public class Friends {
    public Friends(){

    }
    public Friends(String alias, String friendUID) {
        this.alias = alias;
        this.friendUID = friendUID;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFriendUID() {
        return friendUID;
    }

    public void setFriendUID(String friendUID) {
        this.friendUID = friendUID;
    }

    String alias, friendUID;
}
