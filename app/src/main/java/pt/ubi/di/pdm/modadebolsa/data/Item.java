package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Item",
        foreignKeys = @ForeignKey(
                entity = Projeto.class,
                parentColumns = "idProjeto",
                childColumns = "idProjeto",
                onDelete = ForeignKey.CASCADE // apagar o projeto apaga os seus itens
        ),
        indices = {@Index("idProjeto")}
)
public class Item {

    @PrimaryKey(autoGenerate = true)
    public int idItem;

    public int idProjeto;
    public String conteudo;
    public boolean concluido; // True ou false (concluido ou por fazer)

    public Item(int idProjeto, String conteudo, boolean concluido) {
        this.idProjeto = idProjeto;
        this.conteudo = conteudo;
        this.concluido = concluido;
    }
}
