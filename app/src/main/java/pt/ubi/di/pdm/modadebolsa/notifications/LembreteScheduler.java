package pt.ubi.di.pdm.modadebolsa.notifications;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class LembreteScheduler {

    private static final String NOME_TRABALHO = "lembrete_entregas";
    private static final int HORA_NOTIFICACAO = 9; // 09:00

    private LembreteScheduler() {
    }

    // Chamar uma vez ao abrir a app (por exemplo, no onCreate da MainActivity).
    // KEEP: se o trabalho já está agendado, não o duplica nem o reinicia.
    public static void agendar(Context context) {
        PeriodicWorkRequest pedido = new PeriodicWorkRequest.Builder(
                LembreteEntregaWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(atrasoAteProximaHora(HORA_NOTIFICACAO), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context.getApplicationContext())
                .enqueueUniquePeriodicWork(NOME_TRABALHO, ExistingPeriodicWorkPolicy.KEEP, pedido);
    }

    public static void cancelar(Context context) {
        WorkManager.getInstance(context.getApplicationContext())
                .cancelUniqueWork(NOME_TRABALHO);
    }

    // Milissegundos até à próxima ocorrência da hora indicada
    private static long atrasoAteProximaHora(int hora) {
        Calendar agora = Calendar.getInstance();
        Calendar alvo = Calendar.getInstance();
        alvo.set(Calendar.HOUR_OF_DAY, hora);
        alvo.set(Calendar.MINUTE, 0);
        alvo.set(Calendar.SECOND, 0);
        alvo.set(Calendar.MILLISECOND, 0);

        if (!alvo.after(agora)) {
            alvo.add(Calendar.DAY_OF_YEAR, 1);
        }
        return alvo.getTimeInMillis() - agora.getTimeInMillis();
    }
}