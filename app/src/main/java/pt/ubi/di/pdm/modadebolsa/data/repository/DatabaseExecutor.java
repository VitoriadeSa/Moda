
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseExecutor {

    // Número máximo de threads para operações da base de dados
    private static final int NUM_THREADS = 3;

    // Executor partilhado por todos os Repository
    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(NUM_THREADS);

    private DatabaseExecutor() {
        // Impede a criação de instâncias desta classe
    }

    public static ExecutorService getExecutor() {
        return executorService;
    }

    public static void encerrar() {
        executorService.shutdown();
    }
}
