
package pt.ubi.di.pdm.modadebolsa.data;

import static androidx.activity.OnBackPressedDispatcherKt.addCallback;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pt.ubi.di.pdm.modadebolsa.data.dao.*;

@Database(
        entities = {
                CategoriaMolde.class,
                Molde.class,
                Tecido.class,
                CaracteristicasTecido.class,
                Cor.class,
                Projeto.class,
                Equipa.class,
                Item.class,
                Utilizador.class,
                TecidoProjeto.class,
                MoldeProjeto.class,
                CorProjeto.class,
                MembroEquipa.class
        },
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // DAOs

    public abstract CategoriaMoldeDao categoriaMoldeDao();

    public abstract MoldeDao moldeDao();

    public abstract TecidoDao tecidoDao();

    public abstract CaracteristicasTecidoDao caracteristicasTecidoDao();

    public abstract CorDao corDao();

    public abstract ProjetoDao projetoDao();
    public abstract EquipaDao equipaDao();
    public abstract ItemDao itemDao();
    public abstract UtilizadorDao utilizadorDao();

    // Instância única da base de dados

    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "moda_de_bolsa.db"
                            )
                            .addMigrations(Migrations.MIGRATION_2_3)
                            .addCallback(DadosIniciais.callback())
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
