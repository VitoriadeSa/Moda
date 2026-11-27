package pt.ubi.di.pdm.modadebolsa.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pt.ubi.di.pdm.modadebolsa.data.Item;

@Dao
public interface ItemDao {

    // Inserir um item
    @Insert
    long inserir(Item item);

    // Listar os itens de um projeto (por fazer primeiro)
    @Query("SELECT * FROM Item " +
            "WHERE idProjeto = :idProjeto " +
            "ORDER BY concluido ASC, idItem ASC")
    List<Item> obterPorProjeto(int idProjeto);

    // Marcar ou desmarcar um item como concluído
    @Query("UPDATE Item SET concluido = :concluido " +
            "WHERE idItem = :idItem")
    int atualizarConcluido(int idItem, boolean concluido);

    // Alterar o conteúdo de um item
    @Query("UPDATE Item SET conteudo = :conteudo " +
            "WHERE idItem = :idItem")
    int atualizarConteudo(int idItem, String conteudo);

    // Eliminar um item
    @Delete
    int eliminar(Item item);

    // Número total de itens de um projeto
    @Query("SELECT COUNT(*) FROM Item WHERE idProjeto = :idProjeto")
    int contarPorProjeto(int idProjeto);

    // Número de itens concluídos de um projeto (para uma barra de progresso)
    @Query("SELECT COUNT(*) FROM Item " +
            "WHERE idProjeto = :idProjeto AND concluido = 1")
    int contarConcluidos(int idProjeto);
}