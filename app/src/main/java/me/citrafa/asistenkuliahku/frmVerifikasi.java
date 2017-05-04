package me.citrafa.asistenkuliahku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import me.citrafa.asistenkuliahku.ActivityClass.Dashboard;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;
import me.citrafa.asistenkuliahku.Webservice.Webservice_Controller;

public class frmVerifikasi extends AppCompatActivity {
    private static String TAG = "";
    private String emails;
    private String kdVerifikasi;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_verifikasi);

        Bundle extra = getIntent().getExtras();
        emails = extra.getString("Email");

        final TextView txtEmail = (TextView)findViewById(R.id.lblEmailForVerify);
        txtEmail.setText(emails);

        final EditText kdKonfirm = (EditText)findViewById(R.id.txtKodeVerif);
        Button btnVerif = (Button)findViewById(R.id.btnInputVerifikasi);
        btnVerif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String kd = kdKonfirm.getText().toString();
                doVerif(email,kd);
            }
        });


    }

    private void doVerif(final String email, final String kode){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Webservice_Controller.URL_VERIFIKASI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObjs = new JSONObject(response);
                    boolean error = jObjs.getBoolean("error");
                    int status_verifikasi = jObjs.getInt("status_verifikasi");
                    if (!error){
                        if (status_verifikasi == 1) {
                            session.setLogin(true);
                            Intent i = new Intent(frmVerifikasi.this, Dashboard.class);
                            finish();
                        }
                    }else{
                        String errorMsg = jObjs.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "JSONNYA ERROR BANG", Toast.LENGTH_LONG).show();
                }

            } }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
        ){

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("kode_verifikasi", kode);

                    return params;
                }

            };

    }

}

