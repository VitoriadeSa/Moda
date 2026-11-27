package pt.ubi.di.pdm.modadebolsa.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

import pt.ubi.di.pdm.modadebolsa.MainActivity;
import pt.ubi.di.pdm.modadebolsa.R;
import pt.ubi.di.pdm.modadebolsa.data.Projeto;

public class NotificacaoHelper {

    public static final String CHANNEL_ID = "entregas_projetos";
    private static final long DIA_MS = 24L * 60 * 60 * 1000;

    private NotificacaoHelper() {
    }

    // Cria o canal (obrigatório desde o Android 8). Pode ser chamado várias vezes.
    public static void criarCanal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    "Entregas de projetos",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            canal.setDescription("Lembretes das datas de entrega dos projetos");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }
    }

    // Mostra a notificação de um projeto
    public static void mostrar(Context context, Projeto projeto, int diasRestantes) {

        // Android 13+: sem a permissão concedida, não se pode notificar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        criarCanal(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                projeto.idProjeto,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String texto;
        if (diasRestantes == 0) {
            texto = "A entrega do projeto " + projeto.nome + " é hoje!";
        } else if (diasRestantes == 1) {
            texto = "A entrega  do projeto " + projeto.nome + " é amanhã.";
        } else {
            texto = "Faltam " + diasRestantes + " dias para a entrega do projeto " + projeto.nome + ".";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_moda) // troca pelo ícone da app
                .setContentTitle(projeto.nome)
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // O id do projeto serve de id da notificação: uma por projeto, que se substitui a si mesma
        NotificationManagerCompat.from(context).notify(projeto.idProjeto, builder.build());
    }

    // Início do dia (00:00) de um instante, no fuso horário do telemóvel
    public static long inicioDoDia(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    // Dias que faltam até à data de entrega (0 = hoje, negativo = atrasado)
    public static int diasRestantes(long dataEntrega) {
        long diferenca = inicioDoDia(dataEntrega) - inicioDoDia(System.currentTimeMillis());
        // Math.round absorve os dias com 23 ou 25 horas (mudança de hora)
        return (int) Math.round(diferenca / (double) DIA_MS);
    }
}