package com.example.upload_download_image_sample.net;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.upload_download_image_sample.base.BaseApplication;
import com.example.upload_download_image_sample.cache.memory.LruBitmapCache;

/**
 * Created by lz on 2016/7/22.
 */
public class NetWorkMaster {
    private static NetWorkMaster mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private NetWorkMaster() {
        // Instantiate the cache
        Cache cache = new DiskBasedCache(BaseApplication.applicationContext.getCacheDir(), 5 * 1024 * 1024); // 5MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue = getRequestQueue();
        mRequestQueue.start();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
                LruBitmapCache.getCacheSize(BaseApplication.applicationContext)));
    }

    public static synchronized NetWorkMaster getInstance() {
        if (mInstance == null) {
            mInstance = new NetWorkMaster();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(BaseApplication.applicationContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelAll(final Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
