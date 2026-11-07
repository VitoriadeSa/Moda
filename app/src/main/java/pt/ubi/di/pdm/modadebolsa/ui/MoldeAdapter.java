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
<<<<<<< HEAD
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
=======

import pt.ubi.di.pdm.modadebolsa.R;
// Importa a tua entidade Room do molde quando estiver pronta, ex:
// import pt.ubi.di.pdm.modadebolsa.data.MoldeEntity;

public class MoldeAdapter extends RecyclerView.Adapter<MoldeAdapter.MoldeViewHolder> {

    // Substitui Object pela tua classe de dados/entidade do Molde (ex: MoldeEntity)
    private List<Object> listaMoldes = new ArrayList<>();
    private OnMoldeClickListener listener;

    public interface OnMoldeClickListener {
        void onMoldeClick(Object molde);
    }

    public MoldeAdapter(OnMoldeClickListener listener) {
        this.listener = listener;
    }

    public void setMoldes(List<Object> moldes) {
        this.listaMoldes = moldes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoldeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_molde, parent, false);
        return new MoldeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoldeViewHolder holder, int position) {
        Object molde = listaMoldes.get(position);

        // Exemplo de atribuição de dados (ajusta conforme os campos da tua entidade Room):
        // holder.tvNomeMolde.setText(molde.getNome());
        holder.tvNomeMolde.setText("Molde Exemplo " + (position + 1));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoldeClick(molde);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMoldes.size();
    }

    static class MoldeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeMolde;
        ImageView ivMoldeIcon;

        public MoldeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeMolde = itemView.findViewById(R.id.tvNomeMolde);
            ivMoldeIcon = itemView.findViewById(R.id.ivMoldeIcon);
        }
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
    }
}