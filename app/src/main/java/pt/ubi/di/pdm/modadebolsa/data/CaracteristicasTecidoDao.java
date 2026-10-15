
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CaracteristicasTecidoDao {

    @Insert
    long inserir(CaracteristicasTecido caracteristicas);

    @Update
    int atualizar(CaracteristicasTecido caracteristicas);

    @Query("SELECT * FROM CaracteristicasTecido " +
            "WHERE idCaracteristicasTecido = :id " +
            "LIMIT 1")
    CaracteristicasTecido obterPorId(int id);

    @Query("SELECT * FROM CaracteristicasTecido " +
            "ORDER BY idCaracteristicasTecido ASC")
    List<CaracteristicasTecido> obterTodas();

    @Query("SELECT * FROM CaracteristicasTecido " +
            "WHERE composicao = :composicao")
    List<CaracteristicasTecido> filtrarPorComposicao(String composicao);

    @Query("SELECT * FROM CaracteristicasTecido " +
            "WHERE caimento = :caimento")
    List<CaracteristicasTecido> filtrarPorCaimento(String caimento);

    @Query("SELECT * FROM CaracteristicasTecido " +
            "WHERE elasticidade = :elasticidade")
    List<CaracteristicasTecido> filtrarPorElasticidade(String elasticidade);
}
