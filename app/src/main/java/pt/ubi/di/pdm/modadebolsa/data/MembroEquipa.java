package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

// Tabela Intermédia
@Entity(
        tableName = "MembroEquipa",
        primaryKeys = {"idUtilizador", "idEquipa"},
        foreignKeys = {
                @ForeignKey(
                        entity = Utilizador.class,
                        parentColumns = "idUtilizador",
                        childColumns = "idUtilizador",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Equipa.class,
                        parentColumns = "idEquipa",
                        childColumns = "idEquipa",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("idEquipa")}
)
public class MembroEquipa {

    public int idUtilizador;
    public int idEquipa;

    public MembroEquipa(int idUtilizador, int idEquipa) {
        this.idUtilizador = idUtilizador;
        this.idEquipa = idEquipa;
    }
}
