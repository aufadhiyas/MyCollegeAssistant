package me.citrafa.asistenkuliahku.ActivityClass;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.citrafa.asistenkuliahku.MyService;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.Service.GPSTracker;
import me.citrafa.asistenkuliahku.SessionManager.AppController;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;
import me.citrafa.asistenkuliahku.Webservice.Webservice_Controller;
import me.citrafa.asistenkuliahku.frmVerifikasi;

public class frmLogin extends AppCompatActivity {
    private  static final String TAG = frmDaftar.class.getSimpleName();
    Button btnLogin, btnRegister;
    private SessionManager session;
    private EditText txtEmail, txtPassword;
    private ProgressDialog pDialog;
    private TextView lupa;
    private static final int REQUEST_PERMISSIONS = 20;
    String provider;
    static double lat, lng;
    LocationManager locationManager;
    int MY_PERMISSION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmlogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnDaftar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        lupa = (TextView)findViewById(R.id.lblLupaPassword);
        lupa.setMovementMethod(LinkMovementMethod.getInstance());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(frmLogin.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){

        }else {
            gps.showSettingsAlert();
        }


        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intents = new Intent(frmLogin.this, Dashboard.class);
            startActivity(intents);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmLogin.this,
                        Dashboard.class);
                startActivity(intent);
//                String email = txtEmail.getText().toString().trim();
//                String pass = txtPassword.getText().toString().trim();
//                Boolean status= true;
//                if(!email.isEmpty() && !pass.isEmpty()){
//
//                    checkLogin(email,pass,status);
//                }else{
//                    Toast.makeText(getApplicationContext(), "Masukkan Email Dan Password Anda", Toast.LENGTH_LONG).show();
//                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmLogin.this, frmDaftar.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkLogin(final String email, final String password, final Boolean statusLogin) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Webservice_Controller.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObjs = new JSONObject(response);
                    boolean error = jObjs.getBoolean("error");
                    int status_verifikasi = jObjs.getInt("status_verifikasi");
                    String email = jObjs.getString("email");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        if (status_verifikasi == 0){
                            session.setVerifyStat(false);
                            Intent intent = new Intent(frmLogin.this, frmVerifikasi.class);
                            String Email = null;
                            intent.putExtra(email, Email);

                        }else{
                            session.setLogin(true);
                            Intent intent = new Intent(frmLogin.this,
                                    Dashboard.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    response, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObjs.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
