package com.loginmodule.util.gravatar;


import com.loginmodule.util.Utils;
import com.loginmodule.util.Utils;

public class RequestBuilder {
    private static final String GRAVATAR_ENDPOINT = "http://www.gravatar.com/avatar/";
    private final StringBuilder builder;

    RequestBuilder(String email) {
        final String hash = Utils.convertEmailToHash(email);
        builder = new StringBuilder(GRAVATAR_ENDPOINT.length() + hash.length() + 1).append(GRAVATAR_ENDPOINT);
        builder.append(hash);
        builder.append("?");
    }

    /**
     * Specifies the image dimensions (as the image is always squared) to be retrieved,
     * allowing to save both bandwidth and memory.
     * <p/>
     *
     * @param sizeInPixels must be between {@code Gravatar.MIN_IMAGE_SIZE_PIXEL}
     *                     and {@code Gravatar.MAX_IMAGE_SIZE_PIXEL}.
     */
    public RequestBuilder size(int sizeInPixels) {
        if (sizeInPixels >= Gravatar.MIN_IMAGE_SIZE_PIXEL && sizeInPixels <= Gravatar.MAX_IMAGE_SIZE_PIXEL) {
            builder.append("&size=").append(sizeInPixels);
            return this;
        }
        throw new IllegalArgumentException("Requested image size must be between " + Gravatar.MIN_IMAGE_SIZE_PIXEL
                + " and " + Gravatar.MAX_IMAGE_SIZE_PIXEL);
    }

    /**
     * @return the fully built Gravatar URL.
     */
    public String build() {
        final int size = builder.length() - 1;
        if (builder.charAt(size) == '?') {
            builder.deleteCharAt(size);
        }
        return builder.toString();
    }
}