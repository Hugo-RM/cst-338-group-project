package com.example.decisionwheel.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class WheelVIew extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();
    private Wheel wheel;

    public WheelVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setWheel(Wheel wheel){
        this.wheel = wheel;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(wheel == null || wheel.getSlices().isEmpty()){
            return;
        }

        ArrayList<Slice> slices = wheel.getSlices();
        float sweep = 360f / slices.size();
        float startAngle = 0f;

        float size = Math.min(getWidth(), getHeight());
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = (size / 2f) - 20f;
        
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        for(Slice slice : slices){
            // Draw slice
            paint.setColor(slice.getColor());
            canvas.drawArc(rectF, startAngle, sweep, true, paint);

            // Draw text
            canvas.save();
            // Rotate to the middle of the slice
            float middleAngle = startAngle + (sweep / 2f);
            canvas.rotate(middleAngle, centerX, centerY);
            
            // Draw text at a distance from center
            float textPadding = radius * 0.6f; 
            canvas.drawText(slice.getObjective(), centerX + textPadding, centerY + (textPaint.getTextSize() / 3), textPaint);
            
            canvas.restore();

            startAngle += sweep;
        }
    }
}
