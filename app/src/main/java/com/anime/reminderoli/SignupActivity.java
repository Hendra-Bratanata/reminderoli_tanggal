package com.anime.reminderoli;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.nama)
    EditText mNama;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.Password)
    EditText mPass;

    @BindView(R.id.button_signup)
    Button btnLogin;
    @BindView(R.id.loading)
    ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        loading.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                String nama = mNama.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

            if(TextUtils.isEmpty(nama)){
                mNama.setError("Tidak Boleh Kosong");
            }
            if(TextUtils.isEmpty(pass)){
                    mPass.setError("Tidak Boleh Kosong");
            }
            if(TextUtils.isEmpty(email)){
                mEmail.setError("Tidak Boleh Kosong");
            }
            else {
                MD5 md5 = new MD5();

                signUp(nama,md5.EncriptMD5(pass),email);
            }



            }
        });
    }

    private void signUp(String nama, String pass, String email) {

        String URL = "http://reminder.96.lt/setUser.php?user=";
        URL += nama;
        URL += "&pass="+pass;
        URL += "&email="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Log.d("TAg",response);
                if (response.equals("Anda Sudah Terdaftar")){
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignupActivity.this, "Silahkan Login menggunakan akun Anda", Toast.LENGTH_LONG).show();
                  finis();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

   void finis(){
        this.onBackPressed();
        this.finish();
   }
}
