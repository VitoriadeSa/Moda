package pt.ubi.di.pdm.modadebolsa;
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;

public class CirculoCoresActivity extends AppCompatActivity {



=======

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CirculoCoresActivity extends AppCompatActivity {

    private FrameLayout containerColorWheel;
    private ColorWheelView colorWheelView;
    private View viewCor1, viewCor2, viewCor3;
    private TextView tvHex1, tvHex2, tvHex3, tvPaletasSalvas;
    private Button btnGuardarPaleta;
    private ImageView ivLogoTop;

    private int selectedColor1, selectedColor2, selectedColor3;
    private String hex1 = "", hex2 = "", hex3 = "";
    private final List<PaletaItem> listaPaletas = new ArrayList<>();

    private static final String PREF_NAME = "ModaDeBolsa_Paletas";
    private static final String KEY_PALETAS = "paletas_salvas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulo_cores);

        containerColorWheel = findViewById(R.id.containerColorWheel);
        viewCor1 = findViewById(R.id.viewCor1);
        viewCor2 = findViewById(R.id.viewCor2);
        viewCor3 = findViewById(R.id.viewCor3);
        tvHex1 = findViewById(R.id.tvHex1);
        tvHex2 = findViewById(R.id.tvHex2);
        tvHex3 = findViewById(R.id.tvHex3);
        tvPaletasSalvas = findViewById(R.id.tvPaletasSalvas);
        btnGuardarPaleta = findViewById(R.id.btnGuardarPaleta);
        ivLogoTop = findViewById(R.id.ivLogoTop);

        ivLogoTop.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        carregarPaletasSalvas();

        colorWheelView = new ColorWheelView(this);
        containerColorWheel.addView(colorWheelView);

        colorWheelView.setOnTriadColorChangeListener((color1, color2, color3) -> {
            selectedColor1 = color1;
            selectedColor2 = color2;
            selectedColor3 = color3;

            hex1 = String.format("#%06X", (0xFFFFFF & color1));
            hex2 = String.format("#%06X", (0xFFFFFF & color2));
            hex3 = String.format("#%06X", (0xFFFFFF & color3));

            setDrawableColor(viewCor1, color1);
            setDrawableColor(viewCor2, color2);
            setDrawableColor(viewCor3, color3);

            tvHex1.setText(hex1);
            tvHex2.setText(hex2);
            tvHex3.setText(hex3);
        });

        btnGuardarPaleta.setOnClickListener(v -> {
            PaletaItem novaPaleta = new PaletaItem(
                    selectedColor1, hex1,
                    selectedColor2, hex2,
                    selectedColor3, hex3
            );

            listaPaletas.add(0, novaPaleta);

            if (listaPaletas.size() > 3) {
                listaPaletas.remove(listaPaletas.size() - 1);
            }

            salvarPaletasNasPreferencias();
            atualizarHistoricoVisual();

            Toast.makeText(this, "Paleta guardada com sucesso!", Toast.LENGTH_SHORT).show();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_outros);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_outros) {
                return true;
            } else if (id == R.id.nav_moldes) {
                startActivity(new Intent(this, ListaMoldesActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_tecidos) {
                startActivity(new Intent(this, ListaTecidosActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    private void salvarPaletasNasPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        StringBuilder sb = new StringBuilder();
        for (PaletaItem p : listaPaletas) {
            sb.append(p.c1).append(";").append(p.h1).append(";")
                    .append(p.c2).append(";").append(p.h2).append(";")
                    .append(p.c3).append(";").append(p.h3).append("|");
        }

        editor.putString(KEY_PALETAS, sb.toString());
        editor.apply();
    }

    private void carregarPaletasSalvas() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String dados = prefs.getString(KEY_PALETAS, "");

        listaPaletas.clear();
        if (!dados.isEmpty()) {
            String[] itens = dados.split("\\|");
            for (String s : itens) {
                String[] partes = s.split(";");
                if (partes.length == 6) {
                    try {
                        int c1 = Integer.parseInt(partes[0]);
                        String h1 = partes[1];
                        int c2 = Integer.parseInt(partes[2]);
                        String h2 = partes[3];
                        int c3 = Integer.parseInt(partes[4]);
                        String h3 = partes[5];
                        listaPaletas.add(new PaletaItem(c1, h1, c2, h2, c3, h3));
                    } catch (Exception ignored) {}
                }
            }
        }
        atualizarHistoricoVisual();
    }

    private void atualizarHistoricoVisual() {
        if (listaPaletas.isEmpty()) {
            tvPaletasSalvas.setText("Paletas Salvas: Nenhuma ainda");
            return;
        }

        SpannableStringBuilder builderGlobal = new SpannableStringBuilder("Paletas Salvas (Máx. 3):\n");

        for (int i = 0; i < listaPaletas.size(); i++) {
            PaletaItem p = listaPaletas.get(i);

            String linhaTexto = "• [V1] " + p.h1 + "    [V2] " + p.h2 + "    [V3] " + p.h3 + "\n";
            SpannableString spannableLinha = new SpannableString(linhaTexto);

            aplicarVetorNaTag(spannableLinha, "[V1]", p.c1);
            aplicarVetorNaTag(spannableLinha, "[V2]", p.c2);
            aplicarVetorNaTag(spannableLinha, "[V3]", p.c3);

            builderGlobal.append(spannableLinha);
        }

        tvPaletasSalvas.setText(builderGlobal);
    }

    private void aplicarVetorNaTag(SpannableString spannable, String tag, int cor) {
        String textoStr = spannable.toString();
        int index = textoStr.indexOf(tag);
        if (index != -1) {
            Drawable vectorDrawable = ContextCompat.getDrawable(this, R.drawable.ic_paleta_circulo);
            if (vectorDrawable != null) {
                Drawable wrappedDrawable = DrawableCompat.wrap(vectorDrawable);
                DrawableCompat.setTint(wrappedDrawable, cor);
                wrappedDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

                ImageSpan imageSpan = new ImageSpan(wrappedDrawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(imageSpan, index, index + tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static class PaletaItem {
        int c1, c2, c3;
        String h1, h2, h3;
        PaletaItem(int c1, String h1, int c2, String h2, int c3, String h3) {
            this.c1 = c1; this.h1 = h1;
            this.c2 = c2; this.h2 = h2;
            this.c3 = c3; this.h3 = h3;
        }
    }

    private void setDrawableColor(View view, int color) {
        if (view.getBackground() instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) view.getBackground();
            drawable.setColor(color);
        }
    }
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
<<<<<<< HEAD
}
=======

    public static class ColorWheelView extends View {

        private Paint wheelPaint;
        private Paint linePaint;
        private Paint handlePaint;
        private Paint handleBorderPaint;

        private float centerX, centerY, radius;
        private float currentAngle = 0;
        private float handleRadius = 35f;

        private OnTriadColorChangeListener colorChangeListener;

        public interface OnTriadColorChangeListener {
            void onColorsChanged(int color1, int color2, int color3);
        }

        public void setOnTriadColorChangeListener(OnTriadColorChangeListener listener) {
            this.colorChangeListener = listener;
        }

        public ColorWheelView(Context context) {
            super(context);
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            init();
        }

        private void init() {
            wheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(Color.WHITE);
            linePaint.setStrokeWidth(6f);
            linePaint.setStyle(Paint.Style.STROKE);

            handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            handlePaint.setStyle(Paint.Style.FILL);

            handleBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            handleBorderPaint.setColor(Color.WHITE);
            handleBorderPaint.setStyle(Paint.Style.STROKE);
            handleBorderPaint.setStrokeWidth(6f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            centerX = w / 2f;
            centerY = h / 2f;
            radius = Math.min(centerX, centerY) - 30f;

            int[] colors = {
                    Color.RED, Color.YELLOW, Color.GREEN,
                    Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED
            };
            float[] positions = { 0f, 1/6f, 2/6f, 3/6f, 4/6f, 5/6f, 1f };
            SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, positions);
            wheelPaint.setShader(sweepGradient);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawCircle(centerX, centerY, radius, wheelPaint);

            float angle1 = currentAngle;
            float angle2 = (currentAngle + 120) % 360;
            float angle3 = (currentAngle + 240) % 360;

            float x1 = centerX + (radius * 0.75f) * (float) Math.cos(Math.toRadians(angle1));
            float y1 = centerY + (radius * 0.75f) * (float) Math.sin(Math.toRadians(angle1));

            float x2 = centerX + (radius * 0.75f) * (float) Math.cos(Math.toRadians(angle2));
            float y2 = centerY + (radius * 0.75f) * (float) Math.sin(Math.toRadians(angle2));

            float x3 = centerX + (radius * 0.75f) * (float) Math.cos(Math.toRadians(angle3));
            float y3 = centerY + (radius * 0.75f) * (float) Math.sin(Math.toRadians(angle3));

            canvas.drawLine(centerX, centerY, x1, y1, linePaint);
            canvas.drawLine(centerX, centerY, x2, y2, linePaint);
            canvas.drawLine(centerX, centerY, x3, y3, linePaint);

            int c1 = getColorForAngle(angle1);
            int c2 = getColorForAngle(angle2);
            int c3 = getColorForAngle(angle3);

            if (colorChangeListener != null) {
                colorChangeListener.onColorsChanged(c1, c2, c3);
            }

            drawHandle(canvas, x1, y1, c1);
            drawHandle(canvas, x2, y2, c2);
            drawHandle(canvas, x3, y3, c3);
        }

        private void drawHandle(Canvas canvas, float x, float y, int color) {
            handlePaint.setColor(color);
            canvas.drawCircle(x, y, handleRadius, handlePaint);
            canvas.drawCircle(x, y, handleRadius, handleBorderPaint);
        }

        int getColorForAngle(float angle) {
            float normalizedAngle = (angle + 360) % 360;
            return Color.HSVToColor(new float[]{normalizedAngle, 1.0f, 1.0f});
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    float dx = event.getX() - centerX;
                    float dy = event.getY() - centerY;
                    currentAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
                    if (currentAngle < 0) currentAngle += 360;
                    invalidate();
                    return true;
            }
            return super.onTouchEvent(event);
        }
    }
}
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
