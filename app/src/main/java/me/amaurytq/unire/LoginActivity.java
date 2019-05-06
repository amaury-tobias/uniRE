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

    private static final String PARAM_MATRICULA = "matricula";
    private static final String PARAM_PASSWORD = "password";

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
        try {
            if (!etUser.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
                volleyLoginRequest();
            else showToast("Ingrese sus credenciales");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void volleyLoginRequest() throws JSONException {
        String REQUEST_TAG = "me.amaurytq.unire.volleyLoginRequest";

        JSONObject postParams = new JSONObject();
        postParams.put(PARAM_MATRICULA, etUser.getText());
        postParams.put(PARAM_PASSWORD, etPassword.getText());

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.POST,
                Constants.LOGIN_URL,
                postParams,
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

    private void errorHandler(VolleyError error) {
        dismissProgressDialog();
        error.printStackTrace();
        showToast("Verifique sus credenciales o intente de nuevo mas tarde");
    }

    private void responseHandler (JSONObject response) {
        dismissProgressDialog();
        String toastMessage = "";
        String token;
        try {
            token = response.getString("token");

            toastMessage = "Bienvenido";
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            sharedPreferences.edit().putString(Constants.BEARER_TOKEN, token).apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        } catch (JSONException e) {
            e.printStackTrace();
            toastMessage = "Error al obtener el token de sesión intente de nuevo mas tarde";
        } catch (Exception e) {
            e.printStackTrace();
            toastMessage = "Verifique sus credenciales";
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
