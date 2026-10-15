
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TecidoDao {

    @Insert
    long inserir(Tecido tecido);

    @Update
    int atualizar(Tecido tecido);

    @Query("SELECT * FROM Tecido " +
            "WHERE idTecido = :idTecido LIMIT 1")
    Tecido obterPorId(int idTecido);

    @Query("SELECT * FROM Tecido ORDER BY nome ASC")
    List<Tecido> obterTodos();

    @Query("SELECT * FROM Tecido " +
            "WHERE favorito = 1 ORDER BY nome ASC")
    List<Tecido> obterFavoritos();

    @Query("UPDATE Tecido SET favorito = :favorito " +
            "WHERE idTecido = :idTecido")
    int atualizarFavorito(int idTecido, boolean favorito);

    @Query("SELECT Tecido.* FROM Tecido " +
            "INNER JOIN CaracteristicasTecido " +
            "ON Tecido.idCaracteristicasTecido = " +
            "CaracteristicasTecido.idCaracteristicasTecido " +
            "WHERE (:composicao IS NULL OR " +
            "CaracteristicasTecido.composicao = :composicao) " +
            "AND (:caimento IS NULL OR " +
            "CaracteristicasTecido.caimento = :caimento) " +
            "AND (:elasticidade IS NULL OR " +
            "CaracteristicasTecido.elasticidade = :elasticidade) " +
            "AND (:transparencia IS NULL OR " +
            "CaracteristicasTecido.transparencia = :transparencia) " +
            "AND (:amarrotabilidade IS NULL OR " +
            "CaracteristicasTecido.amarrotabilidade = :amarrotabilidade) " +
            "AND (:durabilidade IS NULL OR " +
            "CaracteristicasTecido.durabilidade = :durabilidade) " +
            "ORDER BY Tecido.nome ASC")
    List<Tecido> filtrar(
            String composicao,
            String caimento,
            String elasticidade,
            String transparencia,
            String amarrotabilidade,
            String durabilidade
    );
}


/*
* É possível fazer isto
List<Tecido> resultados = tecidoDao.filtrar(
    "Orgânica",
    null,
    null,
    null,
    null,
    null
);
* */