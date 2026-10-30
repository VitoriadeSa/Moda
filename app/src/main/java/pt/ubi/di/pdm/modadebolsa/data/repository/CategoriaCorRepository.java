
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.CategoriaCor;
import pt.ubi.di.pdm.modadebolsa.data.dao.CategoriaCorDao;

public class CategoriaCorRepository {

    private final CategoriaCorDao categoriaCorDao;
    private final ExecutorService executorService;

    public CategoriaCorRepository(CategoriaCorDao dao) {
        this.categoriaCorDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();    }

    // Inserir categoria de cor
    public void inserir(CategoriaCor categoria, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = categoriaCorDao.inserir(categoria);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Atualizar categoria de cor
    public void atualizar(CategoriaCor categoria, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = categoriaCorDao.atualizar(categoria);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Obter todas as categorias de cor
    public void obterTodas(OnListaCategoriasCallback callback) {
        executorService.execute(() -> {
            List<CategoriaCor> lista = categoriaCorDao.obterTodas();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Obter categoria de cor por ID
    public void obterPorId(int id, OnCategoriaCallback callback) {
        executorService.execute(() -> {
            CategoriaCor categoria = categoriaCorDao.obterPorId(id);

            if (callback != null) {
                callback.onResult(categoria);
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

    public interface OnCategoriaCallback {
        void onResult(CategoriaCor categoria);
    }

    public interface OnListaCategoriasCallback {
        void onResult(List<CategoriaCor> lista);
    }
}
