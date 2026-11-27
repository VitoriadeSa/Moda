package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;

import pt.ubi.di.pdm.modadebolsa.data.Item;
import pt.ubi.di.pdm.modadebolsa.data.dao.ItemDao;

public class ItemRepository {

    private final ItemDao itemDao;
    private final ExecutorService executorService;

    public ItemRepository(ItemDao dao) {
        this.itemDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();
    }

    // Inserir item
    public void inserir(Item item, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = itemDao.inserir(item);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Listar os itens de um projeto
    public void obterPorProjeto(int idProjeto, OnListaItensCallback callback) {
        executorService.execute(() -> {
            List<Item> lista = itemDao.obterPorProjeto(idProjeto);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Marcar ou desmarcar um item como concluído
    public void atualizarConcluido(int idItem, boolean concluido, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = itemDao.atualizarConcluido(idItem, concluido);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Alterar o conteúdo de um item
    public void atualizarConteudo(int idItem, String conteudo, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = itemDao.atualizarConteudo(idItem, conteudo);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Eliminar item
    public void eliminar(Item item, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = itemDao.eliminar(item);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Número total de itens de um projeto
    public void contarPorProjeto(int idProjeto, OnContagemCallback callback) {
        executorService.execute(() -> {
            int total = itemDao.contarPorProjeto(idProjeto);

            if (callback != null) {
                callback.onResult(total);
            }
        });
    }

    // Número de itens concluídos de um projeto
    public void contarConcluidos(int idProjeto, OnContagemCallback callback) {
        executorService.execute(() -> {
            int total = itemDao.contarConcluidos(idProjeto);

            if (callback != null) {
                callback.onResult(total);
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

    public interface OnListaItensCallback {
        void onResult(List<Item> lista);
    }

    public interface OnContagemCallback {
        void onResult(int total);
    }
}