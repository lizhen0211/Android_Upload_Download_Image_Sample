package com.example.upload_download_image_sample.util.net;

import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class TransferByteArrayOutputStream extends ByteArrayOutputStream {

    private ProgressListener listener;

    private long transferred;

    public TransferByteArrayOutputStream(ProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) {
        super.write(b, off, len);
        this.transferred += len;
        this.listener.transferred(this.transferred);
    }

    @Override
    public void write(int b) {
        super.write(b);
        this.transferred++;
        this.listener.transferred(this.transferred);
    }

    public interface ProgressListener {
        void transferred(double progress);
    }
}
