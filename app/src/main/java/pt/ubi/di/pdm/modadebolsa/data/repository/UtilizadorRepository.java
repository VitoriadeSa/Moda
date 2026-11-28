package pt.ubi.di.pdm.modadebolsa.data.repository;

import android.database.sqlite.SQLiteConstraintException;

import java.util.concurrent.ExecutorService;

import pt.ubi.di.pdm.modadebolsa.data.Utilizador;
import pt.ubi.di.pdm.modadebolsa.data.dao.UtilizadorDao;

public class UtilizadorRepository {

    private final UtilizadorDao utilizadorDao;
    private final ExecutorService executorService;

    public UtilizadorRepository(UtilizadorDao dao) {
        this.utilizadorDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();
    }

    // Inserir utilizador (devolve id = -1 se o email já existir)
    public void inserir(Utilizador utilizador, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id;
            try {
                id = utilizadorDao.inserir(utilizador);
            } catch (SQLiteConstraintException e) {
                id = -1;
            }

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Obter utilizador por ID
    public void obterPorId(int idUtilizador, OnUtilizadorCallback callback) {
        executorService.execute(() -> {
            Utilizador utilizador = utilizadorDao.obterPorId(idUtilizador);

            if (callback != null) {
                callback.onResult(utilizador);
            }
        });
    }

    // Obter utilizador por email (útil para o login)
    public void obterPorEmail(String email, OnUtilizadorCallback callback) {
        executorService.execute(() -> {
            Utilizador utilizador = utilizadorDao.obterPorEmail(email);

            if (callback != null) {
                callback.onResult(utilizador);
            }
        });
    }

    // Verificar se um email já está registado
    public void emailExiste(String email, OnBooleanCallback callback) {
        executorService.execute(() -> {
            boolean existe = utilizadorDao.contarPorEmail(email) > 0;

            if (callback != null) {
                callback.onResult(existe);
            }
        });
    }

    // Alterar a password (já em hash)
    public void atualizarPasswordHash(int idUtilizador, String passwordHash, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = utilizadorDao.atualizarPasswordHash(idUtilizador, passwordHash);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Eliminar utilizador
    public void eliminar(Utilizador utilizador, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = utilizadorDao.eliminar(utilizador);

            if (callback != null) {
                callback.onResult(resultado);
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

    public interface OnUtilizadorCallback {
        void onResult(Utilizador utilizador);
    }

    public interface OnBooleanCallback {
        void onResult(boolean valor);
    }
}
