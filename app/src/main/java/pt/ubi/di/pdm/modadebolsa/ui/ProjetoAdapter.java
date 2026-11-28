package pt.ubi.di.pdm.modadebolsa.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ubi.di.pdm.modadebolsa.R;
import pt.ubi.di.pdm.modadebolsa.data.Projeto;


public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.ProjetoViewHolder> {

    // Listener para o clique num projeto
    public interface OnProjetoClick {
        void onClick(Projeto projeto);
    }

    // Listener para editar e eliminar
    public interface OnProjetoMenuListener {
        void onEditar(Projeto projeto);
        void onEliminar(Projeto projeto);
    }

    private static final int MENU_EDITAR = 1;
    private static final int MENU_ELIMINAR = 2;

    private final List<Projeto> projetos = new ArrayList<>();

    private final ProjetoAdapter.OnProjetoClick clickListener;
    private final ProjetoAdapter.OnProjetoMenuListener menuListener;

    public ProjetoAdapter(
            ProjetoAdapter.OnProjetoClick clickListener,
            ProjetoAdapter.OnProjetoMenuListener menuListener
    ) {
        this.clickListener = clickListener;
        this.menuListener = menuListener;
    }

    // Substitui a lista apresentada
    public void setProjetos(List<Projeto> novos) {
        projetos.clear();

        if (novos != null) {
            projetos.addAll(novos);
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProjetoAdapter.ProjetoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipa, parent, false);

        return new ProjetoAdapter.ProjetoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull ProjetoViewHolder holder,
            int position
    ) {
        Projeto projeto = projetos.get(position);

        holder.tvNomeProjeto.setText(projeto.nome);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(projeto);
            }
        });

        holder.btnMenuProjeto.setOnClickListener(v ->
                mostrarMenu(v, projeto)
        );
    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }

    // Menu dos três pontos
    private void mostrarMenu(View ancora, Projeto Projeto) {
        PopupMenu popup = new PopupMenu(ancora.getContext(), ancora);

        popup.getMenu().add(0, MENU_EDITAR, 0, "Editar nome");
        popup.getMenu().add(0, MENU_ELIMINAR, 1, "Eliminar");

        popup.setOnMenuItemClickListener(item -> {

            if (menuListener == null) {
                return false;
            }

            if (item.getItemId() == MENU_EDITAR) {
                menuListener.onEditar(Projeto);
                return true;
            }

            if (item.getItemId() == MENU_ELIMINAR) {
                menuListener.onEliminar(Projeto);
                return true;
            }

            return false;
        });

        popup.show();
    }

    static class ProjetoViewHolder extends RecyclerView.ViewHolder {

        final TextView tvNomeProjeto;
        final ImageView btnMenuProjeto;

        ProjetoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomeProjeto = itemView.findViewById(R.id.tvNomeEquipa);
            btnMenuProjeto = itemView.findViewById(R.id.btnMenuEquipa);
        }
    }

}
