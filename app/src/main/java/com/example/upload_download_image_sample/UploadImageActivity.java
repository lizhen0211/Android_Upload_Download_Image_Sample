package com.example.upload_download_image_sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.upload_download_image_sample.ui.DividerGridItemDecoration;
import com.example.upload_download_image_sample.ui.adapter.GridRecyclerViewAdapter;
import com.example.upload_download_image_sample.util.picture.PictureUtil;

import java.util.ArrayList;
import java.util.List;

public class UploadImageActivity extends Activity {

    private int pictureSize = 10;

    private GridRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, R.drawable.divider_bg1));
        adapter = new GridRecyclerViewAdapter(this, getProgressImage());
        recyclerView.setAdapter(adapter);
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
