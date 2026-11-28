package pt.ubi.di.pdm.modadebolsa.ui;

import android.content.Context;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Consumer;

public final class Dialogos {
    private Dialogos() {
    }

    public static void pedirTexto(Context context, String titulo, String hint,
                                  String textoInicial, int inputType,
                                  Consumer<String> aoConfirmar) {
        EditText input = new EditText(context);
        input.setHint(hint);
        input.setSingleLine(true);
        input.setInputType(inputType);
        input.setText(textoInicial);
        input.setSelection(input.getText().length());

        int margem = (int) (24 * context.getResources().getDisplayMetrics().density);
        FrameLayout container = new FrameLayout(context);
        container.setPadding(margem, margem / 2, margem, 0);
        container.addView(input);

        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setView(container)
                .setPositiveButton("OK", (dialog, which) -> {
                    String texto = input.getText().toString().trim();
                    if (!texto.isEmpty()) {
                        aoConfirmar.accept(texto);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public static void confirmar(Context context, String titulo, String mensagem,
                                 Runnable aoConfirmar) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("Eliminar", (dialog, which) -> aoConfirmar.run())
                .setNegativeButton("Cancelar", null)
                .show();
    }
}