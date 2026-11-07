package pt.ubi.di.pdm.modadebolsa;
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
public class TecidoActivity extends AppCompatActivity {


=======

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;

public class TecidoActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText etPesquisa;
    private ImageButton btnFiltros;
    private RecyclerView rvTecidos;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecido);

        // Inicializar a base de dados Room
        //db = AppDatabase.getDatabase(getApplicationContext());

        etPesquisa = findViewById(R.id.etPesquisa);
        btnFiltros = findViewById(R.id.btnFiltros);
        rvTecidos = findViewById(R.id.rvTecidos);
        bottomNav = findViewById(R.id.bottomNav);

        configurarPesquisa();

        btnFiltros.setOnClickListener(v -> {
            Toast.makeText(this, "Filtros clicados", Toast.LENGTH_SHORT).show();
            // TODO: Abrir BottomSheetDialog ou popup de filtros de tecido
        });

        configurarBottomNav();
    }

    private void configurarPesquisa() {
        etPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                filtrarTecidos(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filtrarTecidos(String query) {
        // TODO: Atualizar a lista do teu RecyclerViewAdapter com base no texto inserido
    }

    private void configurarBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_tecidos);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_tecidos) {
                return true;
            } else if (id == R.id.nav_moldes) {
                finish();
                return true;
            }
            return false;
        });
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
}
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
