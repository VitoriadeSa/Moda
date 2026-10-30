
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.Molde;
import pt.ubi.di.pdm.modadebolsa.data.dao.MoldeDao;

public class MoldeRepository {

    private final MoldeDao moldeDao;
    private final ExecutorService executorService;

    public MoldeRepository(MoldeDao dao) {
        this.moldeDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();    }

    // Obter moldes por categoria
    public void obterPorCategoria(int idCategoria,
                                  OnListaMoldesCallback callback) {
        executorService.execute(() -> {
            List<Molde> lista = moldeDao.obterPorCategoria(idCategoria);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Obter molde por ID
    public void obterPorId(int idMolde, OnMoldeCallback callback) {
        executorService.execute(() -> {
            Molde molde = moldeDao.obterPorId(idMolde);

            if (callback != null) {
                callback.onResult(molde);
            }
        });
    }

    // Obter todos os moldes
    public void obterTodos(OnListaMoldesCallback callback) {
        executorService.execute(() -> {
            List<Molde> lista = moldeDao.obterTodos();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Inserir molde
    public void inserir(Molde molde, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = moldeDao.inserir(molde);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Atualizar estado de favorito
    public void atualizarFavorito(int idMolde, boolean favorito,
                                  OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = moldeDao.atualizarFavorito(idMolde, favorito);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Obter moldes favoritos
    public void obterFavoritos(OnListaMoldesCallback callback) {
        executorService.execute(() -> {
            List<Molde> lista = moldeDao.obterFavoritos();

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

    public interface OnMoldeCallback {
        void onResult(Molde molde);
    }

    public interface OnListaMoldesCallback {
        void onResult(List<Molde> lista);
    }
}
