package com.flask.colorpicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;

import com.flask.colorpicker.builder.PaintBuilder;

public class CircleColorDrawable extends ColorDrawable {
    private int mWidth = 0;
    private float strokeWidth;
    private Paint strokePaint = PaintBuilder.newPaint().style(Paint.Style.STROKE).stroke(strokeWidth).color(0xffffffff).build();
    private Paint fillPaint = PaintBuilder.newPaint().style(Paint.Style.FILL).color(0).build();
    private Paint fillBackPaint = PaintBuilder.newPaint().shader(PaintBuilder.createAlphaPatternShader(16)).build();

    public CircleColorDrawable() {
        super();
    }

    public CircleColorDrawable(int color) {
        super(color);
    }

    public CircleColorDrawable(int c, int v) {
        super(c);
        mWidth = v;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(0);
        int width = 0;
        if (mWidth != 0) {
            width = mWidth;
        } else {
            width = getBounds().width();
        }
        float radius = width / 2f;
        strokeWidth = radius / 12f;

        this.strokePaint.setStrokeWidth(strokeWidth);
        this.fillPaint.setColor(getColor());
        canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5f, fillBackPaint);
        canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5f, fillPaint);
        canvas.drawCircle(radius, radius, radius - strokeWidth, strokePaint);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        invalidateSelf();
    }
}