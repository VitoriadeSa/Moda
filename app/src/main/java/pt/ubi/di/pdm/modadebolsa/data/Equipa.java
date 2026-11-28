package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Equipa")
public class Equipa {

    @PrimaryKey(autoGenerate = true)
    public int idEquipa;

    public String nome;

    public Equipa(String nome) {
        this.nome = nome;
    }
}
