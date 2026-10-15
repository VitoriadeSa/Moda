
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CaracteristicasTecido")
public class CaracteristicasTecido {

    @PrimaryKey(autoGenerate = true)
    public int idCaracteristicasTecido;

    public String composicao;
    public String caimento;
    public String elasticidade;
    public String transparencia;
    public String amarrotabilidade;
    public String durabilidade;

    public CaracteristicasTecido(
            String composicao,
            String caimento,
            String elasticidade,
            String transparencia,
            String amarrotabilidade,
            String durabilidade
    ) {
        this.composicao = composicao;
        this.caimento = caimento;
        this.elasticidade = elasticidade;
        this.transparencia = transparencia;
        this.amarrotabilidade = amarrotabilidade;
        this.durabilidade = durabilidade;
    }
}
