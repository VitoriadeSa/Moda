package pt.ubi.di.pdm.modadebolsa.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import pt.ubi.di.pdm.modadebolsa.data.*;

@Dao
public interface UtilizadorDao {

    // Inserir um utilizador
    // (lança SQLiteConstraintException se o email já existir)
    @Insert
    long inserir(Utilizador utilizador);

    // Obter um utilizador pelo id
    @Query("SELECT * FROM Utilizador " +
            "WHERE idUtilizador = :idUtilizador LIMIT 1")
    Utilizador obterPorId(int idUtilizador);

    // Obter um utilizador pelo email (útil para o login)
    @Query("SELECT * FROM Utilizador " +
            "WHERE email = :email LIMIT 1")
    Utilizador obterPorEmail(String email);

    // Verificar se um email já está registado
    @Query("SELECT COUNT(*) FROM Utilizador WHERE email = :email")
    int contarPorEmail(String email);

    // Alterar a password (já em hash)
    @Query("UPDATE Utilizador SET passwordHash = :passwordHash " +
            "WHERE idUtilizador = :idUtilizador")
    int atualizarPasswordHash(int idUtilizador, String passwordHash);

    // Eliminar um utilizador (remove também as suas associações a equipas)
    @Delete
    int eliminar(Utilizador utilizador);
}