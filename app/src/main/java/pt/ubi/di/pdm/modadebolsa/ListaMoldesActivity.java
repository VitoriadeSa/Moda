package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.Molde;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityListaMoldesBinding;
import pt.ubi.di.pdm.modadebolsa.ui.MoldeAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;

public class ListaMoldesActivity extends AppCompatActivity {

    public static final String EXTRA_ID_CATEGORIA = "idCategoria";
    public static final String EXTRA_NOME_CATEGORIA = "nomeCategoria";

    private ActivityListaMoldesBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaMoldesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_moldes);

        int idCategoria = getIntent().getIntExtra(EXTRA_ID_CATEGORIA, -1);
        String nomeCategoria = getIntent().getStringExtra(EXTRA_NOME_CATEGORIA);
        binding.tvTitulo.setText("Moldes de: " + nomeCategoria);

        MoldeAdapter adapter = new MoldeAdapter(molde -> {
            Intent i = new Intent(this, MoldeActivity.class);
            i.putExtra(MoldeActivity.EXTRA_ID_MOLDE, molde.idMolde);
            startActivity(i);
        });
        binding.rvMoldes.setAdapter(adapter);

        // Consulta à BD fora da thread principal
        executor.execute(() -> {
            List<Molde> moldes = AppDatabase.getInstance(this)
                    .moldeDao()
                    .obterPorCategoria(idCategoria);
            runOnUiThread(() -> adapter.definirLista(moldes));
        });


        EditText etPesquisa = findViewById(R.id.etPesquisa);
        etPesquisa.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                adapter.filtrar(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
=======
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListaMoldesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_moldes);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ListaMoldesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        ImageView ivLogoTop = findViewById(R.id.ivLogoTop);
        if (ivLogoTop != null) {
            ivLogoTop.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_moldes);

            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_moldes) {
                    return true;
                } else if (id == R.id.nav_tecidos) {
                    startActivity(new Intent(this, ListaTecidosActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.nav_outros) {
                    startActivity(new Intent(this, CirculoCoresActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            });
        }
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
    }
}