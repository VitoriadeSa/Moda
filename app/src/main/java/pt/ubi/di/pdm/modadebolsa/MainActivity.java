package pt.ubi.di.pdm.modadebolsa;
import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.ui.CategoriaAdapter;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CategoriaAdapter adapter = new CategoriaAdapter(categoria -> {
            // TODO: abrir a lista de moldes
        });
        binding.rvCategorias.setAdapter(adapter);

        AppDatabase.getInstance(this)
                .categoriaMoldeDao()
                .getTodas()
                .observe(this, adapter::atualizar);
    }
}