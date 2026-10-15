
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Cor",
        foreignKeys = @ForeignKey(
                entity = CategoriaCor.class,
                parentColumns = "idCategoriaCor",
                childColumns = "idCategoriaCor",
                onDelete = ForeignKey.RESTRICT
        ),
        indices = {@Index("idCategoriaCor")}
)
public class Cor {

    @PrimaryKey(autoGenerate = true)
    public int idCor;

    public String codigoHex;
    public String nome;
    public String pathImagem;
    public boolean favorito;
    public int idCategoriaCor;

    public Cor(
            String codigoHex,
            String nome,
            String pathImagem,
            boolean favorito,
            int idCategoriaCor
    ) {
        this.codigoHex = codigoHex;
        this.nome = nome;
        this.pathImagem = pathImagem;
        this.favorito = favorito;
        this.idCategoriaCor = idCategoriaCor;
    }
}
