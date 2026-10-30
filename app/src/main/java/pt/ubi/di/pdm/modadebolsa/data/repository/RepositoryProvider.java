
package pt.ubi.di.pdm.modadebolsa.data.repository;

import android.content.Context;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;

public class RepositoryProvider {

    private final CaracteristicasTecidoRepository caracteristicasTecidoRepository;
    private final CategoriaCorRepository categoriaCorRepository;
    private final CategoriaMoldeRepository categoriaMoldeRepository;
    private final CorRepository corRepository;
    private final MoldeRepository moldeRepository;
    private final TecidoRepository tecidoRepository;

    public RepositoryProvider(Context context) {

        AppDatabase db = AppDatabase.getInstance(context);

        caracteristicasTecidoRepository =
                new CaracteristicasTecidoRepository(
                        db.caracteristicasTecidoDao());

        categoriaCorRepository =
                new CategoriaCorRepository(
                        db.categoriaCorDao());

        categoriaMoldeRepository =
                new CategoriaMoldeRepository(
                        db.categoriaMoldeDao());

        corRepository =
                new CorRepository(
                        db.corDao());

        moldeRepository =
                new MoldeRepository(
                        db.moldeDao());

        tecidoRepository =
                new TecidoRepository(
                        db.tecidoDao());
    }

    public CaracteristicasTecidoRepository getCaracteristicasTecidoRepository() {
        return caracteristicasTecidoRepository;
    }

    public CategoriaCorRepository getCategoriaCorRepository() {
        return categoriaCorRepository;
    }

    public CategoriaMoldeRepository getCategoriaMoldeRepository() {
        return categoriaMoldeRepository;
    }

    public CorRepository getCorRepository() {
        return corRepository;
    }

    public MoldeRepository getMoldeRepository() {
        return moldeRepository;
    }

    public TecidoRepository getTecidoRepository() {
        return tecidoRepository;
    }
}
