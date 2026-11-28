package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.Projeto;
import pt.ubi.di.pdm.modadebolsa.data.repository.ProjetoRepository;
import pt.ubi.di.pdm.modadebolsa.data.repository.RepositoryProvider;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityListaProjetosBinding;
import pt.ubi.di.pdm.modadebolsa.ui.ProjetoAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;
import pt.ubi.di.pdm.modadebolsa.data.repository.*;
import pt.ubi.di.pdm.modadebolsa.data.*;


public class ListaProjetosActivity extends AppCompatActivity {

    // Onde se guarda o utilizador autenticado.
    // Enquanto não existir login, o valor é -1 e a lista mostra todos os projetos.

    public static final String EXTRA_ID_PROJETO = "idProjeto";
    private @NonNull ActivityListaProjetosBinding binding;

    private ProjetoRepository projetoRepository;
    private ProjetoAdapter adapter;
    private int idEquipa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaProjetosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idEquipa = getIntent().getIntExtra(
                ColaboracaoActivity.EXTRA_ID_EQUIPA,
                -1
        );

        if (idEquipa == -1) {
            Toast.makeText(this, "Não foi possível identificar a equipa.",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_colaboracao);

        projetoRepository = new RepositoryProvider(this).getProjetoRepository();

        adapter = new ProjetoAdapter(

                // Clique num projeto
                projeto -> {
                    Intent i = new Intent(this, ProjetoActivity.class);
                    i.putExtra(EXTRA_ID_PROJETO, projeto.idProjeto);
                    startActivity(i);

                    overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );
                },

                // Menu: editar e eliminar
                new ProjetoAdapter.OnProjetoMenuListener() {

                    @Override
                    public void onEditar(Projeto projeto) {
                        mostrarDialogoEditarProjeto(projeto);
                    }

                    @Override
                    public void onEliminar(Projeto projeto) {
                        confirmarEliminarProjeto(projeto);
                    }
                }
        );

        binding.rvProjetos.setAdapter(adapter);

        // Botão "+" para criar novo projeto
        View.OnClickListener novoProjeto = v -> mostrarDialogoNovoProjeto();
        binding.fabNovoProjeto.setOnClickListener(
                v -> mostrarDialogoNovoProjeto()
        );
    }

    // Recarrega sempre que o ecrã volta a ficar visível
    @Override
    protected void onResume() {
        super.onResume();
        carregarProjetos();
    }


    // ---------- Carregar a lista ----------

    private void carregarProjetos() {
        ProjetoRepository.OnListaProjetosCallback callback =
                lista -> runOnUiThread(() -> mostrarProjetos(lista));


        projetoRepository.obterPorEquipa(idEquipa, callback);
    }

    private void mostrarProjetos(List<Projeto> lista) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        adapter.setProjetos(lista);
        binding.tvVazio.setVisibility(
                lista == null || lista.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    // ---------- Novo projeto ----------

    private void mostrarDialogoNovoProjeto() {
        EditText input = criarCampoNome("");

        new AlertDialog.Builder(this)
                .setTitle("Novo projeto")
                .setView(envolver(input))
                .setPositiveButton("Criar", (dialog, which) -> {
                    String nome = input.getText().toString().trim();
                    if (nome.isEmpty()) {
                        Toast.makeText(this, "O nome não pode estar vazio.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    criarProjeto(nome);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void criarProjeto(String nome) {
        projetoRepository.inserir(new Projeto(idEquipa, nome), id -> {
                carregarProjetos();
        });
    }


    private void mostrarDialogoEditarProjeto(Projeto projeto) {
        EditText input = criarCampoNome(projeto.nome);

        new AlertDialog.Builder(this)
                .setTitle("Editar nome")
                .setView(envolver(input))
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nome = input.getText().toString().trim();
                    if (nome.isEmpty()) {
                        Toast.makeText(this, "O nome não pode estar vazio.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    projetoRepository.atualizarNome(projeto.idProjeto, nome, r -> carregarProjetos());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminarProjeto(Projeto projeto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar projeto")
                .setMessage("Queres eliminar \"" + projeto.nome + "\"? " +
                        "O progresso deste projeto também será eliminado.")
                .setPositiveButton("Eliminar", (dialog, which) ->
                        projetoRepository.eliminar(projeto, r -> carregarProjetos()))
                .setNegativeButton("Cancelar", null)
                .show();
    }


    // ---------- Auxiliares dos diálogos ----------

    private EditText criarCampoNome(String textoInicial) {
        EditText input = new EditText(this);
        input.setHint("Nome do projeto");
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
