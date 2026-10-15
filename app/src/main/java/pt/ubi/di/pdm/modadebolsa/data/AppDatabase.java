
package pt.ubi.di.pdm.modadebolsa.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

    public abstract CategoriaMoldeDao categoriaMoldeDao();

    public abstract MoldeDao moldeDao();

    public abstract TecidoDao tecidoDao();

    public abstract CaracteristicasTecidoDao caracteristicasTecidoDao();

    public abstract CorDao corDao();

    public abstract CategoriaCorDao categoriaCorDao();

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
