
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Tecido",
        foreignKeys = @ForeignKey(
                entity = CaracteristicasTecido.class,
                parentColumns = "idCaracteristicasTecido",
                childColumns = "idCaracteristicasTecido",
                onDelete = ForeignKey.RESTRICT
        ),
        indices = {
                @Index(value = "idCaracteristicasTecido", unique = true) // O índice unique = true impede que dois tecidos apontem para o mesmo registo de características
        }
)
public class Tecido {

    @PrimaryKey(autoGenerate = true)
    public int idTecido;

    public String nome;
    public String pathDocumentoPDF;
    public String pathImagem;
    public int idCaracteristicasTecido;
    public boolean favorito;

    public Tecido(
            String nome,
            String pathDocumentoPDF,
            String pathImagem,
            int idCaracteristicasTecido,
            boolean favorito
    ) {
        this.nome = nome;
        this.pathDocumentoPDF = pathDocumentoPDF;
        this.pathImagem = pathImagem;
        this.idCaracteristicasTecido = idCaracteristicasTecido;
        this.favorito = favorito;
    }
}
