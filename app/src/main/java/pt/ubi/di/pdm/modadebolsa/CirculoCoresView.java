package pt.ubi.di.pdm.modadebolsa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CirculoCoresView extends View {
    public interface OnColorChangeListener {
        void onColorChanged(int color);
    }

    private static final float RAIO_SELETOR = 35f;
    private static final float POSICAO_SELETOR = 0.75f;
    private final Paint wheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float centerX, centerY, radius;
    private float angle = 0f;
    private OnColorChangeListener listener;

    public CirculoCoresView(Context context) {
        this(context, null);
    }

    public CirculoCoresView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);
        handlePaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStrokeWidth(6f);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public void setOnColorChangeListener(OnColorChangeListener listener) {
        this.listener = listener;
    }

    public int getColor() {
        return Color.HSVToColor(new float[]{angle, 1f, 1f});
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(centerX, centerY) - 30f;
        int[] cores = {Color.RED, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
        float[] posicoes = {0f, 1 / 6f, 2 / 6f, 3 / 6f, 4 / 6f, 5 / 6f, 1f};
        wheelPaint.setShader(new SweepGradient(centerX, centerY, cores, posicoes));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radius, wheelPaint);
        double rad = Math.toRadians(angle);
        float x = centerX + radius * POSICAO_SELETOR * (float) Math.cos(rad);
        float y = centerY + radius * POSICAO_SELETOR * (float) Math.sin(rad);
        canvas.drawLine(centerX, centerY, x, y, linePaint);
        handlePaint.setColor(getColor());
        canvas.drawCircle(x, y, RAIO_SELETOR, handlePaint);
        canvas.drawCircle(x, y, RAIO_SELETOR, borderPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                angle = (float) Math.toDegrees(
                        Math.atan2(event.getY() - centerY, event.getX() - centerX));
                if (angle < 0) {
                    angle += 360;
                }
                invalidate();
                if (listener != null) {
                    listener.onColorChanged(getColor());
                }
                return true;
            case MotionEvent.ACTION_UP:
                performClick();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}