package com.loginmodule.data.remote;


import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import timber.log.Timber;

class RetrofitUtils {

    static RequestBody getRequestBodyFromString(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    static String getMimeType(File file) {
        String mimeType = "image/png";
        try {
            Uri selectedUri = Uri.fromFile(file);
            String fileExtension
                    = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            mimeType
                    = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        } catch (Exception e) {
            Timber.e("mime type exception %s", e.toString());
        }
        return mimeType;
    }
}

