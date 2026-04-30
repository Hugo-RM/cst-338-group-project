package com.example.decisionwheel.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class WheelVIew extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();
    private Wheel wheel;

    public WheelVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWheel(Wheel wheel){
        this.wheel = wheel;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(wheel == null || wheel.getSlices() == null)
    }
}
