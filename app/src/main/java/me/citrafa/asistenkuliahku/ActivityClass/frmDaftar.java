package me.citrafa.asistenkuliahku.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.SessionManager.AppController;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;
import me.citrafa.asistenkuliahku.Webservice.Webservice_Controller;
import me.citrafa.asistenkuliahku.frmVerifikasi;

public class frmDaftar extends AppCompatActivity {
    Button btnDaftar, btnKembali;
    EditText email, pass, nama;
    AlertDialog alertDialog;
    private static final String TAG = "";
    SessionManager session;

    private String EmailView;
    private ProgressDialog pDialog;

    private static final String Key_nama = "nama";
    private static final String Key_email = "email";
    private static final String Key_password = "password";
    private static final String url = "Webservice_Controller.URL_DAFTAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmdaftar);
        btnDaftar = (Button) findViewById(R.id.btnDaftar1);
        btnKembali = (Button) findViewById(R.id.btnKembali);
        email = (EditText) findViewById(R.id.txtEmailDaftar);
        pass = (EditText) findViewById(R.id.txtPasswordDaftar);
        nama = (EditText) findViewById(R.id.txtNamaDaftar);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDaftar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String emails = email.getText().toString().trim();
                        String passwords = pass.getText().toString().trim();
                        String namas = nama.getText().toString().trim();
                        if (!namas.isEmpty() && !emails.isEmpty() && !passwords.isEmpty()) {

                            registerUser(namas,emails,passwords);

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter your details!", Toast.LENGTH_LONG)
                                    .show();
                        }
                        //registerUser(emails, passwords,namas);


                    }
                });
            }
        });
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(frmDaftar.this,
                        Dashboard.class);
                startActivity(intent);
            }
        });


    }



    private void initCustomAlertDialog(String EmailView) {
        //View v = getLayoutInflater().inflate(R.layout.dialogverivikasi, null);
        //TextView txtEmail = (TextView) findViewById(R.id.lblemailDaftar);
        //txtEmail.setText(EmailView);
        //alertDialog = new AlertDialog.Builder(this).create();
       // alertDialog.setView(v);
        //alertDialog.setTitle("Verifikasi Email");
    }
    private void registerUser( final String namas, final String emails,
                              final String passwords) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Webservice_Controller.URL_DAFTAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String status = jObj.getString("status_verifikasi");

                            Intent intent = new Intent(frmDaftar.this,frmVerifikasi.class);
                            intent.putExtra("status",status);
                            intent.putExtra("email",email);
                            intent.putExtra("nama",name);
                            startActivity(intent);
                            finish();


                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);



                        // Launch login activity
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(frmDaftar.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak Ada Koneksi Internet", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", namas);
                params.put("email", emails);
                params.put("password", passwords);

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
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void VerifikasiDialog(String Email){

    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}