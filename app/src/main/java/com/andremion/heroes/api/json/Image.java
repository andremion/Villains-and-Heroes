package com.andremion.heroes.api.json;

public class Image {

    public static final String SIZE_PORTRAIT_XLARGE = "portrait_xlarge";
    public static final String SIZE_STANDARD_LARGE = "standard_large";
    public static final String SIZE_STANDARD_INCREDIBLE = "standard_incredible";
    public static final String SIZE_DETAIL = "detail";

    public String path;
    public String extension;

    public static String getUrl(String path, String size, String extension) {
        return path + "/" + size + "." + extension;
    }
}
