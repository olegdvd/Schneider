package com.olegdvd.grabber.domain;

public enum KeysEnum {

    NAME("name"),
    URL("url"),
    PRICE("price"),
    HREF("href")
    ;

    private String code;

    KeysEnum(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }

}