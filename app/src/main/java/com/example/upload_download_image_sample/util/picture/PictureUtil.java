package com.example.upload_download_image_sample.util.picture;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureUtil {


    public static List<String> getImageURIs(Context context) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Images.Media.DATE_ADDED);
        while (cursor.moveToNext()) {
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            list.add(uri);
        }
        return list;
    }

    public static boolean isImageExist(String url) {
        boolean flag = false;
        if (!TextUtils.isEmpty(url)) {
            File file = new File(url);
            if (file.exists()) {
                flag = true;
            }
        }
        return flag;
    }

    public static List<String> filterImages(Context context, List<String> imageURIsTemp) {
        List<String> imageURIs = new ArrayList<String>();
        for (String uri : imageURIsTemp) {
            if (!TextUtils.isEmpty(uri) && isImageExist(uri)) {
                int index = uri.lastIndexOf(".");
                if (index != -1) {
                    String suffix = uri.substring(index + 1);
                    if ("bmp".equalsIgnoreCase(suffix)
                            || "jpg".equalsIgnoreCase(suffix)
                            || "png".equalsIgnoreCase(suffix)
                            || "jpeg".equalsIgnoreCase(suffix)
                            || "webp".equalsIgnoreCase(suffix)) {
                        imageURIs.add(uri);
                    }
                }
            }
        }
        return imageURIs;
    }
}
