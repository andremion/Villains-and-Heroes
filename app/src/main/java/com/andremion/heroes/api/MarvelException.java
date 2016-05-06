package com.andremion.heroes.api;

public class MarvelException extends Exception {

    private final int mCode;

    public MarvelException(int code, String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

}
