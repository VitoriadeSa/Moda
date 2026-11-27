package pt.ubi.di.pdm.modadebolsa.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.*;

@Dao
public interface ProjetoDao {

    // Inserir um projeto
    @Insert
    long inserir(Projeto projeto);

    // Obter os detalhes de um projeto
    @Query("SELECT * FROM Projeto " +
            "WHERE idProjeto = :idProjeto LIMIT 1")
    Projeto obterPorId(int idProjeto);

    // Listar os projetos de uma equipa (entregas mais próximas primeiro)
    @Query("SELECT * FROM Projeto " +
            "WHERE idEquipa = :idEquipa " +
            "ORDER BY dataEntrega ASC")
    List<Projeto> obterPorEquipa(int idEquipa);

    // Obter todos os projetos
    @Query("SELECT * FROM Projeto ORDER BY dataEntrega ASC")
    List<Projeto> obterTodos();

    // Projetos com entrega a partir de uma data (usado pelas notificações)
    @Query("SELECT * FROM Projeto " +
            "WHERE dataEntrega >= :desde " +
            "ORDER BY dataEntrega ASC")
    List<Projeto> obterComEntregaAPartirDe(long desde);

    // Atualizar nome
    @Query("UPDATE Projeto SET nome = :nome " +
            "WHERE idProjeto = :idProjeto")
    int atualizarNomeProjeto(int idProjeto, String nome);


    // Atualizar data
    @Query("UPDATE Projeto SET dataEntrega = :dataEntrega " +
            "WHERE idProjeto = :idProjeto")
    int atualizarDataEntrega(int idProjeto, long dataEntrega);


    // Eliminar um projeto (apaga também os seus itens e associações)
    @Delete
    int eliminar(Projeto projeto);

    // ---------- MoldeProjeto ----------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long adicionarMolde(MoldeProjeto moldeProjeto);

    @Query("DELETE FROM MoldeProjeto " +
            "WHERE idMolde = :idMolde AND idProjeto = :idProjeto")
    int removerMolde(int idMolde, int idProjeto);

    @Query("SELECT m.* FROM Molde m " +
            "INNER JOIN MoldeProjeto mp ON m.idMolde = mp.idMolde " +
            "WHERE mp.idProjeto = :idProjeto " +
            "ORDER BY m.nomeMolde ASC")
    List<Molde> obterMoldes(int idProjeto);

    // ---------- TecidoProjeto ----------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long adicionarTecido(TecidoProjeto tecidoProjeto);

    @Query("DELETE FROM TecidoProjeto " +
            "WHERE idTecido = :idTecido AND idProjeto = :idProjeto")
    int removerTecido(int idTecido, int idProjeto);

    @Query("SELECT t.* FROM Tecido t " +
            "INNER JOIN TecidoProjeto tp ON t.idTecido = tp.idTecido " +
            "WHERE tp.idProjeto = :idProjeto")
    List<Tecido> obterTecidos(int idProjeto);

    // ---------- CorProjeto ----------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long adicionarCor(CorProjeto corProjeto);

    @Query("DELETE FROM CorProjeto " +
            "WHERE idCor = :idCor AND idProjeto = :idProjeto")
    int removerCor(int idCor, int idProjeto);

    @Query("SELECT c.* FROM Cor c " +
            "INNER JOIN CorProjeto cp ON c.idCor = cp.idCor " +
            "WHERE cp.idProjeto = :idProjeto")
    List<Cor> obterCores(int idProjeto);
}