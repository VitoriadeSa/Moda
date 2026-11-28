package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Cor"
)
public class Cor {

    @PrimaryKey(autoGenerate = true)
    public int idCor;

    public String codigoHex;
    public String nome;
    public String pathImagem;
    public boolean favorito;

    public Cor(
            String codigoHex,
            String nome,
            boolean favorito
    ) {
        this.codigoHex = codigoHex;
        this.nome = nome;
        this.favorito = favorito;
    }
}