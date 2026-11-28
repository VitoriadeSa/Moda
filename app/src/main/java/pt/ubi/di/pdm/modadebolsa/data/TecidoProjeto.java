package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

// Tabela Intermédia
@Entity(
        tableName = "TecidoProjeto",
        primaryKeys = {"idTecido", "idProjeto"},
        foreignKeys = {
                @ForeignKey(
                        entity = Tecido.class,
                        parentColumns = "idTecido",
                        childColumns = "idTecido",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Projeto.class,
                        parentColumns = "idProjeto",
                        childColumns = "idProjeto",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("idProjeto")}
)
public class TecidoProjeto {

    public int idTecido;
    public int idProjeto;

    public TecidoProjeto(int idTecido, int idProjeto) {
        this.idTecido = idTecido;
        this.idProjeto = idProjeto;
    }
}
