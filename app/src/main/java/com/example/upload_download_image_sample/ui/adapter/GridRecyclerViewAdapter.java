package com.example.upload_download_image_sample.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.upload_download_image_sample.R;
import com.example.upload_download_image_sample.ui.widget.ProgressImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by lz on 2016/9/29.
 */
public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private Context context;

    private List<ProgressImage> progressImages;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    public GridRecyclerViewAdapter(Context context, List<ProgressImage> progressImages) {
        this.context = context;
        this.progressImages = progressImages;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_recyclerview_item, parent, false);
        ViewHolder vh = new ViewHolder(root);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        imageLoader.displayImage("file:///" + progressImages.get(position).imageURI.substring(1), holder.imageView, options);
        holder.imageView.setProgress((int) progressImages.get(position).getProgress());
    }

    @Override
    public int getItemCount() {
        return this.progressImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProgressImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ProgressImageView) itemView.findViewById(R.id.grid_item_img);
        }
    }

    public void setProgressImages(List<ProgressImage> progressImages) {
        this.progressImages = progressImages;
    }

    public List<ProgressImage> getProgressImages() {
        return progressImages;
    }

    public static class ProgressImage {
        private String imageURI;
        private double progress;

        public String getImageURI() {
            return imageURI;
        }

        public void setImageURI(String imageURI) {
            this.imageURI = imageURI;
        }

        public double getProgress() {
            return progress;
        }

        public void setProgress(double progress) {
            this.progress = progress;
        }
    }
}
