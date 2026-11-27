package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public Projeto(int idEquipa, String nome) {
        this.idEquipa = idEquipa;
        this.nome = nome;
        this.dataEntrega = 0;
    }



    public String getDataEntregaFormatada() {
        if (dataEntrega == 0) {
            return "Sem data definida";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
        );

        return sdf.format(new Date(dataEntrega));
    }
}