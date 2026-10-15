
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CorDao {

    @Insert
    long inserir(Cor cor);

    @Update
    int atualizar(Cor cor);

    @Query("SELECT * FROM Cor WHERE idCor = :id LIMIT 1")
    Cor obterPorId(int id);

    @Query("SELECT * FROM Cor ORDER BY nome ASC")
    List<Cor> obterTodas();

    @Query("SELECT * FROM Cor " +
            "WHERE favorito = 1 ORDER BY nome ASC")
    List<Cor> obterFavoritos();

    @Query("UPDATE Cor SET favorito = :favorito " +
            "WHERE idCor = :idCor")
    int atualizarFavorito(int idCor, boolean favorito);

    @Query("SELECT Cor.* FROM Cor " +
            "INNER JOIN CategoriaCor " +
            "ON Cor.idCategoriaCor = CategoriaCor.idCategoriaCor " +
            "WHERE (:estacao IS NULL OR " +
            "CategoriaCor.estacao = :estacao) " +
            "AND (:temperatura IS NULL OR " +
            "CategoriaCor.temperatura = :temperatura) " +
            "ORDER BY Cor.nome ASC")
    List<Cor> filtrar(String estacao, String temperatura);
}

/*
É possível fazer isto
List<Cor> resultados = corDao.filtrar("Inverno", null);
 */