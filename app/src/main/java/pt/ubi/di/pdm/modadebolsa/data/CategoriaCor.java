
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CategoriaCor")
public class CategoriaCor {

    @PrimaryKey(autoGenerate = true)
    public int idCategoriaCor;

    public String estacao;
    public String temperatura;

    public CategoriaCor(String estacao, String temperatura) {
        this.estacao = estacao;
        this.temperatura = temperatura;
    }
}
