package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityMainBinding;
import pt.ubi.di.pdm.modadebolsa.ui.CategoriaAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;
=======
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import pt.ubi.di.pdm.modadebolsa.databinding.ActivityMainBinding;
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

<<<<<<< HEAD
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
=======
        // Configuração direta do BottomNavigationView sem depender da classe auxiliar que bloqueia os cliques
        if (binding.bottomNav != null) {
            binding.bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_moldes) {
                    startActivity(new Intent(this, ListaMoldesActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
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

        if (binding.ivLogoTop != null) {
            binding.ivLogoTop.setOnClickListener(v -> {
                // Já está na main
            });
        }

        if (binding.tvMoldeRecente != null) {
            binding.tvMoldeRecente.setOnClickListener(v -> {
                startActivity(new Intent(this, MoldeActivity.class));
                overridePendingTransition(0, 0);
                finish();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarPaletaRecente();
    }

    private void carregarPaletaRecente() {
        SharedPreferences prefs = getSharedPreferences("ModaDeBolsa_Paletas", MODE_PRIVATE);
        String dados = prefs.getString("paletas_salvas", "");

        if (dados.isEmpty()) {
            binding.tvPaletaRecente.setText("Nenhuma paleta recente guardada");
            return;
        }

        String[] itens = dados.split("\\|");
        if (itens.length > 0 && !itens[0].isEmpty()) {
            String[] partes = itens[0].split(";");
            if (partes.length == 6) {
                try {
                    int c1 = Integer.parseInt(partes[0]);
                    String h1 = partes[1];
                    int c2 = Integer.parseInt(partes[2]);
                    String h2 = partes[3];
                    int c3 = Integer.parseInt(partes[4]);
                    String h3 = partes[5];

                    String textoBase = "[V1] " + h1 + "    [V2] " + h2 + "    [V3] " + h3;
                    SpannableString spannable = new SpannableString(textoBase);

                    substituirTagPorVetor(spannable, textoBase, "[V1]", c1);
                    substituirTagPorVetor(spannable, textoBase, "[V2]", c2);
                    substituirTagPorVetor(spannable, textoBase, "[V3]", c3);

                    binding.tvPaletaRecente.setText(spannable);
                    return;
                } catch (Exception ignored) {}
            }
        }

        binding.tvPaletaRecente.setText("Nenhuma paleta recente guardada");
    }

    private void substituirTagPorVetor(SpannableString spannable, String textoStr, String tag, int cor) {
        int index = textoStr.indexOf(tag);
        while (index != -1) {
            Drawable vectorDrawable = ContextCompat.getDrawable(this, R.drawable.ic_paleta_circulo);
            if (vectorDrawable != null) {
                Drawable wrappedDrawable = DrawableCompat.wrap(vectorDrawable);
                DrawableCompat.setTint(wrappedDrawable, cor);
                wrappedDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

                ImageSpan imageSpan = new ImageSpan(wrappedDrawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(imageSpan, index, index + tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            index = textoStr.indexOf(tag, index + 1);
        }
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
<<<<<<< HEAD

=======
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
}