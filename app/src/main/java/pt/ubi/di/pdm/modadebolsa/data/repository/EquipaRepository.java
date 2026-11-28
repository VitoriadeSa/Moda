package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;

import pt.ubi.di.pdm.modadebolsa.data.Equipa;
import pt.ubi.di.pdm.modadebolsa.data.MembroEquipa;
import pt.ubi.di.pdm.modadebolsa.data.Utilizador;
import pt.ubi.di.pdm.modadebolsa.data.dao.EquipaDao;

public class EquipaRepository {

    private final EquipaDao equipaDao;
    private final ExecutorService executorService;

    public EquipaRepository(EquipaDao dao) {
        this.equipaDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();
    }

    // Inserir equipa
    public void inserir(Equipa equipa, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = equipaDao.inserir(equipa);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Obter equipa por ID
    public void obterPorId(int idEquipa, OnEquipaCallback callback) {
        executorService.execute(() -> {
            Equipa equipa = equipaDao.obterPorId(idEquipa);

            if (callback != null) {
                callback.onResult(equipa);
            }
        });
    }

    // Obter todas as equipas
    public void obterTodas(OnListaEquipasCallback callback) {
        executorService.execute(() -> {
            List<Equipa> lista = equipaDao.obterTodas();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Obter as equipas de um utilizador
    public void obterPorUtilizador(int idUtilizador, OnListaEquipasCallback callback) {
        executorService.execute(() -> {
            List<Equipa> lista = equipaDao.obterPorUtilizador(idUtilizador);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Alterar o nome de uma equipa
    public void atualizarNome(int idEquipa, String nome, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = equipaDao.atualizarNome(idEquipa, nome);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Eliminar equipa
    public void eliminar(Equipa equipa, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = equipaDao.eliminar(equipa);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Adicionar um utilizador a uma equipa (id = -1 se já pertencer)
    public void adicionarMembro(int idUtilizador, int idEquipa, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = equipaDao.adicionarMembro(new MembroEquipa(idUtilizador, idEquipa));

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Remover um utilizador de uma equipa
    public void removerMembro(int idUtilizador, int idEquipa, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = equipaDao.removerMembro(idUtilizador, idEquipa);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Listar os membros de uma equipa
    public void obterMembros(int idEquipa, OnListaUtilizadoresCallback callback) {
        executorService.execute(() -> {
            List<Utilizador> lista = equipaDao.obterMembros(idEquipa);

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

    public interface OnEquipaCallback {
        void onResult(Equipa equipa);
    }

    public interface OnListaEquipasCallback {
        void onResult(List<Equipa> lista);
    }

    public interface OnListaUtilizadoresCallback {
        void onResult(List<Utilizador> lista);
    }
}