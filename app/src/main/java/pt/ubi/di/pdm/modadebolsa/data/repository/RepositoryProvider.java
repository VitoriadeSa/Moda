
package pt.ubi.di.pdm.modadebolsa.data.repository;

import android.content.Context;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.dao.*;

public class RepositoryProvider {

    private final CaracteristicasTecidoRepository caracteristicasTecidoRepository;
    private final CategoriaMoldeRepository categoriaMoldeRepository;
    private final CorRepository corRepository;
    private final MoldeRepository moldeRepository;
    private final TecidoRepository tecidoRepository;
    private final ProjetoRepository projetoRepository;
    private final EquipaRepository equipaRepository;
    private final ItemRepository itemRepository;
    private final UtilizadorRepository utilizadorRepository;


    public RepositoryProvider(Context context) {

        AppDatabase db = AppDatabase.getInstance(context);

        caracteristicasTecidoRepository =
                new CaracteristicasTecidoRepository(db.caracteristicasTecidoDao());

        categoriaMoldeRepository = new CategoriaMoldeRepository(db.categoriaMoldeDao());
        corRepository = new CorRepository(db.corDao());
        moldeRepository = new MoldeRepository(db.moldeDao());
        tecidoRepository = new TecidoRepository(db.tecidoDao());
        projetoRepository = new ProjetoRepository(db.projetoDao());
        equipaRepository = new EquipaRepository(db.equipaDao());
        itemRepository = new ItemRepository(db.itemDao());
        utilizadorRepository = new UtilizadorRepository(db.utilizadorDao());

    }

    public CaracteristicasTecidoRepository getCaracteristicasTecidoRepository() {
        return caracteristicasTecidoRepository;
    }

    public CategoriaMoldeRepository getCategoriaMoldeRepository() {
        return categoriaMoldeRepository;
    }

    public CorRepository getCorRepository() {return corRepository;}
    public MoldeRepository getMoldeRepository() {
        return moldeRepository;
    }
    public TecidoRepository getTecidoRepository() {
        return tecidoRepository;
    }
    public ProjetoRepository getProjetoRepository() {
        return projetoRepository;
    }
    public EquipaRepository getEquipaRepository() {
        return equipaRepository;
    }
    public ItemRepository getItemRepository() {
        return itemRepository;
    }
    public UtilizadorRepository getUtilizadorRepository() {
        return utilizadorRepository;
    }
}
