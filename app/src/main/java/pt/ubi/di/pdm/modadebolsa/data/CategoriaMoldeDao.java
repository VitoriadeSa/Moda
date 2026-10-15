
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Dao
public interface CategoriaMoldeDao {

    @Query("SELECT * FROM CategoriaMolde ORDER BY nomeCategoria ASC")
    List<CategoriaMolde> obterTodas();

    @Insert
    long inserir(CategoriaMolde categoria);

    @Insert
    void inserirTodas(List<CategoriaMolde> categorias);

    @Query("SELECT * FROM CategoriaMolde WHERE idCategoria = :idCategoria LIMIT 1")
    CategoriaMolde obterPorId(int idCategoria);
}
