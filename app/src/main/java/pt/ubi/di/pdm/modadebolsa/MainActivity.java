package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityMainBinding;
import pt.ubi.di.pdm.modadebolsa.ui.CategoriaAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_moldes);

        CategoriaAdapter adapter = new CategoriaAdapter(categoria -> {
            Intent i = new Intent(this, ListaMoldesActivity.class);
            i.putExtra(ListaMoldesActivity.EXTRA_ID_CATEGORIA, categoria.idCategoria);
            i.putExtra(ListaMoldesActivity.EXTRA_NOME_CATEGORIA, categoria.nomeCategoria);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });
        binding.rvCategorias.setAdapter(adapter);

        AppDatabase.getInstance(this)
                .categoriaMoldeDao()
                .getTodas()
                .observe(this, adapter::atualizar);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}