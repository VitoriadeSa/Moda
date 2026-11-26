package pt.ubi.di.pdm.modadebolsa;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.CaracteristicasTecido;
import pt.ubi.di.pdm.modadebolsa.data.Tecido;
import pt.ubi.di.pdm.modadebolsa.data.repository.RepositoryProvider;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityTecidoBinding;
import pt.ubi.di.pdm.modadebolsa.databinding.ItemCaracteristicaBinding;
import pt.ubi.di.pdm.modadebolsa.ui.ImagemUtil;

/** Página de detalhe de um tecido: imagem, nome e ficha de características. */
public class TecidoActivity extends AppCompatActivity {

    public static final String EXTRA_ID_TECIDO = "idTecido";

    private ActivityTecidoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTecidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnVoltar.setOnClickListener(v -> finish());

        int idTecido = getIntent().getIntExtra(EXTRA_ID_TECIDO, -1);
        if (idTecido == -1) {
            finish();
            return;
        }

        RepositoryProvider repos = new RepositoryProvider(this);

        // 1.º o tecido, 2.º a ficha de características a que ele aponta
        repos.getTecidoRepository().obterPorId(idTecido, tecido -> {
            if (tecido == null) {
                runOnUiThread(this::finish);
                return;
            }
            repos.getCaracteristicasTecidoRepository().obterPorId(
                    tecido.idCaracteristicasTecido,
                    caracteristicas -> runOnUiThread(() -> mostrar(tecido, caracteristicas)));
        });
    }

    private void mostrar(Tecido tecido, CaracteristicasTecido c) {
        if (isFinishing() || isDestroyed()) return;

        binding.tvNomeTecido.setText(tecido.nome);
        ImagemUtil.carregar(binding.ivTecido, tecido.pathImagem, R.drawable.bg_placeholder_tecido);

        // Ordem igual à do wireframe
        linha(binding.rowComposicao, R.string.filtro_composicao, c == null ? null : c.composicao);
        linha(binding.rowDurabilidade, R.string.filtro_durabilidade, c == null ? null : c.durabilidade);
        linha(binding.rowElasticidade, R.string.filtro_elasticidade, c == null ? null : c.elasticidade);
        linha(binding.rowAmarrotabilidade, R.string.filtro_amarrotabilidade, c == null ? null : c.amarrotabilidade);
        linha(binding.rowTransparencia, R.string.filtro_transparencia, c == null ? null : c.transparencia);
        linha(binding.rowCaimento, R.string.filtro_caimento, c == null ? null : c.caimento);
    }

    private void linha(ItemCaracteristicaBinding row, int rotuloRes, String valor) {
        row.tvRotulo.setText(rotuloRes);
        row.tvValor.setText(valor == null || valor.isEmpty() ? "—" : valor);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
