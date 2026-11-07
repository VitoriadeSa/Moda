package pt.ubi.di.pdm.modadebolsa;
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
public class ListaTecidosActivity extends AppCompatActivity {



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
=======

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListaTecidosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tecido);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ListaTecidosActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        ImageView ivLogoTop = findViewById(R.id.ivLogoTop);
        if (ivLogoTop != null) {
            ivLogoTop.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_tecidos);

            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_tecidos) {
                    return true;
                } else if (id == R.id.nav_moldes) {
                    startActivity(new Intent(this, ListaMoldesActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.nav_outros) {
                    startActivity(new Intent(this, CirculoCoresActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            });
        }
    }
}
>>>>>>> 7f81ae7 (Sprint 3 - Correção da navegação inferior, persistência, transições de atividades e criação das cores)
