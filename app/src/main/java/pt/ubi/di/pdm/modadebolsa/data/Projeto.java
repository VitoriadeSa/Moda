package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Projeto",
        foreignKeys = @ForeignKey(
                entity = Equipa.class,
                parentColumns = "idEquipa",
                childColumns = "idEquipa",
                onDelete = ForeignKey.CASCADE // apagar a equipa apaga os seus projetos
        ),
        indices = {@Index("idEquipa")}
)
public class Projeto {

    @PrimaryKey(autoGenerate = true)
    public int idProjeto;

    public int idEquipa;
    public String nome;
    public long dataEntrega; // epoch em milissegundos

    public Projeto(int idEquipa, String nome, long dataEntrega) {
        this.idEquipa = idEquipa;
        this.nome = nome;
        this.dataEntrega = dataEntrega;
    }
}