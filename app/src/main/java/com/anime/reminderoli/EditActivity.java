package com.anime.reminderoli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity {

    @BindView(R.id.admin_iduser)
    Spinner spinnerIduser;
    @BindView(R.id.admin_idoli)
    Spinner spinnerIdoli;
    @BindView(R.id.admin_noPol)
    EditText anoPol;
    @BindView(R.id.admin_kmawal)
    EditText akmAwal;
    @BindView(R.id.admin_kmservice)
    EditText akmService;
    @BindView(R.id.admin_namamobil)
    EditText anamaMobil;
    @BindView(R.id.admin_jenismobil)
    EditText ajenisMobil;
    @BindView(R.id.admin_input_selesai)
    Button btnSelesai;


    ArrayList<Mobil> listMobil;
    ArrayList<String> listIdMobil;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> userData;
    ArrayList<String> userDataName;
    ArrayList<String> oliData;
    ArrayList<String> oliDataName;
    String idUser ;
    String idOli ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        listMobil = new ArrayList<>();
        listIdMobil = new ArrayList<>();
        listMobil = getIntent().getParcelableExtra("Mobil");


        LoadData();


    }

    public void SetAdmin(String user, String idOli, String noPol, String kmAwal, String kmService, String namaMobil, String jenisMobil) {

        String URL2 = "http://reminder.96.lt/setMobil2.php?user=";
        URL2 += user;
        URL2 += "&oli=" + idOli;
        URL2 += "&nopol=" + noPol;
        URL2 += "&kmawal=" + kmAwal;
        URL2 += "&kmservice=" + kmService;
        URL2 += "&namamobil=" + namaMobil;
        URL2 += "&jenismobil=" + jenisMobil;
        Log.d("TAG", "SetAdmin: " + URL2);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("TAg", response);
                if (response.equals("Terupdate")) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    void LoadData() {
        String URL = "http://reminder.96.lt/getUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userData = new ArrayList<>();
                userDataName = new ArrayList<>();

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        User user = new User(obj);
                        Log.d("TAG", "onResponse: " + user.getIdUser());


                        userData.add(user.getIdUser());
                        userDataName.add(user.getUserName());
                        Log.d("TAg", "onResponse: " + userData.get(i));


                    }
                    listAdapter = new ArrayAdapter<>(EditActivity.this, R.layout.spinner_item, userDataName);
                    listAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
                    spinnerIduser.setAdapter(listAdapter);
                    loadOli();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void loadOli() {

        String URL = "http://reminder.96.lt/getOli.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                oliData = new ArrayList<>();
                oliDataName = new ArrayList<>();

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array1 = obj.getJSONArray("data");
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj2 = array1.getJSONObject(i);
                        Log.d("TAG", "onResponse: " + obj2.getString("Id_Oli"));
                        oliData.add(obj2.getString("Id_Oli"));
                        oliDataName.add(obj2.getString("Merk_Oli"));
                        Log.d("TAG", "onResponse: " + oliData.get(i));
                    }

                    listAdapter = new ArrayAdapter<>(EditActivity.this, R.layout.spinner_item, oliDataName);
                    listAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
                    spinnerIdoli.setAdapter(listAdapter);


                    spinnerIduser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            idUser = userData.get(position);
                            Log.d("TAG", "onItemSelected: " + idUser);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            idUser = userData.get(0);
                            Log.d("TAG", "onItemSelected: " + idUser);
                        }
                    });
                    spinnerIdoli.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            idOli = oliData.get(position);
                            Log.d("TAG", "onItemSelected: " + idOli);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            idOli = oliData.get(0);
                            Log.d("TAG", "onItemSelected: " + idOli);

                        }
                    });
                    idUser = userData.get(0);
                    idOli = oliData.get(0);
                    btnSelesai.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            String noPol = anoPol.getText().toString().trim();
                            String kmAwal = akmAwal.getText().toString().trim();
                            String kmService = akmService.getText().toString().trim();
                            String namaMobil = anamaMobil.getText().toString().trim();
                            String jenisMobil = ajenisMobil.getText().toString().trim();

                            if (TextUtils.isEmpty(noPol)) {
                                anoPol.setError("Tidak Boleh Kosong");
                            }
                            if (TextUtils.isEmpty(kmAwal)) {
                                akmAwal.setError("Tidak Boleh Kosong");
                            }
                            if (TextUtils.isEmpty(kmService)) {
                                akmService.setError("Tidak Boleh Kosong");
                            }
                            if (TextUtils.isEmpty(namaMobil)) {
                                anamaMobil.setError("Tidak Boleh Kosong");
                            }
                            if (TextUtils.isEmpty(jenisMobil)) {
                                ajenisMobil.setError("Tidak Boleh Kosong");
                            } else {
                                SetAdmin(idUser, idOli, noPol, kmAwal, kmService, namaMobil, jenisMobil);
                            }

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

