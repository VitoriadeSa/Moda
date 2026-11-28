package pt.ubi.di.pdm.modadebolsa.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaAdapter<T> extends RecyclerView.Adapter<ListaAdapter.VH> {
    public interface Binder<T> {
        void ligar(View itemView, T item);
    }

    private final List<T> itens = new ArrayList<>();
    private final int layoutRes;
    private final Binder<T> binder;

    public ListaAdapter(@LayoutRes int layoutRes, Binder<T> binder) {
        this.layoutRes = layoutRes;
        this.binder = binder;
    }

    public void setLista(List<T> novos) {
        itens.clear();
        if (novos != null) {
            itens.addAll(novos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        binder.ligar(holder.itemView, itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}