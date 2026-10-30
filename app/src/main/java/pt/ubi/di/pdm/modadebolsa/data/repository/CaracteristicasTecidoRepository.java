
package pt.ubi.di.pdm.modadebolsa.data.repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.CaracteristicasTecido;
import pt.ubi.di.pdm.modadebolsa.data.dao.CaracteristicasTecidoDao;

public class CaracteristicasTecidoRepository {

    private final CaracteristicasTecidoDao caracteristicasTecidoDao;
    private final ExecutorService executorService;

    public CaracteristicasTecidoRepository(CaracteristicasTecidoDao dao) {
        this.caracteristicasTecidoDao = dao;
        this.executorService = DatabaseExecutor.getExecutor();
    }

    // Inserir características de tecido
    public void inserir(CaracteristicasTecido caracteristicas,
                        OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = caracteristicasTecidoDao.inserir(caracteristicas);

            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    // Atualizar características de tecido
    public void atualizar(CaracteristicasTecido caracteristicas,
                          OnUpdateCallback callback) {
        executorService.execute(() -> {
            int resultado = caracteristicasTecidoDao.atualizar(caracteristicas);

            if (callback != null) {
                callback.onResult(resultado);
            }
        });
    }

    // Obter características por ID
    public void obterPorId(int id, OnCaracteristicasCallback callback) {
        executorService.execute(() -> {
            CaracteristicasTecido caracteristicas =
                    caracteristicasTecidoDao.obterPorId(id);

            if (callback != null) {
                callback.onResult(caracteristicas);
            }
        });
    }

    // Obter todas as características
    public void obterTodas(OnListaCaracteristicasCallback callback) {
        executorService.execute(() -> {
            List<CaracteristicasTecido> lista =
                    caracteristicasTecidoDao.obterTodas();

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Filtrar por composição
    public void filtrarPorComposicao(String composicao,
                                     OnListaCaracteristicasCallback callback) {
        executorService.execute(() -> {
            List<CaracteristicasTecido> lista =
                    caracteristicasTecidoDao.filtrarPorComposicao(composicao);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Filtrar por caimento
    public void filtrarPorCaimento(String caimento,
                                   OnListaCaracteristicasCallback callback) {
        executorService.execute(() -> {
            List<CaracteristicasTecido> lista =
                    caracteristicasTecidoDao.filtrarPorCaimento(caimento);

            if (callback != null) {
                callback.onResult(lista);
            }
        });
    }

    // Filtrar por elasticidade
    public void filtrarPorElasticidade(String elasticidade,
                                       OnListaCaracteristicasCallback callback) {
        executorService.execute(() -> {
            List<CaracteristicasTecido> lista =
                    caracteristicasTecidoDao.filtrarPorElasticidade(elasticidade);

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

    public interface OnCaracteristicasCallback {
        void onResult(CaracteristicasTecido caracteristicas);
    }

    public interface OnListaCaracteristicasCallback {
        void onResult(List<CaracteristicasTecido> lista);
    }
}
