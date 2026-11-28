package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import pt.ubi.di.pdm.modadebolsa.ui.ListaAdapter;
import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.Cor;
import pt.ubi.di.pdm.modadebolsa.data.repository.CorRepository;
import pt.ubi.di.pdm.modadebolsa.ui.CirculoCoresView;
import pt.ubi.di.pdm.modadebolsa.ui.CirculoCoresView;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;
import pt.ubi.di.pdm.modadebolsa.ui.CorUtil;

public class CirculoCoresActivity extends AppCompatActivity {

    private CorRepository corRepository;
    private ListaAdapter<Cor> adapterGuardadas;
    private TextView tvCoresGuardadas;
    private String hexAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulo_cores);

        corRepository = new CorRepository(AppDatabase.getInstance(this).corDao());
        tvCoresGuardadas = findViewById(R.id.tvCoresGuardadas);

        findViewById(R.id.ivLogoTop).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Roda de cores
        CirculoCoresView roda = new CirculoCoresView(this);
        ((FrameLayout) findViewById(R.id.containerColorWheel)).addView(roda);
        roda.setOnColorChangeListener(this::mostrarCor);
        mostrarCor(roda.getColor()); // cor inicial

        // Cores guardadas (BD)
        adapterGuardadas = new ListaAdapter<>(R.layout.layout_cor,
                (v, cor) -> CorUtil.mostrar(v, cor.codigoHex)); // AJUSTA o nome do campo
        ((RecyclerView) findViewById(R.id.rvCoresGuardadas)).setAdapter(adapterGuardadas);

        findViewById(R.id.btnGuardarCor).setOnClickListener(v -> guardarCor());

        NavegacaoInferior.configurar(this, findViewById(R.id.bottomNav), R.id.nav_cores);
        carregarCores();
    }

    private void mostrarCor(int cor) {
        hexAtual = String.format("#%06X", 0xFFFFFF & cor);
        CorUtil.mostrar(findViewById(R.id.layoutCor), hexAtual);
    }

    // Guarda a cor selecionada na BD (se ainda não existir)
    private void guardarCor() {
        String hex = hexAtual; // cópia: a cor pode mudar enquanto a BD responde

        corRepository.obterPorHex(hex, existente -> {
            if (existente != null) {
                naUi(() -> toast("Esta cor já está guardada."));
                return;
            }
            // AJUSTA ao construtor da tua entidade Cor
            corRepository.inserir(new Cor(hex, false), id -> {
                naUi(() -> toast("Cor guardada!"));
                carregarCores();
            });
        });
    }

    private void carregarCores() {
        corRepository.obterTodas(lista -> naUi(() -> {
            adapterGuardadas.setLista(lista);
            tvCoresGuardadas.setText(lista == null || lista.isEmpty()
                    ? "Cores guardadas: nenhuma ainda" : "Cores guardadas");
        }));
    }

    // Os callbacks dos repositories correm em thread de fundo
    private void naUi(Runnable acao) {
        if (!isFinishing() && !isDestroyed()) {
            runOnUiThread(acao);
        }
    }

    private void toast(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}