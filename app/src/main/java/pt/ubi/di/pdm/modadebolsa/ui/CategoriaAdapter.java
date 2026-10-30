package pt.ubi.di.pdm.modadebolsa.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.CategoriaMolde;
import pt.ubi.di.pdm.modadebolsa.databinding.ItemCategoriaBinding;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.VH> {

    public interface OnCategoriaClick {
        void onClick(CategoriaMolde categoria);
    }

    private List<CategoriaMolde> lista = new ArrayList<>();
    private final OnCategoriaClick listener;

    public CategoriaAdapter(OnCategoriaClick listener) {
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemCategoriaBinding b;
        VH(ItemCategoriaBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCategoriaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CategoriaMolde cat = lista.get(position);
        holder.b.tvNome.setText(cat.nomeCategoria);
        holder.b.getRoot().setOnClickListener(v -> listener.onClick(cat));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualizar(List<CategoriaMolde> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }
}