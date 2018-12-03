package com.example.upload_download_image_sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(this);
        Toast.makeText(this, "请打开写文件权限", Toast.LENGTH_LONG).show();
    }

    public void onUploadClick(View view) {
        Intent intent = new Intent(this, UploadImageActivity.class);
        startActivity(intent);
    }

    public void init(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(960, 960)
                .diskCacheSize(20 * 1024 * 1024)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(((int) Runtime.getRuntime().maxMemory() / 10)))
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
