package com.example.upload_download_image_sample.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * @author lizhen
 */
public class CircleProgressImageView extends ImageView {

    private Paint mPaint;

    private int progress;

    /**
     * 单位DP
     */
    private float circlePadding;

    /**
     * 内圆padding 单位像素
     */
    private float innerCirclePadding;

    public CircleProgressImageView(Context context) {
        super(context);
        init();
    }

    public CircleProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        // 消除锯齿
        mPaint.setAntiAlias(true);
        circlePadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        innerCirclePadding = 20F;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.progress < 100) {
            float centerRectWidth = getWidth() - 2 * circlePadding;
            float centerRectHeight = getHeight() - 2 * circlePadding;
            float centerLeft = circlePadding;
            float centerTop = circlePadding;
            float centerRight = centerLeft + centerRectWidth;
            float centerBottom = centerTop + centerRectHeight;
            //设置padding的矩形
            RectF centerRect = new RectF(centerLeft, centerTop, centerRight, centerBottom);
            //canvas.drawRect(centerRect, mPaint);
            float cx = centerRect.centerX();
            float cy = centerRect.centerY();
            float radius = Math.min(centerRectWidth, centerRectHeight) / 2F;

            mPaint.setColor(Color.parseColor("#FFFFFF"));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2);
            //以矩形中心描画的的圆环
            canvas.drawCircle(cx, cy, radius, mPaint);

            //内圆的外接正方形
            float squareWidth = Math.min(centerRectWidth, centerRectHeight) - innerCirclePadding;
            float squareHeight = squareWidth;

            float left = getWidth() / 2 - squareWidth / 2;
            float top = getHeight() / 2 - squareHeight / 2;
            float right = left + squareWidth;
            float bottom = top + squareHeight;
            RectF oval = new RectF(left, top, right, bottom);
            //canvas.drawRect(oval, mPaint);
            float percentage = progress / 100F;
            float startAngle = -90;
            float sweepAngle = percentage * 360;

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor("#30000000"));
            //内圆背景
            canvas.drawArc(oval, 0, 360, true, mPaint);
            mPaint.setColor(Color.parseColor("#C0FFFFFF"));
            //已完成的进度扇形
            canvas.drawArc(oval, startAngle, sweepAngle, true, mPaint);
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }
}
