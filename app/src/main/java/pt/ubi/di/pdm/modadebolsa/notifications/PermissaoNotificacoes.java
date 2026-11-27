package pt.ubi.di.pdm.modadebolsa.notifications;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Pede a permissão de notificações sem nunca bloquear a app.
// Tem de ser criada no onCreate da Activity (antes de a Activity ficar visível),
// porque o registerForActivityResult não pode ser chamado mais tarde.
public class PermissaoNotificacoes {

    private static final String PREFS = "prefs_notificacoes";
    private static final String CHAVE_JA_PEDIU = "ja_pediu_permissao";

    private final AppCompatActivity activity;
    private final ActivityResultLauncher<String> launcher;
    private Runnable aoTerminar;

    public PermissaoNotificacoes(AppCompatActivity activity) {
        this.activity = activity;

        this.launcher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                concedida -> {
                    if (!concedida) {
                        // Só informa. A app continua a funcionar normalmente.
                        Toast.makeText(
                                activity,
                                "Sem notificações não receberás lembretes das entregas. " +
                                        "Podes ativá-las nas definições da app.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    terminar();
                }
        );
    }

    // Pede a permissão se ainda fizer sentido. O "aoTerminar" é chamado sempre,
    // quer o utilizador aceite, recuse ou nem chegue a ser perguntado,
    // e serve para continuar o fluxo (por exemplo, fechar o ecrã de criar projeto).
    public void pedirSeNecessario(Runnable aoTerminar) {
        this.aoTerminar = aoTerminar;

        // Antes do Android 13 não há permissão para pedir
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            terminar();
            return;
        }

        // Já concedida
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            terminar();
            return;
        }

        SharedPreferences prefs = activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean jaPediu = prefs.getBoolean(CHAVE_JA_PEDIU, false);
        boolean deveExplicar = ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.POST_NOTIFICATIONS);

        if (!jaPediu || deveExplicar) {
            // Primeira vez, ou o utilizador recusou uma vez: explica e pergunta
            mostrarExplicacao(prefs);
        } else {
            // Recusada em definitivo: o sistema já não mostra o diálogo, por isso não insistimos
            terminar();
        }
    }

    private void mostrarExplicacao(SharedPreferences prefs) {
        new AlertDialog.Builder(activity)
                .setTitle("Lembretes de entrega")
                .setMessage("Queres receber notificações a avisar quando a data de entrega " +
                        "dos teus projetos se aproxima?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    prefs.edit().putBoolean(CHAVE_JA_PEDIU, true).apply();
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS);
                })
                .setNegativeButton("Agora não", (dialog, which) -> terminar())
                .setOnCancelListener(dialog -> terminar())
                .show();
    }

    // Chama o "aoTerminar" uma única vez
    private void terminar() {
        Runnable r = aoTerminar;
        aoTerminar = null;
        if (r != null) {
            r.run();
        }
    }
}