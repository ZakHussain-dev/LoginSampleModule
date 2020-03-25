package com.loginmodule.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;

import com.loginmodule.BuildConfig;
import com.loginmodule.R;
import com.loginmodule.common.Codes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();
    private Activity activity;
    private final int cameraRequestCode = Codes.REQUEST_OPEN_CAMERA_ADD_IMAGE;
    private final int galleryRequestCode = Codes.REQUEST_OPEN_GALLERY_ADD_IMAGE;

    @Inject
    public ImageUtils() {
    }

    public void showImageChooserDialog(final Activity activity) {
        try {
            this.activity = activity;
            final Dialog dialog = new Dialog(activity, R.style.dialog_translucent);
            dialog.setContentView(R.layout.dialog_image_chooser);
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);


            AppCompatButton btnCamera = dialog.findViewById(R.id.btnCamera);
            AppCompatButton btnGallery = dialog.findViewById(R.id.btnGallery);
            AppCompatTextView tvTitle = dialog.findViewById(R.id.tvTitle);

            btnCamera.setText(R.string.camera);
            btnGallery.setText(R.string.gallery);
            tvTitle.setText(R.string.pick_image_from);

            btnCamera.setOnClickListener(view -> {
                dialog.dismiss();
                startCamera();
            });

            btnGallery.setOnClickListener(view -> {
                dialog.dismiss();
                openGallery();
            });

            dialog.show();

        } catch (Exception e) {
        }
    }

    /**
     * Method to start the Camera
     */
    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File fileToBeWritten = new File(getDirectory(), "temp.jpg");
            if (!fileToBeWritten.exists()) {
                try {
                    fileToBeWritten.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                try {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity,
                            BuildConfig.APPLICATION_ID + ".provider",
                            fileToBeWritten));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileToBeWritten));
            }
            activity.startActivityForResult(takePictureIntent, cameraRequestCode);
        }
    }

    /**
     * Method to retrieve the App Directory,
     * where files like logs can be Saved
     *
     * @return directory corresponding to the FileType
     */
    private File getDirectory() {
        try {
            String strFolder = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_NAME + File.separator;

            File folder = new File(strFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to perform operation on image after
     * selecting from Gallery
     *
     * @param uri
     */
    public void copyFileFromUri(Uri uri) throws IOException {

        Timber.e(TAG, "copyFileFromUri");
        if (activity == null || uri == null)
            return;
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);

        FileOutputStream fileOutputStream = new FileOutputStream(
                new File(getDirectory(), "temp.jpg")
        );

        byte[] buffer = new byte[1024];

        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1)
            fileOutputStream.write(buffer, 0, bytesRead);

        fileOutputStream.close();

        inputStream.close();
    }

    public String compressImage() {
        String filePath;
        Bitmap scaledBitmap = null, bmp = null;
        try {
            filePath = new File(getDirectory(), "temp.jpg").getAbsolutePath();
            BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 1.5f * 816.0f;
            float maxWidth = 1.5f * 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filename = null;
        try {
            filename = saveBitmapImage(scaledBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }


    /**
     * Method to open the Gallery view
     */

    private void openGallery() {
        Timber.e(TAG, "openGallery");
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            activity.startActivityForResult(photoPickerIntent, galleryRequestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * A power of two value is calculated because the decoder
     * uses a final value by rounding down to the nearest power of two.
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {

        Timber.e(TAG, "calculateInSampleSize");
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {
            final int heightRatio = Math.round((float) height / (float) requiredHeight);
            final int widthRatio = Math.round((float) width / (float) requiredWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = requiredWidth * requiredHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        Timber.e(TAG, "calculateInSampleSize==" + inSampleSize);
        return inSampleSize;
    }

    /**
     * Method to convert a Bitmap to file
     */
    private void convertBitmapToFile(Bitmap bitmap, String filePath) throws IOException {

        Timber.e(TAG, "convertBitmapToFile");

        // Create a file to write bitmap data
        File file = new File(filePath);
        file.createNewFile();

        if (bitmap == null)
            return;

        try {
            // Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70   /*ignored for PNG*/, bos);

            // Write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to save the Bitmap
     */
    private String saveBitmapImage(Bitmap bitmap) throws IOException {
        Timber.e(TAG, "saveBitmapImage");
        String timeStamp = new SimpleDateFormat("ddMMyyyy_hhmmss", Locale.ENGLISH).format(new Date());
        String fileName = "PROFILE" + timeStamp + ".jpg";
        String path = new File(getDirectory(), fileName).getAbsolutePath();
        convertBitmapToFile(bitmap, path);
        return path;
    }

}

