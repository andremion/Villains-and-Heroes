package com.andremion.heroes.api.json;

public class Util {

    public static String getLastPathSegment(String uri) {
        if (uri == null) {
            return "";
        }
        int indexOf = uri.lastIndexOf("/");
        return uri.substring(indexOf + 1);
    }
}
