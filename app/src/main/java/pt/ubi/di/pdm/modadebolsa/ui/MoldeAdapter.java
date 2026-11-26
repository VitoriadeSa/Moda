package pt.ubi.di.pdm.modadebolsa.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.ubi.di.pdm.modadebolsa.R;
import pt.ubi.di.pdm.modadebolsa.data.Molde;

public class MoldeAdapter extends RecyclerView.Adapter<MoldeAdapter.VH> {

    public interface OnMoldeClick { void onClick(Molde molde); }

    private List<Molde> todos = new ArrayList<>();
    private List<Molde> visiveis = new ArrayList<>();
    private final OnMoldeClick listener;

    public MoldeAdapter(OnMoldeClick listener) { this.listener = listener; }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvNomeMolde;
        final ImageView ivMolde;

        VH(View v) {
            super(v);
            tvNomeMolde = v.findViewById(R.id.tvNomeMolde);
            ivMolde = v.findViewById(R.id.ivMolde);
        }
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_molde, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Molde m = visiveis.get(position);
        holder.tvNomeMolde.setText(m.nomeMolde);
        ImagemUtil.carregar(holder.ivMolde, m.pathImagem);
        holder.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override
    public int getItemCount() { return visiveis.size(); }

    public void definirLista(List<Molde> lista) {
        todos = new ArrayList<>(lista);
        visiveis = new ArrayList<>(lista);
        notifyDataSetChanged();
    }

    public void filtrar(String texto) {
        String q = texto == null ? "" : texto.trim().toLowerCase(Locale.getDefault());
        visiveis = new ArrayList<>();
        for (Molde m : todos) {
            if (q.isEmpty() || m.nomeMolde.toLowerCase(Locale.getDefault()).contains(q)) {
                visiveis.add(m);
            }
        }
        notifyDataSetChanged();
    }
}