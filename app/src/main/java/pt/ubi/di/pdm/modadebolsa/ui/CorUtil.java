package pt.ubi.di.pdm.modadebolsa.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import pt.ubi.di.pdm.modadebolsa.R;

// Preenche um layout_cor (círculo + código hex)
public final class CorUtil {

    private CorUtil() {
    }

    // "raiz" é a view que contém o layout_cor (ou o próprio layout_cor)
    public static void mostrar(View raiz, String hex) {
        View circulo = raiz.findViewById(R.id.viewCor);
        ((TextView) raiz.findViewById(R.id.tvHex)).setText(hex);

        try {
            circulo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hex)));
        } catch (IllegalArgumentException | NullPointerException e) {
            // hex inválido: volta à cor por defeito (importante nas views reutilizadas)
            circulo.setBackgroundTintList(null);
        }
    }
}