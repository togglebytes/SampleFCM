package samplefcm.com.samplefcm;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    private String newToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                /* new TokenSend().execute();*/
                // registerToken(newToken);
                sendToken();

            }
        });
    }


    public void sendToken() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://togglebits.in/pet-health/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofit.create(RetroApi.class).setNotification(newToken, "Sam Testing").enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("sam", response.raw().toString());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                Log.d("sam", t.getMessage());
            }
        });
    }

    interface RetroApi {
        @FormUrlEncoded
        @POST("store")
        Call<Result> setNotification(@Field("token") String token, @Field("notification") String string);
    }

    class Result {
        public String message;
        public boolean success;

    }

    class Token{
        public String token;
        public String notification;
    }

}
    /*class TokenSend extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            Log.d("sam",newToken);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("token",newToken)
                    .add("notification","Sushil")
                    .build();

            Request request = new Request.Builder()
                    .url("http://togglebits.in/pet-health/store")
                    .post(body)
                    .build();

            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

}*/
