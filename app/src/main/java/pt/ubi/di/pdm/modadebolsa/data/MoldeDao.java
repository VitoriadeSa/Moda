
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoldeDao {

    // Listar os moldes de uma categoria
    @Query("SELECT * FROM Molde " +
            "WHERE idCategoria = :idCategoria " +
            "ORDER BY nomeMolde ASC")
    List<Molde> obterPorCategoria(int idCategoria);

    // Obter os detalhes de um molde
    @Query("SELECT * FROM Molde " +
            "WHERE idMolde = :idMolde LIMIT 1")
    Molde obterPorId(int idMolde);

    // Obter todos os moldes
    @Query("SELECT * FROM Molde ORDER BY nomeMolde ASC")
    List<Molde> obterTodos();

    // Inserir um molde
    @Insert
    long inserir(Molde molde);

    // Marcar ou desmarcar um molde como favorito
    @Query("UPDATE Molde SET favorito = :favorito " +
            "WHERE idMolde = :idMolde")
    int atualizarFavorito(int idMolde, boolean favorito);

    // Consultar os moldes favoritos
    @Query("SELECT * FROM Molde " +
            "WHERE favorito = 1 ORDER BY nomeMolde ASC")
    List<Molde> obterFavoritos();
}
