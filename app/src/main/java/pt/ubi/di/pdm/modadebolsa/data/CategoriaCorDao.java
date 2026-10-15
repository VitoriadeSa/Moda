
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoriaCorDao {

    @Insert
    long inserir(CategoriaCor categoria);

    @Update
    int atualizar(CategoriaCor categoria);

    @Query("SELECT * FROM CategoriaCor " +
            "ORDER BY estacao ASC")
    List<CategoriaCor> obterTodas();

    @Query("SELECT * FROM CategoriaCor " +
            "WHERE idCategoriaCor = :id LIMIT 1")
    CategoriaCor obterPorId(int id);
}
