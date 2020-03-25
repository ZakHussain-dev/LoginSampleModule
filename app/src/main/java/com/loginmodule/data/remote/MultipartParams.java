package com.loginmodule.data.remote;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MultipartParams {
    private HashMap<String, RequestBody> map;

    private MultipartParams(Builder builder) {
        this.map = builder.map;
    }

    public HashMap<String, RequestBody> getMap() {
        return map;
    }


    public static class Builder {
        HashMap<String, RequestBody> map = new HashMap<>();

        public Builder() {
        }

        public Builder add(String key, Object value) {

            if (value == null)
                return this;
            map.put(key, RetrofitUtils.getRequestBodyFromString(String.valueOf(value)));
            return this;
        }

        //for single file
        public Builder addFile(String key, File mFile) {
            if (mFile == null)
                return this;

            map.put(key + "\"; filename=\"" + mFile.getName(), RequestBody.create(MediaType.parse(RetrofitUtils.getMimeType(mFile)), mFile));
            return this;
        }

        public MultipartParams build() {
            return new MultipartParams(this);
        }
    }
}

