package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private TextInputLayout tilNome, tilEmail, tilPassword, tilConfirmarPassword;
    private EditText etNome, etEmail, etPassword, etConfirmarPassword;
    private TextView tvSubtitulo, tvEsqueciSenha, tvAlternarModo;
    private MaterialButton btnEntrar;
    private ProgressBar progressBar;

    private SupabaseAuth supabaseAuth;
    private boolean modoRegisto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        supabaseAuth = new SupabaseAuth(this);

        if (supabaseAuth.isLoggedIn()) {
            irParaAppPrincipal();
            return;
        }

        tilNome = findViewById(R.id.tilNome);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmarPassword = findViewById(R.id.tilConfirmarPassword);
        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        tvSubtitulo = findViewById(R.id.tvSubtitulo);
        tvEsqueciSenha = findViewById(R.id.tvEsqueciSenha);
        tvAlternarModo = findViewById(R.id.tvAlternarModo);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);

        tvAlternarModo.setOnClickListener(v -> {
            modoRegisto = !modoRegisto;
            atualizarModo();
        });

        btnEntrar.setOnClickListener(v -> submeter());
        tvEsqueciSenha.setOnClickListener(v -> mostrarDialogoRecuperarSenha());

        atualizarModo();
    }

    private void atualizarModo() {
        tilNome.setVisibility(modoRegisto ? View.VISIBLE : View.GONE);
        tilConfirmarPassword.setVisibility(modoRegisto ? View.VISIBLE : View.GONE);
        tvEsqueciSenha.setVisibility(modoRegisto ? View.GONE : View.VISIBLE);
        tvSubtitulo.setText(modoRegisto ? "Cria a tua conta" : "Bem-vindo de volta");
        tilPassword.setHint(modoRegisto
                ? "Palavra-passe (mín. 8, A-Z e ., _, !)"
                : "Palavra-passe");
        btnEntrar.setText(modoRegisto ? "Registar" : "Entrar");
        tvAlternarModo.setText(modoRegisto
                ? "Já tens conta? Entra agora"
                : "Não tens conta? Regista-te agora");

        if (!modoRegisto) {
            etConfirmarPassword.setText("");
            tilConfirmarPassword.setError(null);
        }
    }

    private void submeter() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmarPassword = etConfirmarPassword.getText().toString().trim();

        tilNome.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmarPassword.setError(null);

        if (modoRegisto && nome.isEmpty()) {
            tilNome.setError("Indica o teu nome");
            return;
        }
        if (email.isEmpty()) {
            tilEmail.setError("Indica o teu e-mail");
            return;
        }

        if (modoRegisto) {
            String erroSenha = validarSenhaRegisto(password);
            if (erroSenha != null) {
                tilPassword.setError(erroSenha);
                return;
            }
            if (!password.equals(confirmarPassword)) {
                tilConfirmarPassword.setError("As palavras-passe não coincidem");
                return;
            }
        } else if (password.isEmpty()) {
            tilPassword.setError("Indica a tua palavra-passe");
            return;
        }

        setLoading(true);

        SupabaseAuth.AuthCallback callback = new SupabaseAuth.AuthCallback() {
            @Override
            public void onSuccess(String userId, String userEmail) {
                setLoading(false);
                Toast.makeText(
                        LoginActivity.this,
                        modoRegisto ? "Conta criada com sucesso!" : "Bem-vindo(a)!",
                        Toast.LENGTH_SHORT
                ).show();
                irParaAppPrincipal();
            }

            @Override
            public void onError(String message) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };

        if (modoRegisto) {
            supabaseAuth.signUp(nome, email, password, callback);
        } else {
            supabaseAuth.signIn(email, password, callback);
        }
    }

    private String validarSenhaRegisto(String senha) {
        if (senha.length() < MIN_PASSWORD_LENGTH) {
            return "Mínimo de " + MIN_PASSWORD_LENGTH + " caracteres";
        }
        if (!senha.matches(".*[A-Z].*")) {
            return "Inclui pelo menos uma letra maiúscula";
        }
        if (!senha.matches(".*[^a-zA-Z0-9].*")) {
            return "Inclui pelo menos um carácter especial (ex.: ., _, @, !)";
        }
        return null;
    }

    private void mostrarDialogoRecuperarSenha() {
        final EditText inputEmail = new EditText(this);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputEmail.setHint("o.teu.email@exemplo.com");
        inputEmail.setText(etEmail.getText().toString().trim());

        new AlertDialog.Builder(this)
                .setTitle("Esqueceu a senha?")
                .setMessage("Introduz o teu e-mail para receberes o link de recuperação.")
                .setView(inputEmail)
                .setPositiveButton("Enviar", (dialog, which) -> {
                    String emailRecuperacao = inputEmail.getText().toString().trim();
                    if (emailRecuperacao.isEmpty()) {
                        Toast.makeText(this, "Indica um e-mail válido.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setLoading(true);
                    supabaseAuth.resetPassword(emailRecuperacao, new SupabaseAuth.SimpleCallback() {
                        @Override
                        public void onSuccess(String message) {
                            setLoading(false);
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(String message) {
                            setLoading(false);
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnEntrar.setEnabled(!loading);
        tvAlternarModo.setEnabled(!loading);
        tvEsqueciSenha.setEnabled(!loading);
        etNome.setEnabled(!loading);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
        etConfirmarPassword.setEnabled(!loading);
    }

    private void irParaAppPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}