package pt.ubi.di.pdm.modadebolsa.data;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

// Tabela Intermédia
@Entity(
        tableName = "CorProjeto",
        primaryKeys = {"idCor", "idProjeto"},
        foreignKeys = {
                @ForeignKey(
                        entity = Cor.class,
                        parentColumns = "idCor",
                        childColumns = "idCor",
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
public class CorProjeto {

    public int idCor;
    public int idProjeto;

    public CorProjeto(int idCor, int idProjeto) {
        this.idCor = idCor;
        this.idProjeto = idProjeto;
    }
}
