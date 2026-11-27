
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
import pt.ubi.di.pdm.modadebolsa.data.Equipa;

public class EquipaAdapter extends RecyclerView.Adapter<EquipaAdapter.EquipaViewHolder> {

    // Listener para o clique numa equipa
    public interface OnEquipaClick {
        void onClick(Equipa equipa);
    }

    // Listener para editar e eliminar
    public interface OnEquipaMenuListener {
        void onEditar(Equipa equipa);
        void onEliminar(Equipa equipa);
    }

    private static final int MENU_EDITAR = 1;
    private static final int MENU_ELIMINAR = 2;

    private final List<Equipa> equipas = new ArrayList<>();

    private final OnEquipaClick clickListener;
    private final OnEquipaMenuListener menuListener;

    public EquipaAdapter(
            OnEquipaClick clickListener,
            OnEquipaMenuListener menuListener
    ) {
        this.clickListener = clickListener;
        this.menuListener = menuListener;
    }

    // Substitui a lista apresentada
    public void setEquipas(List<Equipa> novas) {
        equipas.clear();

        if (novas != null) {
            equipas.addAll(novas);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EquipaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipa, parent, false);

        return new EquipaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EquipaViewHolder holder,
            int position
    ) {
        Equipa equipa = equipas.get(position);

        holder.tvNomeEquipa.setText(equipa.nome);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(equipa);
            }
        });

        holder.btnMenuEquipa.setOnClickListener(v ->
                mostrarMenu(v, equipa)
        );
    }

    @Override
    public int getItemCount() {
        return equipas.size();
    }

    // Menu dos três pontos
    private void mostrarMenu(View ancora, Equipa equipa) {
        PopupMenu popup = new PopupMenu(ancora.getContext(), ancora);

        popup.getMenu().add(0, MENU_EDITAR, 0, "Editar nome");
        popup.getMenu().add(0, MENU_ELIMINAR, 1, "Eliminar");

        popup.setOnMenuItemClickListener(item -> {

            if (menuListener == null) {
                return false;
            }

            if (item.getItemId() == MENU_EDITAR) {
                menuListener.onEditar(equipa);
                return true;
            }

            if (item.getItemId() == MENU_ELIMINAR) {
                menuListener.onEliminar(equipa);
                return true;
            }

            return false;
        });

        popup.show();
    }

    static class EquipaViewHolder extends RecyclerView.ViewHolder {

        final TextView tvNomeEquipa;
        final ImageView btnMenuEquipa;

        EquipaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomeEquipa = itemView.findViewById(R.id.tvNomeEquipa);
            btnMenuEquipa = itemView.findViewById(R.id.btnMenuEquipa);
        }
    }
}
