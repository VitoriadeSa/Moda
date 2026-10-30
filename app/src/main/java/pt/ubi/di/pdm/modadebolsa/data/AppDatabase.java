
package pt.ubi.di.pdm.modadebolsa.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pt.ubi.di.pdm.modadebolsa.data.dao.CaracteristicasTecidoDao;
import pt.ubi.di.pdm.modadebolsa.data.dao.CategoriaCorDao;
import pt.ubi.di.pdm.modadebolsa.data.dao.CategoriaMoldeDao;
import pt.ubi.di.pdm.modadebolsa.data.dao.CorDao;
import pt.ubi.di.pdm.modadebolsa.data.dao.MoldeDao;
import pt.ubi.di.pdm.modadebolsa.data.dao.TecidoDao;

@Database(
        entities = {
                CategoriaMolde.class,
                Molde.class,
                Tecido.class,
                CaracteristicasTecido.class,
                Cor.class,
                CategoriaCor.class
        },
        version = 1,
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

    public abstract CategoriaCorDao categoriaCorDao();

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
                            .addCallback(DadosIniciais.callback())
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
