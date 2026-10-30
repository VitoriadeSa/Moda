
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.Cor;
import pt.ubi.di.pdm.modadebolsa.data.dao.CorDao;

public class CorRepository {

    private final CorDao corDao;
    private final ExecutorService executorService;

    public CorRepository(CorDao dao) {
        this.corDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();    }

    // Inserir cor
    public void inserir(Cor cor, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = corDao.inserir(cor);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Atualizar cor
    public void atualizar(Cor cor, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = corDao.atualizar(cor);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Obter cor por ID
    public void obterPorId(int id, OnCorCallback callback) {
        executorService.execute(() -> {
            Cor cor = corDao.obterPorId(id);

            if (callback != null) {
                callback.onResult(cor);
            }
        });
    }

    // Obter todas as cores
    public void obterTodas(OnListaCoresCallback callback) {
        executorService.execute(() -> {
            List<Cor> lista = corDao.obterTodas();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Obter cores favoritas
    public void obterFavoritos(OnListaCoresCallback callback) {
        executorService.execute(() -> {
            List<Cor> lista = corDao.obterFavoritos();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Atualizar estado de favorito
    public void atualizarFavorito(int idCor, boolean favorito,
                                  OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = corDao.atualizarFavorito(idCor, favorito);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Filtrar cores por estação e temperatura
    public void filtrar(String estacao, String temperatura,
                        OnListaCoresCallback callback) {
        executorService.execute(() -> {
            List<Cor> lista = corDao.filtrar(estacao, temperatura);

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

    public interface OnCorCallback {
        void onResult(Cor cor);
    }

    public interface OnListaCoresCallback {
        void onResult(List<Cor> lista);
    }
}
