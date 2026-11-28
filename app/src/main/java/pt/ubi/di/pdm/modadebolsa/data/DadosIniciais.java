
package pt.ubi.di.pdm.modadebolsa.data;

import static pt.ubi.di.pdm.modadebolsa.data.ValoresTecido.*;

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

                // =========================================
                // 1. CATEGORIAS DE MOLDES
                // =========================================

                String[] categorias = {
                        "Calças",
                        "Saias",
                        "Blusas",
                        "Vestidos",
                };

                for (String categoria : categorias) {
                    db.execSQL(
                            "INSERT INTO CategoriaMolde (nomeCategoria) VALUES (?)",
                            new Object[]{categoria}
                    );
                }

                // =========================================
                // 2. MOLDES
                // =========================================

                String[] moldes = {

                        molde("Calças", "Calças direitas", "Fácil"),

                        molde("Saias", "Saia plissada", "Médio"),

                        molde("Blusas", "Blusa com folhos", "Médio"),

                        molde("Vestidos", "Vestido reto", "Fácil"),

                        };

                for (String sql : moldes) {
                    db.execSQL(sql);
                }

                // =========================================
                // 3. TECIDOS + CARACTERÍSTICAS
                //    (cada tecido tem a sua ficha de características)
                //    Imagens: colocar em app/src/main/assets/tecidos/
                // =========================================

                //            nome        imagem                         composição  caimento     elasticidade transparência amarrotab. durabilidade
                tecido(db, "Algodão",   "asset:tecidos/algodao.jpg",   ORGANICA,   RIGIDO,      BAIXA,       BAIXA,        MEDIA,      ALTA);
                tecido(db, "Linho",     "asset:tecidos/linho.jpg",     ORGANICA,   RIGIDO,      BAIXA,       MEDIA,        ALTA,       ALTA);
                tecido(db, "Elastano",  "asset:tecidos/elastano.jpg",  SINTETICA,  EQUILIBRADO, ALTA,        BAIXA,        BAIXA,      MEDIA);
                tecido(db, "Poliéster", "asset:tecidos/poliester.jpg", SINTETICA,  EQUILIBRADO, BAIXA,       BAIXA,        BAIXA,      ALTA);
                tecido(db, "Viscose",   "asset:tecidos/viscose.jpg",   MISTA,      FLUIDO,      BAIXA,       MEDIA,        ALTA,       MEDIA);
                tecido(db, "Lã",        "asset:tecidos/la.jpg",        ORGANICA,   EQUILIBRADO, MEDIA,       BAIXA,        BAIXA,      ALTA);
            }
        };
    }

    // =============================================
    // MÉTODOS AUXILIARES
    // =============================================

    private static String molde(
            String categoria,
            String nome,
            String dificuldade) {

        return "INSERT INTO Molde (" +
                "nomeMolde, idCategoria, creditos, " +
                "pathImagem, pathDocumentoPDF, dificuldade, favorito" +
                ") VALUES (" +
                "'" + nome + "', " +
                "(SELECT idCategoria FROM CategoriaMolde " +
                "WHERE nomeCategoria = '" + categoria + "' LIMIT 1), " +
                "NULL, NULL, NULL, " +
                "'" + dificuldade + "', 0)";
    }

    /**
     * Insere a ficha de características e, logo a seguir, o tecido que aponta para ela.
     * Usa last_insert_rowid() em vez de procurar a ficha pelos valores: assim dois
     * tecidos com características iguais não colidem no índice UNIQUE de Tecido.
     */
    private static void tecido(
            SupportSQLiteDatabase db,
            String nome,
            String pathImagem,
            String composicao,
            String caimento,
            String elasticidade,
            String transparencia,
            String amarrotabilidade,
            String durabilidade) {

        db.execSQL(
                "INSERT INTO CaracteristicasTecido (" +
                        "composicao, caimento, elasticidade, " +
                        "transparencia, amarrotabilidade, durabilidade" +
                        ") VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{composicao, caimento, elasticidade,
                        transparencia, amarrotabilidade, durabilidade}
        );

        db.execSQL(
                "INSERT INTO Tecido (" +
                        "nome, pathDocumentoPDF, pathImagem, " +
                        "idCaracteristicasTecido, favorito" +
                        ") VALUES (?, NULL, ?, last_insert_rowid(), 0)",
                new Object[]{nome, pathImagem}
        );
    }
}
