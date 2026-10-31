package pt.ubi.di.pdm.modadebolsa.ui;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.ubi.di.pdm.modadebolsa.CirculoCoresActivity;
import pt.ubi.di.pdm.modadebolsa.ListaTecidosActivity;
import pt.ubi.di.pdm.modadebolsa.MainActivity;
import pt.ubi.di.pdm.modadebolsa.R;

public class NavegacaoInferior {

    /** @param selecionado R.id.nav_moldes, R.id.nav_tecidos ou R.id.nav_outros */
    public static void configurar(Activity activity, BottomNavigationView nav, int selecionado) {
        nav.setSelectedItemId(selecionado);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selecionado) return true;

            Class<?> destino;
            if (id == R.id.nav_moldes)       destino = MainActivity.class;
            else if (id == R.id.nav_tecidos) destino = ListaTecidosActivity.class;
            else                             destino = CirculoCoresActivity.class;

            Intent i = new Intent(activity, destino);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(i);
            activity.overridePendingTransition(0, 0);
            return true;
        });

        nav.setOnItemReselectedListener(item -> { /* TO DO */ });
    }
}