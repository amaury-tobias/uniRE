package me.amaurytq.unire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUser) EditText etUser;
    @BindView(R.id.etPassword) EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.App);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Constants.TOKEN = sharedPreferences.getString(Constants.BEARER_TOKEN, "");
        if (!Constants.TOKEN.equals("")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }


    @OnClick(R.id.btnLogin)
    public void onBtnLoginClick(View view) {
        String url = "http://192.168.1.46:3300/login";
        try {
            if (!etUser.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
                volleyLoginRequest(url);
            else showToast("Ingrese sus credenciales");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void volleyLoginRequest(String url) throws JSONException {
        String REQUEST_TAG = "me.amaurytq.unire.volleyLoginRequest";

        JSONObject postParams = new JSONObject();
        postParams.put("username", etUser.getText());
        postParams.put("password", etPassword.getText());

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.POST,
                url, postParams,
                this::responseHandler,
                this::errorHandler
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        showProgressDialog();
        AppController.getInstance().addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    private void errorHandler (VolleyError error) {
        dismissProgressDialog();
        showToast("Error al conectarse con el servidor");
    }

    private void responseHandler (JSONObject response) {
        dismissProgressDialog();
        String toastMessage = "";
        String token;
        try {
            String message = response.getString("message");
            if (message.equals("ok")) {
                toastMessage = "Bienvenido";
                token = response.getString("token");
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                sharedPreferences.edit().putString(Constants.BEARER_TOKEN, token).apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            if (message.equals("error")) toastMessage = "Verifique sus credenciales";
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            showToast(toastMessage);
        }
    }

    private void showToast (String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private ProgressDialog progressDialog = null;

    private void showProgressDialog () {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog () {
        progressDialog.dismiss();
    }
}
