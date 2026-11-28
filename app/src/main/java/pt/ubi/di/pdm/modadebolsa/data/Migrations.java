package pt.ubi.di.pdm.modadebolsa.data;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrations {

    private Migrations() {
        // Classe só com constantes
    }

    // Versão 2 -> 3: cria as 8 tabelas novas (Projeto, Equipa, Utilizador, Item, junções)
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {

            // ---------- Equipa ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `Equipa` (" +
                    "`idEquipa` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`nome` TEXT)");

            // ---------- Utilizador ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `Utilizador` (" +
                    "`idUtilizador` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`email` TEXT, " +
                    "`passwordHash` TEXT)");
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Utilizador_email` " +
                    "ON `Utilizador` (`email`)");

            // ---------- Projeto ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `Projeto` (" +
                    "`idProjeto` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`idEquipa` INTEGER NOT NULL, " +
                    "`nome` TEXT, " +
                    "`dataEntrega` INTEGER NOT NULL, " +
                    "FOREIGN KEY(`idEquipa`) REFERENCES `Equipa`(`idEquipa`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_Projeto_idEquipa` " +
                    "ON `Projeto` (`idEquipa`)");

            // ---------- Item ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `Item` (" +
                    "`idItem` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`idProjeto` INTEGER NOT NULL, " +
                    "`conteudo` TEXT, " +
                    "`concluido` INTEGER NOT NULL, " +
                    "FOREIGN KEY(`idProjeto`) REFERENCES `Projeto`(`idProjeto`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_Item_idProjeto` " +
                    "ON `Item` (`idProjeto`)");

            // ---------- MembroEquipa ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `MembroEquipa` (" +
                    "`idUtilizador` INTEGER NOT NULL, " +
                    "`idEquipa` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`idUtilizador`, `idEquipa`), " +
                    "FOREIGN KEY(`idUtilizador`) REFERENCES `Utilizador`(`idUtilizador`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE, " +
                    "FOREIGN KEY(`idEquipa`) REFERENCES `Equipa`(`idEquipa`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_MembroEquipa_idEquipa` " +
                    "ON `MembroEquipa` (`idEquipa`)");

            // ---------- MoldeProjeto ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `MoldeProjeto` (" +
                    "`idMolde` INTEGER NOT NULL, " +
                    "`idProjeto` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`idMolde`, `idProjeto`), " +
                    "FOREIGN KEY(`idMolde`) REFERENCES `Molde`(`idMolde`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE, " +
                    "FOREIGN KEY(`idProjeto`) REFERENCES `Projeto`(`idProjeto`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_MoldeProjeto_idProjeto` " +
                    "ON `MoldeProjeto` (`idProjeto`)");

            // ---------- TecidoProjeto ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `TecidoProjeto` (" +
                    "`idTecido` INTEGER NOT NULL, " +
                    "`idProjeto` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`idTecido`, `idProjeto`), " +
                    "FOREIGN KEY(`idTecido`) REFERENCES `Tecido`(`idTecido`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE, " +
                    "FOREIGN KEY(`idProjeto`) REFERENCES `Projeto`(`idProjeto`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_TecidoProjeto_idProjeto` " +
                    "ON `TecidoProjeto` (`idProjeto`)");

            // ---------- CorProjeto ----------
            db.execSQL("CREATE TABLE IF NOT EXISTS `CorProjeto` (" +
                    "`idCor` INTEGER NOT NULL, " +
                    "`idProjeto` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`idCor`, `idProjeto`), " +
                    "FOREIGN KEY(`idCor`) REFERENCES `Cor`(`idCor`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE, " +
                    "FOREIGN KEY(`idProjeto`) REFERENCES `Projeto`(`idProjeto`) " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_CorProjeto_idProjeto` " +
                    "ON `CorProjeto` (`idProjeto`)");
        }
    };
}