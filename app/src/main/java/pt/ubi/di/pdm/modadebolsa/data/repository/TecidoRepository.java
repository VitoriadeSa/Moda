
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

    // Atualizar tecido
    public void atualizar(Tecido tecido, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = tecidoDao.atualizar(tecido);

            if (callback != null) {
                callback.onResult(resultado);
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

    // Obter tecidos favoritos
    public void obterFavoritos(OnListaTecidosCallback callback) {
        executorService.execute(() -> {
            List<Tecido> lista = tecidoDao.obterFavoritos();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Atualizar estado de favorito
    public void atualizarFavorito(int idTecido, boolean favorito,
                                  OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = tecidoDao.atualizarFavorito(
                    idTecido, favorito);

            if (callback != null) {
                callback.onResult(resultado);
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
