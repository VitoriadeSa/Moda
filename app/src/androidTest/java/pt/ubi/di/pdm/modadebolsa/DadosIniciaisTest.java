
package pt.ubi.di.pdm.modadebolsa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.CategoriaMolde;
import pt.ubi.di.pdm.modadebolsa.data.DadosIniciais;

@RunWith(AndroidJUnit4.class)
public class DadosIniciaisTest {

    private AppDatabase db;

    @Before
    public void criarBaseDeDados() {
        Context context =
                ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(
                        context,
                        AppDatabase.class
                )
                .addCallback(DadosIniciais.callback())
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void fecharBaseDeDados() {
        if (db != null) {
            db.close();
        }
    }

    @Test
    public void deveInserirQuatroCategoriasIniciais() {
        List<CategoriaMolde> categorias =
                db.categoriaMoldeDao().obterTodas();

        assertNotNull(categorias);
        assertEquals(4, categorias.size());

        assertEquals("Blusas", categorias.get(0).nomeCategoria);
        assertEquals("Calças", categorias.get(1).nomeCategoria);
        assertEquals("Casacos", categorias.get(2).nomeCategoria);
        assertEquals("Saias", categorias.get(3).nomeCategoria);
    }
}
