
package pt.ubi.di.pdm.modadebolsa.data;

import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


public final class DadosIniciais {

    private DadosIniciais() {
        // Esta classe não precisa de ser instanciada.
    }

    public static RoomDatabase.Callback callback() {
        return new RoomDatabase.Callback() {

            @Override
            public void onCreate(SupportSQLiteDatabase db) {
                super.onCreate(db);

                db.execSQL(
                        "INSERT INTO CategoriaMolde (nomeCategoria) VALUES (?)",
                        new Object[]{"Calças"}
                );

                db.execSQL(
                        "INSERT INTO CategoriaMolde (nomeCategoria) VALUES (?)",
                        new Object[]{"Saias"}
                );

                db.execSQL(
                        "INSERT INTO CategoriaMolde (nomeCategoria) VALUES (?)",
                        new Object[]{"Blusas"}
                );

                db.execSQL(
                        "INSERT INTO CategoriaMolde (nomeCategoria) VALUES (?)",
                        new Object[]{"Casacos"}
                );
            }
        };
    }
}
