package pt.ubi.di.pdm.modadebolsa;
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
public class MoldeActivity extends AppCompatActivity {

    public static final String EXTRA_ID_MOLDE = "idMolde";



=======

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;

public class MoldeActivity extends AppCompatActivity {

    public static final String EXTRA_ID_MOLDE = "idMolde";
    private AppDatabase db;
    private int idMolde = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_molde);

        // Inicializar a base de dados Room
        //db = AppDatabase.getDatabase(getApplicationContext());

        // Recuperar o ID do molde passado pela MainActivity através do Intent
        if (getIntent() != null && getIntent().hasExtra(EXTRA_ID_MOLDE)) {
            idMolde = getIntent().getIntExtra(EXTRA_ID_MOLDE, -1);
        }

        // Exemplo de como podes carregar os dados do molde ou abrir o PDF automaticamente
        if (idMolde != -1) {
            carregarDetalhesMolde(idMolde);
        }
    }

    private void carregarDetalhesMolde(int id) {
        // Aqui podes consultar a base de dados Room para obter o nome do ficheiro PDF ou o caminho do molde
        // Exemplo simulado:
        // MoldModel molde = db.moldDao().getById(id);

        // Para abrir o PDF associado a este ID:
        // abrirPdfMolde("molde_" + id + ".pdf");
    }

    /**
     * Abre um ficheiro PDF utilizando uma aplicação externa instalada no dispositivo (ex: leitor de PDF / Google Drive)
     */
    private void abrirPdfMolde(String nomeFicheiroPdf) {
        try {
            // Se o PDF estiver na pasta assets, podes copiá-lo para o cache dir ou abri-lo diretamente se estiver em filesDir
            File file = new File(getFilesDir(), nomeFicheiroPdf);

            if (!file.exists()) {
                Toast.makeText(this, "Ficheiro PDF do molde não encontrado.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri pdfUri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName() + ".fileprovider",
                    file
            );

            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao abrir o PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
