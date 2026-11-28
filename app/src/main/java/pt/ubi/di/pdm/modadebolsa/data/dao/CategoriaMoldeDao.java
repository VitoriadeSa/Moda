
package pt.ubi.di.pdm.modadebolsa.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.CategoriaMolde;

@Dao
public interface CategoriaMoldeDao {

    @Query("SELECT * FROM CategoriaMolde ORDER BY nomeCategoria ASC")
    List<CategoriaMolde> obterTodas();

    @Query("SELECT * FROM CategoriaMolde ORDER BY nomeCategoria")
    LiveData<List<CategoriaMolde>> getTodas();

    @Insert
    long inserir(CategoriaMolde categoria);

    @Insert
    void inserirTodas(List<CategoriaMolde> categorias);

    @Query("SELECT * FROM CategoriaMolde WHERE idCategoria = :idCategoria LIMIT 1")
    CategoriaMolde obterPorId(int idCategoria);
}
