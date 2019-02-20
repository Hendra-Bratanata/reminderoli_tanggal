package com.anime.reminderoli;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MyJobService extends JobService {
    public final static String TAG = "TAG";

    @Override
    public boolean onStartJob(JobParameters job) {


        Log.d("TAG", "onStartJob: ");

        getCurrentWeather(job);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "onStopJob: ");
        return true;
    }


    private void getCurrentWeather(final JobParameters job) {
        Log.d(TAG, "getCurrentWeather: ");
        final String nopol = job.getExtras().getString("nopol");



        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://reminder.96.lt/getMobil.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: ");

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        Log.d(TAG, "onLoop: ");
                        JSONObject object = array.getJSONObject(i);
                        Mobil mobil = new Mobil(object);

                        if (mobil.getNoPol().equalsIgnoreCase(nopol)) {
                            //ambil nilai tanggal sekarang
                            Calendar calendar = Calendar.getInstance();
                            int hari = calendar.get(Calendar.DAY_OF_MONTH);
                            int bulan = calendar.get(Calendar.MONTH)+1;
                            int tahun = calendar.get(Calendar.YEAR);

                            Log.d(TAG, "Tanggal Hp: "+hari+":"+bulan+":"+tahun);
                            Log.d(TAG, "onIf" + mobil.getNoPol());
                            Log.d(TAG, "onIf" + nopol);
                            int kmService = Integer.parseInt(mobil.getKmService().trim());

                            int kmSekarang = Integer.parseInt(mobil.getKmSekarang().trim());
                            if(kmSekarang >= kmService ) {
                                int total = kmService - kmSekarang;
                                Log.d(TAG, "onResponse: " + total);
                                Log.d(TAG, "Km Cek ==============================");
                                Log.d(TAG, "kmSekarang " + kmSekarang);
                                Log.d(TAG, "kmService: " + kmService);
                                showNotification(getApplicationContext(), "Warning", "Sudah Waktunya Service km sekarang = " + kmSekarang, 100);
                                jobFinished(job, false);
                            }else if(kmSekarang >= kmService - 500){
                                int total = kmService - kmSekarang;
                                Log.d(TAG, "onResponse: " + total);
                                Log.d(TAG, "Km Cek ==============================");
                                Log.d(TAG, "kmSekarang " + kmSekarang);
                                Log.d(TAG, "kmService: " + kmService);
                                showNotification(getApplicationContext(), "Warning", "Sudah mendekati Wktu Service km sekarang = " + kmSekarang, 100);
                                jobFinished(job, false);
                            }


                        }
                        Log.d("TAG", "onResponse: " + mobil.getKmSekarang());



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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void showNotification(Context context, String tittle, String massage, int notifid) {
        Log.d(TAG, "showNotification: ");


        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(notifid))
                .setContentText(tittle)
                .setSmallIcon(R.drawable.ic_warning_black_24dp)
                .setColor(ContextCompat.getColor(context, android.R.color.holo_blue_bright))
                .setContentText(massage)

                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmsound);

        notificationManagerCompat.notify(notifid, builder.build());

    }
}
