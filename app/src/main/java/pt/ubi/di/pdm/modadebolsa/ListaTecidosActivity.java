package pt.ubi.di.pdm.modadebolsa;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.Tecido;
import pt.ubi.di.pdm.modadebolsa.data.ValoresTecido;
import pt.ubi.di.pdm.modadebolsa.data.repository.RepositoryProvider;
import pt.ubi.di.pdm.modadebolsa.data.repository.TecidoRepository;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityListaTecidosBinding;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;
import pt.ubi.di.pdm.modadebolsa.ui.TecidoAdapter;

public class ListaTecidosActivity extends AppCompatActivity {

    private static final int COR_FILTRO_ATIVO = 0xFF9B78C0;
    private static final int COR_FILTRO_INATIVO = 0xFF757575;

    private ActivityListaTecidosBinding binding;
    private TecidoRepository repositorio;
    private TecidoAdapter adapter;

    // Um grupo de chips (seleção única, opcional) por característica
    private ChipGroup cgComposicao, cgCaimento, cgElasticidade,
            cgTransparencia, cgAmarrotabilidade, cgDurabilidade;

    /** Só fica ativo enquanto o painel de filtros está aberto: o "voltar" fecha o painel. */
    private OnBackPressedCallback fecharPainelAoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaTecidosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_tecidos);

        repositorio = new RepositoryProvider(this).getTecidoRepository();

        adapter = new TecidoAdapter(tecido -> {
            Intent i = new Intent(this, TecidoActivity.class);
            i.putExtra(TecidoActivity.EXTRA_ID_TECIDO, tecido.idTecido);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        binding.rvTecidos.setAdapter(adapter);

        fecharPainelAoVoltar = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                definirPainelVisivel(false);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, fecharPainelAoVoltar);

        construirPainelFiltros();
        configurarPesquisa();

        // Primeira carga: sem filtros devolve todos os tecidos
        aplicarFiltros();
    }

    // ------------------------------------------------------------------
    // Pesquisa por nome (cliente) + filtros por características (BD)
    // ------------------------------------------------------------------

    private void configurarPesquisa() {
        binding.searchBar.etPesquisa.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                adapter.filtrar(s.toString());
                atualizarEstadoVazio();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        binding.searchBar.etPesquisa.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                esconderTeclado();
                return true;
            }
            return false;
        });

        binding.searchBar.btnFiltros.setOnClickListener(v ->
                definirPainelVisivel(binding.painelFiltros.getVisibility() != View.VISIBLE));

        binding.btnPesquisar.setOnClickListener(v -> {
            aplicarFiltros();
            definirPainelVisivel(false);
        });

        binding.btnLimpar.setOnClickListener(v -> {
            for (ChipGroup g : grupos()) g.clearCheck();
            aplicarFiltros();
            definirPainelVisivel(false);
        });
    }

    /** Vai à BD com os chips selecionados (null = sem filtro nessa característica). */
    private void aplicarFiltros() {
        String composicao = selecionado(cgComposicao);
        String caimento = selecionado(cgCaimento);
        String elasticidade = selecionado(cgElasticidade);
        String transparencia = selecionado(cgTransparencia);
        String amarrotabilidade = selecionado(cgAmarrotabilidade);
        String durabilidade = selecionado(cgDurabilidade);

        boolean haFiltros = composicao != null || caimento != null || elasticidade != null
                || transparencia != null || amarrotabilidade != null || durabilidade != null;
        // Ícone do filtro fica roxo quando há filtros ativos
        ImageViewCompat.setImageTintList(binding.searchBar.btnFiltros,
                ColorStateList.valueOf(haFiltros ? COR_FILTRO_ATIVO : COR_FILTRO_INATIVO));

        repositorio.filtrar(composicao, caimento, elasticidade,
                transparencia, amarrotabilidade, durabilidade,
                lista -> runOnUiThread(() -> mostrar(lista)));
    }

    private void mostrar(List<Tecido> lista) {
        if (isFinishing() || isDestroyed()) return;
        adapter.definirLista(lista);
        atualizarEstadoVazio();
    }

    private void atualizarEstadoVazio() {
        binding.tvVazio.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    // ------------------------------------------------------------------
    // Painel de filtros (chips gerados a partir de ValoresTecido)
    // ------------------------------------------------------------------

    private void construirPainelFiltros() {
        cgComposicao = adicionarGrupo(R.string.filtro_composicao, ValoresTecido.COMPOSICOES);
        cgCaimento = adicionarGrupo(R.string.filtro_caimento, ValoresTecido.CAIMENTOS);
        cgElasticidade = adicionarGrupo(R.string.filtro_elasticidade, ValoresTecido.NIVEIS);
        cgTransparencia = adicionarGrupo(R.string.filtro_transparencia, ValoresTecido.NIVEIS);
        cgAmarrotabilidade = adicionarGrupo(R.string.filtro_amarrotabilidade, ValoresTecido.NIVEIS);
        cgDurabilidade = adicionarGrupo(R.string.filtro_durabilidade, ValoresTecido.NIVEIS);
    }

    private ChipGroup adicionarGrupo(@StringRes int rotuloRes, String[] opcoes) {
        TextView rotulo = new TextView(this);
        rotulo.setText(rotuloRes);
        rotulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        rotulo.setTypeface(rotulo.getTypeface(), Typeface.BOLD);
        rotulo.setTextColor(ContextCompat.getColor(this, R.color.texto_primario));
        LinearLayout.LayoutParams lpRotulo = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpRotulo.topMargin = dp(12);
        binding.llFiltros.addView(rotulo, lpRotulo);

        ChipGroup grupo = new ChipGroup(this);
        grupo.setSingleSelection(true);
        grupo.setSelectionRequired(false);   // tocar outra vez no chip desmarca-o
        grupo.setChipSpacingHorizontal(dp(8));
        for (String opcao : opcoes) {
            Chip chip = (Chip) getLayoutInflater()
                    .inflate(R.layout.item_chip_filtro, grupo, false);
            chip.setText(opcao);
            grupo.addView(chip);
        }
        binding.llFiltros.addView(grupo, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return grupo;
    }

    private ChipGroup[] grupos() {
        return new ChipGroup[]{cgComposicao, cgCaimento, cgElasticidade,
                cgTransparencia, cgAmarrotabilidade, cgDurabilidade};
    }

    /** Texto do chip selecionado, ou null se nenhum. */
    private String selecionado(ChipGroup grupo) {
        int id = grupo.getCheckedChipId();
        if (id == View.NO_ID) return null;
        Chip chip = grupo.findViewById(id);
        return chip == null ? null : chip.getText().toString();
    }

    private void definirPainelVisivel(boolean visivel) {
        binding.painelFiltros.setVisibility(visivel ? View.VISIBLE : View.GONE);
        fecharPainelAoVoltar.setEnabled(visivel);
        if (visivel) esconderTeclado();
    }

    // ------------------------------------------------------------------
    // Utilitários
    // ------------------------------------------------------------------

    private void esconderTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
        binding.searchBar.etPesquisa.clearFocus();
    }

    private int dp(float valor) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, valor, getResources().getDisplayMetrics());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
