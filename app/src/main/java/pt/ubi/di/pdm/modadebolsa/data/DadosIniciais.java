
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
                // 3. CARACTERÍSTICAS DOS TECIDOS
                // =========================================

                String[] caracteristicas = {

                        caracteristica(
                                "Algodão",
                                "Médio",
                                "Sem elasticidade",
                                "Opaco",
                                "Média",
                                "Alta"
                        ),

                        caracteristica(
                                "Viscose",
                                "Muito fluido",
                                "Sem elasticidade",
                                "Opaco",
                                "Média",
                                "Média"
                        ),

                        caracteristica(
                                "Lã",
                                "Estruturado",
                                "Sem elasticidade",
                                "Opaco",
                                "Baixa",
                                "Alta"
                        ),

                };

                for (String sql : caracteristicas) {
                    db.execSQL(sql);
                }

                // =========================================
                // 4. TECIDOS
                // =========================================

                String[] tecidos = {

                        tecido("Algodão natural", "Algodão", "Médio",
                                "Sem elasticidade", "Opaco", "Média", "Alta"),

                        tecido("Viscose fluida", "Viscose", "Muito fluido",
                                "Sem elasticidade", "Opaco", "Média", "Média"),

                        tecido("Lã tradicional", "Lã", "Estruturado",
                                "Sem elasticidade", "Opaco", "Baixa", "Alta"),

                        };

                for (String sql : tecidos) {
                    db.execSQL(sql);
                }
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

    private static String caracteristica(
            String composicao,
            String caimento,
            String elasticidade,
            String transparencia,
            String amarrotabilidade,
            String durabilidade) {

        return "INSERT INTO CaracteristicasTecido (" +
                "composicao, caimento, elasticidade, " +
                "transparencia, amarrotabilidade, durabilidade" +
                ") VALUES (" +
                "'" + composicao + "', " +
                "'" + caimento + "', " +
                "'" + elasticidade + "', " +
                "'" + transparencia + "', " +
                "'" + amarrotabilidade + "', " +
                "'" + durabilidade + "')";
    }

    private static String tecido(
            String nome,
            String composicao,
            String caimento,
            String elasticidade,
            String transparencia,
            String amarrotabilidade,
            String durabilidade) {

        return "INSERT INTO Tecido (" +
                "nome, pathDocumentoPDF, pathImagem, " +
                "idCaracteristicasTecido, favorito" +
                ") SELECT " +
                "'" + nome + "', " +
                "NULL, NULL, " +
                "idCaracteristicasTecido, 0 " +
                "FROM CaracteristicasTecido " +
                "WHERE composicao = '" + composicao + "' " +
                "AND caimento = '" + caimento + "' " +
                "AND elasticidade = '" + elasticidade + "' " +
                "AND transparencia = '" + transparencia + "' " +
                "AND amarrotabilidade = '" + amarrotabilidade + "' " +
                "AND durabilidade = '" + durabilidade + "' " +
                "LIMIT 1";
    }
}
