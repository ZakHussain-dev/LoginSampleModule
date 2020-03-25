package com.loginmodule.databinding;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.BindingAdapter;

import com.loginmodule.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Binding adapters
 * <p>
 */
final class BindingAdapters {

    private BindingAdapters() {
        // Private Constructor to hide the implicit one
    }

    @BindingAdapter(value = "imageUrl")
    public static void loadImage(ImageView imageView, String imgUrl) {
        Glide.with(imageView.getContext()
        ).asBitmap()
                .apply(new RequestOptions().fitCenter())
                .placeholder(R.drawable.ic_profile_placeholder)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(600, 200)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(final Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
