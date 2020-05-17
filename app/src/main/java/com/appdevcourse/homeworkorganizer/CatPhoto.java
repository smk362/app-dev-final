package com.appdevcourse.homeworkorganizer;

import com.squareup.moshi.Json;

public class CatPhoto {
    @Json(name = "name")String name;
    @Json(name = "url")String url;

    public String getUrl() {
        return url;
    }
}
