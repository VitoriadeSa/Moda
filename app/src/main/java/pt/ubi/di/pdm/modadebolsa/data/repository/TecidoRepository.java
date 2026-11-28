
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.Tecido;
import pt.ubi.di.pdm.modadebolsa.data.dao.TecidoDao;

public class TecidoRepository {

    private final TecidoDao tecidoDao;
    private final ExecutorService executorService;

    public TecidoRepository(TecidoDao dao) {
        this.tecidoDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();    }

    // Inserir tecido
    public void inserir(Tecido tecido, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = tecidoDao.inserir(tecido);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }


    // Obter tecido por ID
    public void obterPorId(int idTecido, OnTecidoCallback callback) {
        executorService.execute(() -> {
            Tecido tecido = tecidoDao.obterPorId(idTecido);

            if (callback != null) {
                callback.onResult(tecido);
            }
        });
    }

    // Obter todos os tecidos
    public void obterTodos(OnListaTecidosCallback callback) {
        executorService.execute(() -> {
            List<Tecido> lista = tecidoDao.obterTodos();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Filtrar tecidos pelas suas características
    public void filtrar(
            String composicao,
            String caimento,
            String elasticidade,
            String transparencia,
            String amarrotabilidade,
            String durabilidade,
            OnListaTecidosCallback callback) {

        executorService.execute(() -> {
            List<Tecido> lista = tecidoDao.filtrar(
                    composicao,
                    caimento,
                    elasticidade,
                    transparencia,
                    amarrotabilidade,
                    durabilidade
            );

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }


    // Callbacks

    public interface OnInsertCallback {
        void onResult(long id);
    }

    public interface OnUpdateCallback {
        void onResult(int resultado);
    }

    public interface OnTecidoCallback {
        void onResult(Tecido tecido);
    }

    public interface OnListaTecidosCallback {
        void onResult(List<Tecido> lista);
    }
}
