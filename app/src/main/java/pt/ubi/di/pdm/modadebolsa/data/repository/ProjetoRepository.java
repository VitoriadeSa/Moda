package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;

import pt.ubi.di.pdm.modadebolsa.data.*;
import pt.ubi.di.pdm.modadebolsa.data.dao.ProjetoDao;

public class ProjetoRepository {

    private final ProjetoDao ProjetoDao;
    private final ExecutorService executorService;

    public ProjetoRepository(ProjetoDao dao) {
        this.ProjetoDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();
    }

    // Inserir projeto
    public void inserir(Projeto projeto, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = ProjetoDao.inserir(projeto);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Obter projeto por ID
    public void obterPorId(int idProjeto, OnProjetoCallback callback) {
        executorService.execute(() -> {
            Projeto projeto = ProjetoDao.obterPorId(idProjeto);

            if (callback != null) {
                callback.onResult(projeto);
            }
        });
    }

    // Listar os projetos de uma equipa
    public void obterPorEquipa(int idEquipa, OnListaProjetosCallback callback) {
        executorService.execute(() -> {
            List<Projeto> lista = ProjetoDao.obterPorEquipa(idEquipa);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Obter todos os projetos
    public void obterTodos(OnListaProjetosCallback callback) {
        executorService.execute(() -> {
            List<Projeto> lista = ProjetoDao.obterTodos();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Versão síncrona: só pode ser chamada fora da thread principal (por exemplo, num Worker)
    public List<Projeto> obterComEntregaAPartirDeSync(long desde) {
        return ProjetoDao.obterComEntregaAPartirDe(desde);
    }

    // Atualizar projeto
    public void atualizar(Projeto projeto, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = ProjetoDao.atualizar(projeto);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Eliminar projeto
    public void eliminar(Projeto projeto, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = ProjetoDao.eliminar(projeto);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // ---------- Moldes do projeto ----------

    public void adicionarMolde(int idMolde, int idProjeto, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = ProjetoDao.adicionarMolde(new MoldeProjeto(idMolde, idProjeto));

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    public void removerMolde(int idMolde, int idProjeto, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = ProjetoDao.removerMolde(idMolde, idProjeto);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    public void obterMoldes(int idProjeto, OnListaMoldesCallback callback) {
        executorService.execute(() -> {
            List<Molde> lista = ProjetoDao.obterMoldes(idProjeto);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // ---------- Tecidos do projeto ----------

    public void adicionarTecido(int idTecido, int idProjeto, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = ProjetoDao.adicionarTecido(new TecidoProjeto(idTecido, idProjeto));

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    public void removerTecido(int idTecido, int idProjeto, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = ProjetoDao.removerTecido(idTecido, idProjeto);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    public void obterTecidos(int idProjeto, OnListaTecidosCallback callback) {
        executorService.execute(() -> {
            List<Tecido> lista = ProjetoDao.obterTecidos(idProjeto);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // ---------- Cores do projeto ----------

    public void adicionarCor(int idCor, int idProjeto, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = ProjetoDao.adicionarCor(new CorProjeto(idCor, idProjeto));

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    public void removerCor(int idCor, int idProjeto, OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = ProjetoDao.removerCor(idCor, idProjeto);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    public void obterCores(int idProjeto, OnListaCoresCallback callback) {
        executorService.execute(() -> {
            List<Cor> lista = ProjetoDao.obterCores(idProjeto);

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

    public interface OnProjetoCallback {
        void onResult(Projeto projeto);
    }

    public interface OnListaProjetosCallback {
        void onResult(List<Projeto> lista);
    }

    public interface OnListaMoldesCallback {
        void onResult(List<Molde> lista);
    }

    public interface OnListaTecidosCallback {
        void onResult(List<Tecido> lista);
    }

    public interface OnListaCoresCallback {
        void onResult(List<Cor> lista);
    }
}