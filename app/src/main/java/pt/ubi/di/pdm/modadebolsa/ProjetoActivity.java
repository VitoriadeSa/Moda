package pt.ubi.di.pdm.modadebolsa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pt.ubi.di.pdm.modadebolsa.databinding.ActivityProjetoBinding;
import pt.ubi.di.pdm.modadebolsa.ui.CorUtil;
import pt.ubi.di.pdm.modadebolsa.ui.ListaAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.MoldeAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;
import pt.ubi.di.pdm.modadebolsa.ui.TecidoAdapter;
import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.Cor;
import pt.ubi.di.pdm.modadebolsa.data.Item;
import pt.ubi.di.pdm.modadebolsa.data.Projeto;
import pt.ubi.di.pdm.modadebolsa.data.Utilizador;
import pt.ubi.di.pdm.modadebolsa.data.repository.EquipaRepository;
import pt.ubi.di.pdm.modadebolsa.data.repository.ItemRepository;
import pt.ubi.di.pdm.modadebolsa.data.repository.ProjetoRepository;
import pt.ubi.di.pdm.modadebolsa.data.repository.UtilizadorRepository;
import pt.ubi.di.pdm.modadebolsa.notifications.LembreteScheduler;
import pt.ubi.di.pdm.modadebolsa.notifications.NotificacaoHelper;
import pt.ubi.di.pdm.modadebolsa.notifications.PermissaoNotificacoes;
import pt.ubi.di.pdm.modadebolsa.ui.Dialogos;

public class ProjetoActivity extends AppCompatActivity {

    public static final String EXTRA_ID_PROJETO = "idProjeto";

    // Forma de abrir este ecrã: startActivity(ProjetoActivity.novaIntent(this, idProjeto))
    public static Intent novaIntent(Context context, int idProjeto) {
        return new Intent(context, ProjetoActivity.class).putExtra(EXTRA_ID_PROJETO, idProjeto);
    }

    private static final int TEXTO =
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
    private static final int EMAIL =
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

    private @NonNull ActivityProjetoBinding binding;
    private int idProjeto;
    private volatile Projeto projeto; // preenchido em thread de fundo

    private ProjetoRepository projetoRepository;
    private ItemRepository itemRepository;
    private EquipaRepository equipaRepository;
    private UtilizadorRepository utilizadorRepository;
    private PermissaoNotificacoes permissaoNotificacoes;

    private TextView tvTitulo, tvDataEntrega, tvContagemDias;
    private TextView tvSemMoldes, tvSemTecidos, tvSemCores;

    // Adapters (os três simples usam o ListaAdapter genérico)
    private final ListaAdapter<Item> adapterPorFazer =
            new ListaAdapter<>(R.layout.item_tarefa, this::ligarTarefa);
    private final ListaAdapter<Item> adapterFeito =
            new ListaAdapter<>(R.layout.item_tarefa, this::ligarTarefa);
    private final ListaAdapter<Utilizador> adapterMembros =
            new ListaAdapter<>(R.layout.item_membro, (v, u) ->
                    ((TextView) v.findViewById(R.id.tvEmailMembro)).setText(u.email));
    private final ListaAdapter<Cor> adapterCores =
            new ListaAdapter<>(R.layout.layout_cor, this::ligarCor);
    private MoldeAdapter moldeAdapter;
    private TecidoAdapter tecidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjetoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_colaboracao);

        idProjeto = getIntent().getIntExtra(EXTRA_ID_PROJETO, -1);
        if (idProjeto == -1) {
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        projetoRepository = new ProjetoRepository(db.projetoDao());
        itemRepository = new ItemRepository(db.itemDao());
        equipaRepository = new EquipaRepository(db.equipaDao());
        utilizadorRepository = new UtilizadorRepository(db.utilizadorDao());

        // Tem de ser criada no onCreate (usa registerForActivityResult)
        permissaoNotificacoes = new PermissaoNotificacoes(this);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDataEntrega = findViewById(R.id.tvDataEntrega);
        tvContagemDias = findViewById(R.id.tvContagemDias);
        tvSemMoldes = findViewById(R.id.tvSemMoldes);
        tvSemTecidos = findViewById(R.id.tvSemTecidos);
        tvSemCores = findViewById(R.id.tvSemCores);

        moldeAdapter = new MoldeAdapter(molde -> {
            Intent intent = new Intent(this, MoldeActivity.class);
            intent.putExtra("idMolde", molde.idMolde);              // mesma chave que a MoldeActivity lê
            startActivity(intent);
        });
        tecidoAdapter = new TecidoAdapter(tecido -> {
            Intent intent = new Intent(this, TecidoActivity.class);
            intent.putExtra("idTecido", tecido.idTecido);           // mesma chave que a TecidoActivity lê
            startActivity(intent);
        });

        configurarLista(R.id.rvPorFazer, adapterPorFazer);
        configurarLista(R.id.rvFeito, adapterFeito);
        configurarLista(R.id.rvMembros, adapterMembros);
        configurarLista(R.id.rvCores, adapterCores);
        configurarLista(R.id.rvMoldes, moldeAdapter);
        configurarLista(R.id.rvTecidos, tecidoAdapter);

        findViewById(R.id.cardDataEntrega).setOnClickListener(v -> escolherData());
        findViewById(R.id.btnAdicionarPorFazer).setOnClickListener(v -> adicionarItem(false));
        findViewById(R.id.btnAdicionarFeito).setOnClickListener(v -> adicionarItem(true));
        findViewById(R.id.btnAdicionarMembro).setOnClickListener(v -> adicionarMembro());

    }

    // Recarrega sempre que o ecrã volta a ficar visível
    @Override
    protected void onResume() {
        super.onResume();
        carregar();
    }

    // ---------- Carregar dados ----------

    private void carregar() {
        projetoRepository.obterPorId(idProjeto, p -> {
            if (p == null) {
                naUi(this::finish); // o projeto foi eliminado
                return;
            }
            projeto = p;
            naUi(this::mostrarCabecalho);

            carregarItens();
            carregarMembros();

            projetoRepository.obterMoldes(idProjeto, l -> naUi(() -> {
                moldeAdapter.definirLista(l);
                alternarVazio(l, tvSemMoldes,
                        findViewById(R.id.tvSetaEsqMoldes), findViewById(R.id.tvSetaDirMoldes));
            }));
            projetoRepository.obterTecidos(idProjeto, l -> naUi(() -> {
                tecidoAdapter.definirLista(l);
                alternarVazio(l, tvSemTecidos,
                        findViewById(R.id.tvSetaEsqTecidos), findViewById(R.id.tvSetaDirTecidos));
            }));
            projetoRepository.obterCores(idProjeto, l -> naUi(() -> {
                adapterCores.setLista(l);
                alternarVazio(l, tvSemCores);
            }));
        });
    }

    private void carregarItens() {
        itemRepository.obterPorProjeto(idProjeto, itens -> {
            List<Item> porFazer = new ArrayList<>();
            List<Item> feito = new ArrayList<>();
            for (Item i : itens) {
                (i.concluido ? feito : porFazer).add(i);
            }
            naUi(() -> {
                adapterPorFazer.setLista(porFazer);
                adapterFeito.setLista(feito);
            });
        });
    }

    // Os membros do projeto são os membros da equipa a que ele pertence
    private void carregarMembros() {
        equipaRepository.obterMembros(projeto.idEquipa,
                m -> naUi(() -> adapterMembros.setLista(m)));
    }

    // ---------- Cabeçalho: título, data e contagem de dias ----------

    private void mostrarCabecalho() {
        tvTitulo.setText(projeto.nome);
        mostrarData();
    }

    private void mostrarData() {
        if (projeto.dataEntrega <= 0) {
            tvDataEntrega.setText("Definir");
            tvContagemDias.setText("Define uma data de entrega para receberes lembretes.");
            return;
        }

        tvDataEntrega.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(projeto.dataEntrega)));

        int dias = NotificacaoHelper.diasRestantes(projeto.dataEntrega);
        String texto;
        if (dias > 1) {
            texto = "Faltam " + dias + " dias até à entrega. Mãos à obra!";
        } else if (dias == 1) {
            texto = "Falta 1 dia até à entrega. Mãos à obra!";
        } else if (dias == 0) {
            texto = "A entrega é hoje! Mãos à obra!";
        } else {
            texto = "A data de entrega já passou.";
        }
        tvContagemDias.setText(texto);
    }

    private void escolherData() {
        if (projeto == null) {
            return;
        }

        Calendar inicial = Calendar.getInstance();
        if (projeto.dataEntrega > 0) {
            inicial.setTimeInMillis(projeto.dataEntrega);
        }

        new DatePickerDialog(this, (picker, ano, mes, dia) -> {
            // Meia-noite do dia escolhido, no fuso horário do telemóvel
            Calendar escolhida = Calendar.getInstance();
            escolhida.clear();
            escolhida.set(ano, mes, dia);
            projeto.dataEntrega = escolhida.getTimeInMillis();

            projetoRepository.atualizarDataProjeto(idProjeto, projeto.dataEntrega, r -> naUi(() -> {
                mostrarData();
                // Com data definida, passam a fazer sentido os lembretes
                permissaoNotificacoes.pedirSeNecessario(null);
                LembreteScheduler.agendar(this);
            }));
        }, inicial.get(Calendar.YEAR), inicial.get(Calendar.MONTH),
                inicial.get(Calendar.DAY_OF_MONTH)).show();

    }

    // ---------- Progresso (itens) ----------

    private void adicionarItem(boolean concluido) {
        Dialogos.pedirTexto(this, "Novo item", "Descrição", "", TEXTO, texto ->
                itemRepository.inserir(new Item(idProjeto, texto, concluido),
                        id -> carregarItens()));
    }

    // Toque: passa o item de "Por fazer" para "Feito" (e vice-versa).
    // Toque longo: elimina.
    private void ligarTarefa(View v, Item item) {
        ((TextView) v.findViewById(R.id.tvConteudoItem)).setText(item.conteudo);

        v.setOnClickListener(x -> itemRepository.atualizarConcluido(
                item.idItem, !item.concluido, r -> carregarItens()));

        v.setOnLongClickListener(x -> {
            Dialogos.confirmar(this, "Eliminar item",
                    "Queres eliminar \"" + item.conteudo + "\"?",
                    () -> itemRepository.eliminar(item, r -> carregarItens()));
            return true;
        });
    }

    // ---------- Membros ----------

    private void adicionarMembro() {
        if (projeto == null) {
            return;
        }
        /*
        Intent envio = new Intent(Intent.ACTION_SEND);
        envio.setType("text/plain");
        envio.putExtra(Intent.EXTRA_TEXT, "Junta-te à equipa \"" + nome + "\" na Moda de Bolsa: " + link);
        startActivity(Intent.createChooser(envio, "Convidar para a equipa"));
        */

        Dialogos.pedirTexto(this, "Adicionar membro", "Email do utilizador", "", EMAIL, email ->
                utilizadorRepository.obterPorEmail(email, u -> {
                    if (u == null) {
                        naUi(() -> toast("Não existe nenhum utilizador com esse email."));
                        return;
                    }
                    equipaRepository.adicionarMembro(u.idUtilizador, projeto.idEquipa,
                            id -> naUi(() -> {
                                toast(id == -1 ? "Já é membro da equipa." : "Membro adicionado.");
                                carregarMembros();
                            }));
                }));


    }

    // ---------- Cores ----------

    private void ligarCor(View v, Cor cor) {
        CorUtil.mostrar(v, cor.codigoHex);
    }


    // ---------- Auxiliares ----------

    private void configurarLista(int id, RecyclerView.Adapter<?> adapter) {
        ((RecyclerView) findViewById(id)).setAdapter(adapter);
    }

    // Mostra a mensagem de lista vazia e esconde as setas (ou o contrário)
    private void alternarVazio(List<?> lista, View vazio, View... setas) {
        boolean vazia = lista == null || lista.isEmpty();
        vazio.setVisibility(vazia ? View.VISIBLE : View.GONE);
        for (View seta : setas) {
            seta.setVisibility(vazia ? View.GONE : View.VISIBLE);
        }
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
}