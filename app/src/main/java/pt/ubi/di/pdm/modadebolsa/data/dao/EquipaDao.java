package pt.ubi.di.pdm.modadebolsa.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.Equipa;
import pt.ubi.di.pdm.modadebolsa.data.MembroEquipa;
import pt.ubi.di.pdm.modadebolsa.data.Utilizador;

@Dao
public interface EquipaDao {

    // Inserir uma equipa
    @Insert
    long inserir(Equipa equipa);

    // Obter os detalhes de uma equipa
    @Query("SELECT * FROM Equipa " +
            "WHERE idEquipa = :idEquipa LIMIT 1")
    Equipa obterPorId(int idEquipa);

    // Obter todas as equipas
    @Query("SELECT * FROM Equipa ORDER BY nome ASC")
    List<Equipa> obterTodas();

    // Alterar o nome de uma equipa
    @Query("UPDATE Equipa SET nome = :nome " +
            "WHERE idEquipa = :idEquipa")
    int atualizarNome(int idEquipa, String nome);

    // Eliminar uma equipa (apaga também os seus projetos e membros)
    @Delete
    int eliminar(Equipa equipa);

    // ---------- MembroEquipa (tabela intermédia) ----------

    // Adicionar um utilizador a uma equipa (devolve -1 se já pertencer)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long adicionarMembro(MembroEquipa membro);

    // Remover um utilizador de uma equipa
    @Query("DELETE FROM MembroEquipa " +
            "WHERE idUtilizador = :idUtilizador AND idEquipa = :idEquipa")
    int removerMembro(int idUtilizador, int idEquipa);

    // Listar os membros de uma equipa
    @Query("SELECT u.* FROM Utilizador u " +
            "INNER JOIN MembroEquipa m ON u.idUtilizador = m.idUtilizador " +
            "WHERE m.idEquipa = :idEquipa " +
            "ORDER BY u.email ASC")
    List<Utilizador> obterMembros(int idEquipa);

    // Listar as equipas de um utilizador
    @Query("SELECT e.* FROM Equipa e " +
            "INNER JOIN MembroEquipa m ON e.idEquipa = m.idEquipa " +
            "WHERE m.idUtilizador = :idUtilizador " +
            "ORDER BY e.nome ASC")
    List<Equipa> obterPorUtilizador(int idUtilizador);
}