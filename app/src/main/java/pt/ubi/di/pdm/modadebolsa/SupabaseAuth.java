package pt.ubi.di.pdm.modadebolsa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseAuth {

    private static final String SUPABASE_URL = "https://vjqvzvoijendzefjpfbp.supabase.co";
    private static final String SUPABASE_ANON_KEY = "sb_publishable_TTBDalIlfgb0pzXIq5Bnrw_eXrnQgM6";

    private static final String PREFS_NAME = "moda_bolso_auth";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final SharedPreferences prefs;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface AuthCallback {
        void onSuccess(String userId, String email);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public SupabaseAuth(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void signUp(String nome, String email, String password, AuthCallback callback) {
        try {
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("email", email);
            bodyJson.put("password", password);

            if (nome != null && !nome.trim().isEmpty()) {
                JSONObject dataJson = new JSONObject();
                dataJson.put("nome", nome.trim());
                bodyJson.put("data", dataJson);
            }

            sendAuthRequest("/auth/v1/signup", bodyJson, email, nome, callback);
        } catch (JSONException e) {
            callback.onError("Erro ao preparar os dados de registo.");
        }
    }

    public void signIn(String email, String password, AuthCallback callback) {
        try {
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("email", email);
            bodyJson.put("password", password);

            sendAuthRequest("/auth/v1/token?grant_type=password", bodyJson, email, null, callback);
        } catch (JSONException e) {
            callback.onError("Erro ao preparar os dados de login.");
        }
    }

    public void resetPassword(String email, SimpleCallback callback) {
        try {
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("email", email);

            RequestBody body = RequestBody.create(bodyJson.toString(), JSON);
            Request request = new Request.Builder()
                    .url(SUPABASE_URL + "/auth/v1/recover")
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    mainHandler.post(() -> callback.onError("Erro de rede: " + e.getMessage()));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    if (!response.isSuccessful()) {
                        String msg = extractErrorMessage(responseBody, response.code());
                        mainHandler.post(() -> callback.onError(msg));
                        return;
                    }
                    mainHandler.post(() -> callback.onSuccess("Enviámos um e-mail para redefinir a senha."));
                }
            });
        } catch (JSONException e) {
            callback.onError("Erro ao enviar pedido de recuperação.");
        }
    }

    private void sendAuthRequest(String endpoint, JSONObject bodyJson, String fallbackEmail, String fallbackName, AuthCallback callback) {
        RequestBody body = RequestBody.create(bodyJson.toString(), JSON);
        Request request = new Request.Builder()
                .url(SUPABASE_URL + endpoint)
                .addHeader("apikey", SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() -> callback.onError("Erro de rede: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                if (!response.isSuccessful()) {
                    String msg = extractErrorMessage(responseBody, response.code());
                    mainHandler.post(() -> callback.onError(msg));
                    return;
                }

                try {
                    JSONObject json = new JSONObject(responseBody);
                    String accessToken = json.optString("access_token", "");
                    JSONObject userObj = json.optJSONObject("user");
                    String userId = userObj != null ? userObj.optString("id", "") : json.optString("id", "");
                    String userEmail = userObj != null ? userObj.optString("email", fallbackEmail) : fallbackEmail;

                    String nomeGuardado = fallbackName;
                    if ((nomeGuardado == null || nomeGuardado.isEmpty()) && userObj != null) {
                        JSONObject meta = userObj.optJSONObject("user_metadata");
                        if (meta != null) {
                            nomeGuardado = meta.optString("nome", "");
                        }
                    }

                    SharedPreferences.Editor editor = prefs.edit()
                            .putString(KEY_USER_ID, userId)
                            .putString(KEY_USER_EMAIL, userEmail);

                    if (!accessToken.isEmpty()) {
                        editor.putString(KEY_ACCESS_TOKEN, accessToken);
                    }
                    if (nomeGuardado != null && !nomeGuardado.isEmpty()) {
                        editor.putString(KEY_USER_NAME, nomeGuardado);
                    }
                    editor.apply();

                    mainHandler.post(() -> callback.onSuccess(userId, userEmail));
                } catch (JSONException e) {
                    mainHandler.post(() -> callback.onError("Erro ao processar resposta do servidor."));
                }
            }
        });
    }

    public boolean isLoggedIn() {
        String token = prefs.getString(KEY_ACCESS_TOKEN, null);
        return token != null && !token.isEmpty();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    private String extractErrorMessage(String responseBody, int statusCode) {
        try {
            JSONObject err = new JSONObject(responseBody);
            if (err.has("msg")) return err.getString("msg");
            if (err.has("error_description")) return err.getString("error_description");
            if (err.has("message")) return err.getString("message");
        } catch (JSONException ignored) {
        }
        return "Falha na autenticação (código " + statusCode + ").";
    }
}