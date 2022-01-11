package com.example.jpa.user.model;

public enum UserPointType {
    NONE(0),
    USER_REGISTER(100),
    ADD_POST(10),
    ADD_COMMENT(1),
    ADD_LIKE(1);

    int value;
    public int getValue(){
        return this.value;
    }

    UserPointType(int value) {
        this.value = value;
    }
}
