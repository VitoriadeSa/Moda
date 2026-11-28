package pt.ubi.di.pdm.modadebolsa.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.ubi.di.pdm.modadebolsa.R;
import pt.ubi.di.pdm.modadebolsa.data.Tecido;
import pt.ubi.di.pdm.modadebolsa.databinding.ItemTecidoBinding;

public class TecidoAdapter extends RecyclerView.Adapter<TecidoAdapter.VH> {

    public interface OnTecidoClick { void onClick(Tecido tecido); }

    /** Resultado atual da BD (já com os filtros de características aplicados). */
    private List<Tecido> todos = new ArrayList<>();
    /** O que se vê: "todos" filtrado pelo texto da barra de pesquisa. */
    private List<Tecido> visiveis = new ArrayList<>();
    private String consulta = "";
    private final OnTecidoClick listener;

    public TecidoAdapter(OnTecidoClick listener) { this.listener = listener; }

    static class VH extends RecyclerView.ViewHolder {
        final ItemTecidoBinding b;

        VH(ItemTecidoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemTecidoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Tecido t = visiveis.get(position);
        holder.b.tvNomeTecido.setText(t.nome);
        ImagemUtil.carregar(holder.b.ivTecido, t.pathImagem, R.drawable.bg_placeholder_tecido);
        holder.itemView.setOnClickListener(v -> listener.onClick(t));
    }

    @Override
    public int getItemCount() { return visiveis.size(); }

    /** Substitui a lista (vinda da BD) e volta a aplicar o texto de pesquisa atual. */
    public void definirLista(List<Tecido> lista) {
        todos = lista == null ? new ArrayList<>() : new ArrayList<>(lista);
        aplicarConsulta();
    }

    /** Filtra por nome, ignorando maiúsculas e acentos ("la" encontra "Lã"). */
    public void filtrar(String texto) {
        consulta = normalizar(texto);
        aplicarConsulta();
    }

    private void aplicarConsulta() {
        visiveis = new ArrayList<>();
        for (Tecido t : todos) {
            if (consulta.isEmpty() || normalizar(t.nome).contains(consulta)) {
                visiveis.add(t);
            }
        }
        notifyDataSetChanged();
    }

    private static String normalizar(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
    }
}
