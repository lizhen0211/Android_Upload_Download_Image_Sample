package com.example.upload_download_image_sample.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

public class ProgressImageView extends ImageView {

    private Paint mPaint;
    private float textSize;

    private int progress;

    public ProgressImageView(Context context) {
        super(context);
        init();
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.progress < 100) {
            mPaint.setColor(Color.parseColor("#70000000"));// 半透明
            canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress
                    / 100, mPaint);

            mPaint.setColor(Color.parseColor("#00000000"));// 全透明
            canvas.drawRect(0, getHeight() - getHeight() * progress / 100,
                    getWidth(), getHeight(), mPaint);

            mPaint.setTextSize(textSize);
            mPaint.setColor(Color.parseColor("#FFFFFF"));
            mPaint.setStrokeWidth(2);
            Rect rect = new Rect();
            mPaint.getTextBounds(progress + "%", 0, (progress + "%").length(), rect);// 确定文字的宽度
            canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                    getHeight() / 2, mPaint);
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }
}
