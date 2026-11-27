package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Utilizador",
        indices = {@Index(value = {"email"}, unique = true)}
)
public class Utilizador {

    @PrimaryKey(autoGenerate = true)
    public int idUtilizador;

    public String email;
    public String passwordHash;

    public Utilizador(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
