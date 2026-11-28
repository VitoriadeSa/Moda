package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

// Tabela Intermédia
@Entity(
        tableName = "MoldeProjeto",
        primaryKeys = {"idMolde", "idProjeto"},
        foreignKeys = {
                @ForeignKey(
                        entity = Molde.class,
                        parentColumns = "idMolde",
                        childColumns = "idMolde",
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
public class MoldeProjeto {

    public int idMolde;
    public int idProjeto;

    public MoldeProjeto(int idMolde, int idProjeto) {
        this.idMolde = idMolde;
        this.idProjeto = idProjeto;
    }
}