
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Molde",
        foreignKeys = @ForeignKey(
                entity = CategoriaMolde.class,
                parentColumns = "idCategoria",
                childColumns = "idCategoria",
                onDelete = ForeignKey.RESTRICT // impede a eliminação da tabela se tiver moldes associados
        ),
        indices = {@Index("idCategoria")}
)
public class Molde {

    @PrimaryKey(autoGenerate = true)
    public int idMolde;

    public String nomeMolde;
    public int idCategoria;
    public String creditos;
    public String pathImagem;
    public String pathDocumentoPDF;
    public String dificuldade;
    public boolean favorito;

    public Molde(
            String nomeMolde,
            int idCategoria,
            String creditos,
            String pathImagem,
            String pathDocumentoPDF,
            String dificuldade,
            boolean favorito
    ) {
        this.nomeMolde = nomeMolde;
        this.idCategoria = idCategoria;
        this.creditos = creditos;
        this.pathImagem = pathImagem;
        this.pathDocumentoPDF = pathDocumentoPDF;
        this.dificuldade = dificuldade;
        this.favorito = favorito;
    }
}
