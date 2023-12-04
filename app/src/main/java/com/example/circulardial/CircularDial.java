package com.example.circulardial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

public class CircularDial extends View {
    // Création dynamique du slider
    public CircularDial(Context context)
    {
        super(context);
        init(context, null);
    }

    // Création statique du slider
    public CircularDial(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public interface SliderChangeListener
    {
        void onChange(float value);
    }

    private void init(Context context, AttributeSet attrs)
    {
        m_outlinePaint = new Paint();
        m_insidePaint = new Paint();
        m_selectedPaint = new Paint();
        m_unselectedPaint = new Paint();
        m_bigTicksPaint = new Paint();
        m_smallTicksPaint = new Paint();

        m_outlinePaint.setColor(ContextCompat.getColor(context, R.color.lightGrey));
        m_insidePaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarkest));
        m_selectedPaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarker));
        m_unselectedPaint.setColor(ContextCompat.getColor(context, R.color.primaryColor));
        m_bigTicksPaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarkest));
        m_smallTicksPaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarkest));

        m_bigTicksPaint.setStrokeWidth(dpToPx(DEFAULT_TICK_THICKNESS));
        m_smallTicksPaint.setStrokeWidth(dpToPx(DEFAULT_TICK_THICKNESS / 1.5f));

        m_bigTicksPaint.setStrokeCap(Paint.Cap.ROUND);
        m_smallTicksPaint.setStrokeCap(Paint.Cap.ROUND);

        // TODO
        //setMinimumHeight((int)dpToPx(MIN_CURSOR_DIAMETER + MIN_BAR_LENGTH));
        //setMinimumWidth((int)dpToPx(MIN_CURSOR_DIAMETER));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //adaptDims(); TODO

        final float totalRadius = dpToPx(DEFAULT_DIAL_DIAMETER / 2.f + DEFAULT_OUTLINE_THICKNESS);
        final RectF rect = new RectF(
                dpToPx(DEFAULT_OUTLINE_THICKNESS),
                dpToPx(DEFAULT_OUTLINE_THICKNESS),
                dpToPx(DEFAULT_OUTLINE_THICKNESS + DEFAULT_DIAL_DIAMETER),
                dpToPx(DEFAULT_OUTLINE_THICKNESS + DEFAULT_DIAL_DIAMETER));

        canvas.drawCircle(totalRadius, totalRadius, totalRadius, m_outlinePaint);
        canvas.drawCircle(totalRadius, totalRadius, dpToPx(DEFAULT_DIAL_DIAMETER / 2.f), m_unselectedPaint);
        canvas.drawArc(rect, -90.f, 50.f, true, m_selectedPaint);
        canvas.drawCircle(totalRadius, totalRadius, dpToPx(DEFAULT_DISPLAY_DIAMETER / 2.f), m_insidePaint);
    }

    private float dpToPx(float dp)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private float angleToRatio(float angle) {
        return angle / 360.f;
    }

    private float ratioToAngle(float ratio) {
        return ratio * 360.f;
    }

    private float positionToAngle(Point point) {
        return 0; // TODO
    }

    public void setM_sliderChangeListener(SliderChangeListener m_sliderChangeListener) {
        this.m_sliderChangeListener = m_sliderChangeListener;
    }

    private SliderChangeListener m_sliderChangeListener;

    private Paint
            m_outlinePaint, m_insidePaint,
            m_selectedPaint, m_unselectedPaint,
            m_bigTicksPaint, m_smallTicksPaint;

    public final static float DEFAULT_DIAL_DIAMETER = 160.f; // Diameter of the dial (without outline)
    public final static float DEFAULT_OUTLINE_THICKNESS = 5.f; // Thickness of the dial outline
    public final static float DEFAULT_DISPLAY_DIAMETER = 70.f; // Diameter of the inside circle (where the % is displayed)
    public final static float DEFAULT_TICK_THICKNESS = 3.f; // Thickness of the tick strokes (big ticks)
}
