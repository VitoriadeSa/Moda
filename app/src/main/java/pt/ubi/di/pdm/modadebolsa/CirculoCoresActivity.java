package pt.ubi.di.pdm.modadebolsa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pt.ubi.di.pdm.modadebolsa.data.Cor;
import pt.ubi.di.pdm.modadebolsa.data.dao.CorDao;
import pt.ubi.di.pdm.modadebolsa.data.repository.CorRepository;
import pt.ubi.di.pdm.modadebolsa.ui.CorUtil;
import pt.ubi.di.pdm.modadebolsa.ui.Dialogos;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;

public class CirculoCoresActivity extends AppCompatActivity {

    private FrameLayout containerColorWheel;
    private ColorWheelView colorWheelView;
    private View viewCor1, viewCor2, viewCor3;
    private TextView tvHex1, tvHex2, tvHex3, tvCoresGuardadas;
    private Button btnGuardarCor, btnAssociarCor;
    private RecyclerView rvCoresGuardadas;
    private ImageView ivLogoTop;

    private int selectedColor1 = Color.RED;
    private int selectedColor2 = Color.GREEN;
    private int selectedColor3 = Color.BLUE;
    private String hex1 = "#FF0000";
    private String hex2 = "#00FF00";
    private String hex3 = "#0000FF";

    private final List<PaletaItem> listaPaletas = new ArrayList<>();
    private PaletaAdapter paletaAdapter;
    private CorRepository corRepository;

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
        tvCoresGuardadas = findViewById(R.id.tvCoresGuardadas);
        btnGuardarCor = findViewById(R.id.btnGuardarCor);
        btnAssociarCor = findViewById(R.id.btnAssociarCor);
        rvCoresGuardadas = findViewById(R.id.rvCoresGuardadas);
        ivLogoTop = findViewById(R.id.ivLogoTop);

        inicializarRepositorio();

        rvCoresGuardadas.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        paletaAdapter = new PaletaAdapter(listaPaletas, paleta -> {
            aplicarPaletaNaTela(paleta);
            Toast.makeText(this, "Paleta carregada", Toast.LENGTH_SHORT).show();
        });
        rvCoresGuardadas.setAdapter(paletaAdapter);

        ivLogoTop.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        carregarPaletasSalvas();

        colorWheelView = new ColorWheelView(this);
        containerColorWheel.addView(colorWheelView);

        colorWheelView.setOnTriadColorChangeListener((c1, c2, c3) -> {
            selectedColor1 = c1;
            selectedColor2 = c2;
            selectedColor3 = c3;

            hex1 = String.format("#%06X", (0xFFFFFF & c1));
            hex2 = String.format("#%06X", (0xFFFFFF & c2));
            hex3 = String.format("#%06X", (0xFFFFFF & c3));

            setDrawableColor(viewCor1, c1);
            setDrawableColor(viewCor2, c2);
            setDrawableColor(viewCor3, c3);

            tvHex1.setText(hex1);
            tvHex2.setText(hex2);
            tvHex3.setText(hex3);
        });

        btnGuardarCor.setOnClickListener(v -> {
            PaletaItem novaPaleta = new PaletaItem(
                    selectedColor1, hex1,
                    selectedColor2, hex2,
                    selectedColor3, hex3
            );

            listaPaletas.add(0, novaPaleta);
            if (listaPaletas.size() > 6) {
                listaPaletas.remove(listaPaletas.size() - 1);
            }

            salvarPaletasNasPreferencias();
            paletaAdapter.notifyDataSetChanged();
            atualizarEstadoPaletas();

            Toast.makeText(this, "Paleta guardada com sucesso!", Toast.LENGTH_SHORT).show();
        });

        btnAssociarCor.setOnClickListener(v ->
                Dialogos.pedirTexto(
                        this,
                        "Associar Paleta a um Projeto",
                        "Nome da paleta ou projeto",
                        "",
                        InputType.TYPE_CLASS_TEXT,
                        this::guardarPaletaNaBaseDados
                )
        );

        NavegacaoInferior.configurar(this, findViewById(R.id.bottomNav), R.id.nav_cores);
    }

    private void inicializarRepositorio() {
        try {
            Class<?> dbClass = Class.forName("pt.ubi.di.pdm.modadebolsa.data.AppDatabase");
            Object dbInstance = null;
            for (String methodName : new String[]{"getInstance", "getDatabase", "obterInstancia"}) {
                try {
                    Method m = dbClass.getMethod(methodName, Context.class);
                    dbInstance = m.invoke(null, this);
                    if (dbInstance != null) break;
                } catch (NoSuchMethodException ignored) {
                }
            }
            if (dbInstance != null) {
                Method daoMethod = dbClass.getMethod("corDao");
                CorDao dao = (CorDao) daoMethod.invoke(dbInstance);
                if (dao != null) {
                    corRepository = new CorRepository(dao);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void guardarPaletaNaBaseDados(String nomeBase) {
        if (corRepository == null) {
            Toast.makeText(this, "Base de dados indisponível neste momento", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] hexes = { hex1, hex2, hex3 };
        AtomicInteger concluidas = new AtomicInteger(0);

        for (int i = 0; i < hexes.length; i++) {
            String nomeCor = nomeBase + " #" + (i + 1);
            Cor novaCor = new Cor(hexes[i], nomeCor, false);

            corRepository.inserir(novaCor, id -> {
                if (concluidas.incrementAndGet() == hexes.length) {
                    runOnUiThread(() -> Toast.makeText(
                            this,
                            "Paleta guardada na BD (" + nomeBase + ")",
                            Toast.LENGTH_SHORT
                    ).show());
                }
            });
        }
    }

    private void aplicarPaletaNaTela(PaletaItem p) {
        selectedColor1 = p.c1;
        selectedColor2 = p.c2;
        selectedColor3 = p.c3;
        hex1 = p.h1;
        hex2 = p.h2;
        hex3 = p.h3;

        setDrawableColor(viewCor1, p.c1);
        setDrawableColor(viewCor2, p.c2);
        setDrawableColor(viewCor3, p.c3);

        tvHex1.setText(p.h1);
        tvHex2.setText(p.h2);
        tvHex3.setText(p.h3);
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
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        if (paletaAdapter != null) {
            paletaAdapter.notifyDataSetChanged();
        }
        atualizarEstadoPaletas();
    }

    private void atualizarEstadoPaletas() {
        if (listaPaletas.isEmpty()) {
            tvCoresGuardadas.setText("Paletas guardadas: Nenhuma");
        } else {
            tvCoresGuardadas.setText("Paletas guardadas (" + listaPaletas.size() + ")");
        }
    }

    private static class PaletaItem {
        final int c1, c2, c3;
        final String h1, h2, h3;

        PaletaItem(int c1, String h1, int c2, String h2, int c3, String h3) {
            this.c1 = c1;
            this.h1 = h1;
            this.c2 = c2;
            this.h2 = h2;
            this.c3 = c3;
            this.h3 = h3;
        }
    }

    private interface OnPaletaClickListener {
        void onPaletaClick(PaletaItem paleta);
    }

    private static class PaletaAdapter extends RecyclerView.Adapter<PaletaAdapter.PaletaViewHolder> {

        private final List<PaletaItem> itens;
        private final OnPaletaClickListener listener;

        PaletaAdapter(List<PaletaItem> itens, OnPaletaClickListener listener) {
            this.itens = itens;
            this.listener = listener;
        }

        @NonNull
        @Override
        public PaletaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_cor, parent, false);
            return new PaletaViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PaletaViewHolder holder, int position) {
            PaletaItem p = itens.get(position);
            CorUtil.mostrar(holder.itemView, p.h1);
            holder.tvHex.setText(p.h1 + " " + p.h2 + " " + p.h3);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPaletaClick(p);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itens.size();
        }

        static class PaletaViewHolder extends RecyclerView.ViewHolder {
            final TextView tvHex;

            PaletaViewHolder(@NonNull View itemView) {
                super(itemView);
                tvHex = itemView.findViewById(R.id.tvHex);
            }
        }
    }

    private static void setDrawableColor(View view, int color) {
        if (view != null && view.getBackground() instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) view.getBackground();
            drawable.setColor(color);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static class ColorWheelView extends View {

        private Paint wheelPaint;
        private Paint linePaint;
        private Paint handlePaint;
        private Paint handleBorderPaint;

        private float centerX, centerY, radius;
        private float currentAngle = 0;
        private final float handleRadius = 35f;

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