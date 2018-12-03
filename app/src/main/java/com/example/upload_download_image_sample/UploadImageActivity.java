package com.example.upload_download_image_sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.upload_download_image_sample.net.NetWorkMaster;
import com.example.upload_download_image_sample.ui.DividerGridItemDecoration;
import com.example.upload_download_image_sample.ui.adapter.GridRecyclerViewAdapter;
import com.example.upload_download_image_sample.util.net.MultipartRequest;
import com.example.upload_download_image_sample.util.net.TransferByteArrayOutputStream;
import com.example.upload_download_image_sample.util.picture.PictureUtil;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadImageActivity extends Activity {

    private static final String TAG = UploadImageActivity.class.getSimpleName();

    private int pictureSize = 10;
    private AtomicInteger uploadSize = new AtomicInteger(pictureSize);

    List<GridRecyclerViewAdapter.ProgressImage> progressImages;
    private GridRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, R.drawable.divider_bg1));
        progressImages = getProgressImage();
        adapter = new GridRecyclerViewAdapter(this, progressImages);
        recyclerView.setAdapter(adapter);
    }

    public void onUploadClick(View view) {
        for (int i = 0; i < uploadSize.intValue(); i++) {
            File file = new File(progressImages.get(i).getImageURI());
            requestUpload(file, file.getName(), "wash_car", i);
        }
    }

    public void requestUpload(File file, String fileName, String source, final int index) {
        MultipartRequest request = new MultipartRequest("https://uustorage-t.uucin.com/storages/images", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                int i = uploadSize.decrementAndGet();
                if (i == 0) {
                    Log.e(TAG, "all uploadComplete");
                    uploadSize.set(pictureSize);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                int i = uploadSize.decrementAndGet();
                if (i == 0) {
                    uploadSize.set(pictureSize);
                    Log.e(TAG, "all uploadComplete");
                }
            }
        }) {
            @Override
            public byte[] getBody() {
                MultipartEntity multiPartEntity = getMultiPartEntity();
                final long totalLen = multiPartEntity.getContentLength();
                TransferByteArrayOutputStream bos = new TransferByteArrayOutputStream(new TransferByteArrayOutputStream.ProgressListener() {
                    @Override
                    public void transferred(double progress) {
                        updateCurProgress(totalLen, progress);
                    }
                });

                try {
                    // multipart body
                    multiPartEntity.writeTo(bos);
                } catch (IOException e) {
                    Log.e("", "IOException writing to ByteArrayOutputStream");
                }
                return bos.toByteArray();
            }

            private synchronized void updateCurProgress(long totalLen, double progress) {
                Log.e(TAG + index, (progress / totalLen) * 100 + " :" + progress + " :" + totalLen);
                List<GridRecyclerViewAdapter.ProgressImage> progressImages = adapter.getProgressImages();
                progressImages.get(index).setProgress((progress / totalLen) * 100);
                adapter.setProgressImages(progressImages);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemChanged(index);
                    }
                });
            }
        };
        MultipartEntity multipartEntity = request.getMultiPartEntity();
        try {
            multipartEntity.addPart("file", new FileBody(file, fileName, "img/png", "utf-8"));
            multipartEntity.addPart("source", new StringBody(source, Charset.forName("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        NetWorkMaster.getInstance().addToRequestQueue(request);
    }

    private List<GridRecyclerViewAdapter.ProgressImage> getProgressImage() {
        List<GridRecyclerViewAdapter.ProgressImage> progressImages = new ArrayList<GridRecyclerViewAdapter.ProgressImage>();
        List<String> imageURIs = PictureUtil.getImageURIs(this);
        List<String> images = PictureUtil.filterImages(this, imageURIs);
        for (String imageUri : images) {
            GridRecyclerViewAdapter.ProgressImage progressImage = new GridRecyclerViewAdapter.ProgressImage();
            progressImage.setImageURI(imageUri);
            progressImages.add(progressImage);
        }
        return progressImages.subList(0, pictureSize);
    }
}
