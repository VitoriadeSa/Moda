
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CategoriaMolde")
public class CategoriaMolde {

    @PrimaryKey(autoGenerate = true)
    public int idCategoria;

    public String nomeCategoria;

    public CategoriaMolde(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
