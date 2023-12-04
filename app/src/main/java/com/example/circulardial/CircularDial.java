package com.example.circulardial;

import static androidx.core.math.MathUtils.clamp;

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
        m_textPaint = new Paint();

        m_outlinePaint.setColor(ContextCompat.getColor(context, R.color.lightGrey));
        m_insidePaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarkest));
        m_selectedPaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarker));
        m_unselectedPaint.setColor(ContextCompat.getColor(context, R.color.primaryColor));
        m_bigTicksPaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarkest));
        m_smallTicksPaint.setColor(ContextCompat.getColor(context, R.color.primaryColorDarkest));
        m_textPaint.setColor(ContextCompat.getColor(context, R.color.white));

        m_bigTicksPaint.setStrokeWidth(dpToPx(DEFAULT_TICK_THICKNESS));
        m_smallTicksPaint.setStrokeWidth(dpToPx(DEFAULT_TICK_THICKNESS / 1.5f));

        m_bigTicksPaint.setStrokeCap(Paint.Cap.ROUND);
        m_smallTicksPaint.setStrokeCap(Paint.Cap.ROUND);

        m_textPaint.setTextAlign(Paint.Align.CENTER);
        m_textPaint.setTextSize(dpToPx(DEFAULT_DISPLAY_DIAMETER / 3.f));

        setMinimumHeight((int)dpToPx(MIN_SLIDER_RADIUS * 2.f));
        setMinimumWidth((int)dpToPx(MIN_SLIDER_RADIUS * 2.f));
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
        canvas.drawArc(rect, -90.f,ratioToAngle(m_value), true, m_selectedPaint);
        canvas.drawCircle(totalRadius, totalRadius, dpToPx(DEFAULT_DISPLAY_DIAMETER / 2.f), m_insidePaint);

        final float sliderWidth = (DEFAULT_DIAL_DIAMETER - DEFAULT_DISPLAY_DIAMETER) / 2.f;

        for (int i = 0; i < 8; i++) {
            float x = (float)Math.cos((Math.PI / 4.f) * (float)i);
            float y = (float)Math.sin((Math.PI / 4.f) * (float)i);

            float radiusIn = dpToPx(DEFAULT_DISPLAY_DIAMETER / 2.f + 0.2f * sliderWidth);
            float radiusOut = dpToPx(DEFAULT_DISPLAY_DIAMETER / 2.f + 0.8f * sliderWidth);
            canvas.drawLine(
                    radiusIn * x + totalRadius, radiusIn * y + totalRadius,
                    radiusOut * x + totalRadius, radiusOut * y + totalRadius, m_bigTicksPaint);
        }

        for (int i = 0; i < 8; i++) {
            float x = (float)Math.cos((Math.PI / 4.f) * (float)i + Math.PI / 8.f);
            float y = (float)Math.sin((Math.PI / 4.f) * (float)i + Math.PI / 8.f);

            float radiusIn = dpToPx(DEFAULT_DISPLAY_DIAMETER / 2.f + 0.35f * sliderWidth);
            float radiusOut = dpToPx(DEFAULT_DISPLAY_DIAMETER / 2.f + 0.65f * sliderWidth);
            canvas.drawLine(
                    radiusIn * x + totalRadius, radiusIn * y + totalRadius,
                    radiusOut * x + totalRadius, radiusOut * y + totalRadius, m_smallTicksPaint);
        }

        canvas.drawText(String.valueOf((int)(m_value * 100.f)) + "%", totalRadius, totalRadius + dpToPx(DEFAULT_DISPLAY_DIAMETER / 8.f), m_textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // TODO: current size values
        int suggestedHeight = (int)Math.max(getMinimumHeight(), dpToPx(DEFAULT_DIAL_DIAMETER + DEFAULT_OUTLINE_THICKNESS * 2.f) + getPaddingTop() + getPaddingBottom());
        int suggestedWidth = (int)Math.max(getMinimumWidth(), dpToPx(DEFAULT_DIAL_DIAMETER + DEFAULT_OUTLINE_THICKNESS * 2.f) + getPaddingLeft() + getPaddingRight());

        int height = resolveSize(suggestedHeight, heightMeasureSpec);
        int width = resolveSize(suggestedWidth, widthMeasureSpec);

        setMeasuredDimension(width, height);
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

    public void setSliderChangeListener(SliderChangeListener m_sliderChangeListener) {
        this.m_sliderChangeListener = m_sliderChangeListener;
    }

    public void setValue(float value) {
        m_value = clamp(value, 0.f, 1.f);
        invalidate();
    }

    public float getValue() {
        return m_value;
    }

    private SliderChangeListener m_sliderChangeListener;

    private Paint
            m_outlinePaint, m_insidePaint,
            m_selectedPaint, m_unselectedPaint,
            m_bigTicksPaint, m_smallTicksPaint,
            m_textPaint;

    private float m_value = 0.3f;

    public final static float DEFAULT_DIAL_DIAMETER = 160.f; // Diameter of the dial (without outline)
    public final static float DEFAULT_OUTLINE_THICKNESS = 5.f; // Thickness of the dial outline
    public final static float DEFAULT_DISPLAY_DIAMETER = 70.f; // Diameter of the inside circle (where the % is displayed)
    public final static float DEFAULT_TICK_THICKNESS = 3.f; // Thickness of the tick strokes (big ticks)

    public final static float MIN_SLIDER_RADIUS = 160.f / 4.f;
}
