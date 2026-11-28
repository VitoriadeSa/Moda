package pt.ubi.di.pdm.modadebolsa.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import pt.ubi.di.pdm.modadebolsa.R;

public final class CorUtil {
    private CorUtil() {
    }

    public static void mostrar(View raiz, String hex) {
        View circulo = raiz.findViewById(R.id.viewCor);
        ((TextView) raiz.findViewById(R.id.tvHex)).setText(hex);
        try {
            circulo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hex)));
        } catch (IllegalArgumentException | NullPointerException e) {
            circulo.setBackgroundTintList(null);
        }
    }
}