/*
 * Copyright (c) 2017. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
