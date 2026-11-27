package pt.ubi.di.pdm.modadebolsa.notifications;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.Projeto;
import pt.ubi.di.pdm.modadebolsa.data.dao.*;
import pt.ubi.di.pdm.modadebolsa.data.repository.ProjetoRepository;

public class LembreteEntregaWorker extends Worker {

    public LembreteEntregaWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    // O WorkManager corre este método numa thread de fundo
    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        ProjetoRepository repository = new ProjetoRepository(
                AppDatabase.getInstance(context).projetoDao());

        long inicioHoje = NotificacaoHelper.inicioDoDia(System.currentTimeMillis());
        List<Projeto> projetos = repository.obterComEntregaAPartirDeSync(inicioHoje);

        for (Projeto projeto : projetos) {
            int dias = NotificacaoHelper.diasRestantes(projeto.dataEntrega);

            if (deveNotificar(dias)) {
                NotificacaoHelper.mostrar(context, projeto, dias);
            }
        }

        return Result.success();
    }

    // Até 7 dias: todos os dias. Mais de 7 dias: dia sim, dia não.
    // Usa a paridade dos dias que faltam, por isso não precisa de guardar estado.
    static boolean deveNotificar(int diasRestantes) {
        if (diasRestantes < 0) {
            return false; // já passou a data
        }
        if (diasRestantes <= 7) {
            return true;
        }
        return diasRestantes % 2 == 0; // envia notificações dia sim dia não se data entrega > 7
    }
}