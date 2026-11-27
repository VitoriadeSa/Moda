package pt.ubi.di.pdm.modadebolsa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.repository.RepositoryProvider;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityColaboracaoBinding;
import pt.ubi.di.pdm.modadebolsa.ui.EquipaAdapter;
import pt.ubi.di.pdm.modadebolsa.data.Equipa;
import pt.ubi.di.pdm.modadebolsa.data.repository.EquipaRepository;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;

public class ColaboracaoActivity extends AppCompatActivity {

    // Onde se guarda o utilizador autenticado.
    // Enquanto não existir login, o valor é -1 e a lista mostra todas as equipas.
    private static final String PREFS_SESSAO = "sessao";
    private static final String CHAVE_ID_UTILIZADOR = "idUtilizador";
    public static final String EXTRA_ID_EQUIPA = "idEquipa";
    private @NonNull ActivityColaboracaoBinding binding;

    private EquipaRepository equipaRepository;
    private EquipaAdapter adapter;
    private int idUtilizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityColaboracaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_colaboracao);

        equipaRepository = new RepositoryProvider(this).getEquipaRepository();

        idUtilizador = getSharedPreferences(PREFS_SESSAO, Context.MODE_PRIVATE)
                .getInt(CHAVE_ID_UTILIZADOR, -1);

        adapter = new EquipaAdapter(

                // Clique numa equipa
                equipa -> {
                    Intent i = new Intent(this, ListaProjetosActivity.class);
                    i.putExtra(EXTRA_ID_EQUIPA, equipa.idEquipa);
                    startActivity(i);

                    overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );
                },

                // Menu: editar e eliminar
                new EquipaAdapter.OnEquipaMenuListener() {

                    @Override
                    public void onEditar(Equipa equipa) {
                        mostrarDialogoEditarEquipa(equipa);
                    }

                    @Override
                    public void onEliminar(Equipa equipa) {
                        confirmarEliminarEquipa(equipa);
                    }
                }
        );

        binding.rvEquipas.setAdapter(adapter);

        // Botão "+" para criar nova equipa
        View.OnClickListener novaEquipa = v -> mostrarDialogoNovaEquipa();
        binding.fabNovaEquipa.setOnClickListener(
                v -> mostrarDialogoNovaEquipa()
        );
    }

    // Recarrega sempre que o ecrã volta a ficar visível
    @Override
    protected void onResume() {
        super.onResume();
        carregarEquipas();
    }


    // ---------- Carregar a lista ----------

    private void carregarEquipas() {
        EquipaRepository.OnListaEquipasCallback callback =
                lista -> runOnUiThread(() -> mostrarEquipas(lista));

        if (idUtilizador != -1) {
            equipaRepository.obterPorUtilizador(idUtilizador, callback);
        } else {
            equipaRepository.obterTodas(callback);
        }
    }

    private void mostrarEquipas(List<Equipa> lista) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        adapter.setEquipas(lista);
        binding.tvVazio.setVisibility(
                lista == null || lista.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    // ---------- Nova equipa ----------

    private void mostrarDialogoNovaEquipa() {
        EditText input = criarCampoNome("");

        new AlertDialog.Builder(this)
                .setTitle("Nova equipa")
                .setView(envolver(input))
                .setPositiveButton("Criar", (dialog, which) -> {
                    String nome = input.getText().toString().trim();
                    if (nome.isEmpty()) {
                        Toast.makeText(this, "O nome não pode estar vazio.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    criarEquipa(nome);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void criarEquipa(String nome) {
        equipaRepository.inserir(new Equipa(nome), id -> {
            if (idUtilizador != -1) {
                // Quem cria a equipa passa a ser membro dela
                equipaRepository.adicionarMembro(idUtilizador, (int) id, r -> carregarEquipas());
            } else {
                carregarEquipas();
            }
        });
    }


    private void mostrarDialogoEditarEquipa(Equipa equipa) {
        EditText input = criarCampoNome(equipa.nome);

        new AlertDialog.Builder(this)
                .setTitle("Editar nome")
                .setView(envolver(input))
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nome = input.getText().toString().trim();
                    if (nome.isEmpty()) {
                        Toast.makeText(this, "O nome não pode estar vazio.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    equipaRepository.atualizarNome(equipa.idEquipa, nome, r -> carregarEquipas());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminarEquipa(Equipa equipa) {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar equipa")
                    .setMessage("Queres eliminar \"" + equipa.nome + "\"? " +
                            "Os projetos desta equipa também serão eliminados.")
                    .setPositiveButton("Eliminar", (dialog, which) ->
                            equipaRepository.eliminar(equipa, r -> carregarEquipas()))
                    .setNegativeButton("Cancelar", null)
                    .show();
    }


    // ---------- Auxiliares dos diálogos ----------

    private EditText criarCampoNome(String textoInicial) {
        EditText input = new EditText(this);
        input.setHint("Nome da equipa");
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setText(textoInicial);
        input.setSelection(input.getText().length());
        return input;
    }

    // Dá margem ao campo dentro do diálogo
    private FrameLayout envolver(View view) {
        int margem = (int) (24 * getResources().getDisplayMetrics().density);
        FrameLayout container = new FrameLayout(this);
        container.setPadding(margem, margem / 2, margem, 0);
        container.addView(view);
        return container;
    }
}
