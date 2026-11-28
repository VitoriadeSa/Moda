
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.CategoriaMolde;
import pt.ubi.di.pdm.modadebolsa.data.dao.CategoriaMoldeDao;

public class CategoriaMoldeRepository {

    private final CategoriaMoldeDao categoriaMoldeDao;
    private final ExecutorService executorService;

    public CategoriaMoldeRepository(CategoriaMoldeDao dao) {
        this.categoriaMoldeDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();    }

    // Obter todas as categorias de molde
    public void obterTodas(OnListaCategoriasCallback callback) {
        executorService.execute(() -> {
            List<CategoriaMolde> lista = categoriaMoldeDao.obterTodas();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Inserir categoria de molde
    public void inserir(CategoriaMolde categoria, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = categoriaMoldeDao.inserir(categoria);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Inserir várias categorias de molde
    public void inserirTodas(List<CategoriaMolde> categorias,
                             OnInsertTodasCallback callback) {
        executorService.execute(() -> {
            categoriaMoldeDao.inserirTodas(categorias);

            if (callback != null) {
                callback.onResult();
            }
        });
    }

    // Obter categoria de molde por ID
    public void obterPorId(int idCategoria,
                           OnCategoriaCallback callback) {
        executorService.execute(() -> {
            CategoriaMolde categoria =
                    categoriaMoldeDao.obterPorId(idCategoria);

            if (callback != null) {
                callback.onResult(categoria);
            }
        });
    }



    // Callbacks

    public interface OnInsertCallback {
        void onResult(long id);
    }

    public interface OnInsertTodasCallback {
        void onResult();
    }

    public interface OnCategoriaCallback {
        void onResult(CategoriaMolde categoria);
    }

    public interface OnListaCategoriasCallback {
        void onResult(List<CategoriaMolde> lista);
    }
}
