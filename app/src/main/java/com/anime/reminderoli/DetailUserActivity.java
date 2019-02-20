package com.anime.reminderoli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailUserActivity extends AppCompatActivity {
    final static String getData = "mobil";
    Mobil mobil;
    FirebaseJobDispatcher mDispacher;
    @BindView(R.id.detail_username)
    TextView mUser;
    @BindView(R.id.detail_nopol)
    TextView mNoPol;
    @BindView(R.id.detail_jenisoli)
    TextView mOli;
    @BindView(R.id.detail_kilometersekarang)
    TextView kmAwal;
    @BindView(R.id.detaiKmService)
    TextView kmAkhir;
    @BindView(R.id.detaiNamaMobil)
    TextView mNamaMobil;
    @BindView(R.id.detaiJenisMobil)
    TextView mJenisMobil;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.loadingDetail)
    ProgressBar loading;
    @BindView(R.id.edt_button_detail)
    Button edtbutton;
    @BindView(R.id.hps_button_detail)
    Button hpsbutton;
    @BindView(R.id.tglservice)
            TextView tglService;


    String URL = "http://reminder.96.lt/getMobil.php";
    String nopol ;
    Mobil newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        ButterKnife.bind(this);
        mDispacher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        mobil = getIntent().getParcelableExtra(getData);
        edtbutton.setVisibility(View.GONE);
        hpsbutton.setVisibility(View.GONE);
        loadData(mobil);

        //loadUser();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(mobil);
            }
        });

    }


    private void loadData(final Mobil user) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newUser = new Mobil();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        Mobil userUrl = new Mobil(object1);


                        if (userUrl.getId_user().equals(user.getId_user())) {
                            loading.setVisibility(View.GONE);
                            newUser = userUrl;



                        }

                    }
                    startDispatcher();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //isi data disini
                nopol=newUser.getNoPol();
                mUser.setText("Id USer\t\t\t\t\t\t\t: " + newUser.getId_user());
                mNoPol.setText("No Polisi\t\t\t\t\t\t\t: " + newUser.getNoPol());
                kmAkhir.setText("Km Service\t\t\t: " + newUser.getKmService());
                kmAwal.setText("Km Awal\t\t\t\t\t: " + newUser.getKmSekarang());
                mOli.setText("Id Oli\t\t\t\t\t: " + newUser.getId_oli());
                mJenisMobil.setText("Jenis mobil \t\t\t: " + newUser.getJenisMobil());
                mNamaMobil.setText("nama Mobil\t\t\t: " + newUser.getNamaMobil());
                tglService.setText("tgl Service\t\t\t"+newUser.getTanggalService());
                swipe.setRefreshing(false);

                edtbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailUserActivity.this,EditActivity.class);
                        startActivity(intent);
                    }
                });


                if (mobil.getStatus().equalsIgnoreCase("admin")){
                    edtbutton.setVisibility(View.VISIBLE);
                    hpsbutton.setVisibility(View.VISIBLE);
                }
                hpsbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadHapus();
                    }
                });



            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailUserActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(DetailUserActivity.this);
        requestQueue.add(stringRequest);

    }
    public void loadUser (){
        String link = "reminder.96.lt/getUser.php";
        StringRequest req = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            User userbaru = new User();
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj2 = array.getJSONObject(i);
                        User userJSON = new User(obj2);

                        if (userJSON.getIdUser().equalsIgnoreCase(mobil.getId_user())){
                            if(userJSON.getStatus().equalsIgnoreCase("admin")){
                                edtbutton.setVisibility(View.VISIBLE);
                                hpsbutton.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }







            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue quen = Volley.newRequestQueue(this);
        quen.add(req);
    }
    public void LoadHapus(){
        Log.d("TAg", "LoadHapus: "+nopol);
        String URL2 = "http://reminder.96.lt/setHapusMobil.php?no=";
        URL2 +=nopol;
        Log.d("TAg", "LoadHapus: "+URL2);
        StringRequest hapus = new StringRequest(Request.Method.GET, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue quen = Volley.newRequestQueue(this);
        quen.add(hapus);


    }

    public void startDispatcher(){
        Log.d("TAG", "startDispatcher: ");

        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("nopol",newUser.getNoPol());

        Job myjob = mDispacher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("TAG")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0,5))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK,Constraint.DEVICE_CHARGING,Constraint.DEVICE_IDLE


                )
                .setExtras(myExtrasBundle)
                .build();
        mDispacher.mustSchedule(myjob);

    }

    public void cancelDispacher(){
        mDispacher.cancel("TAG");
        Log.d("TAG", "cancelDispacher: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notif,menu);
    return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.on:

                startDispatcher();
                Toast.makeText(this,"Notifikasi telah dihidupkan",Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.off:
                cancelDispacher();
                Toast.makeText(this,"Notifikasi telah dimatikan",Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.out:
                cancelDispacher();
                Toast.makeText(this,"Logout finish",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
        }    return super.onOptionsItemSelected(item);
    }
}