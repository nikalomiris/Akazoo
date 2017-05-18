package journey.forjobs.akazoo_project.rest;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import journey.forjobs.akazoo_project.BuildConfig;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.utils.Const;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class RestClient {

  private static RestAPI REST_API;

  static {
    setupRestClient();
  }


  public static RestAPI call() {
    sendSucessfullBroadcastMessage(Const.SHOW_SPINNER);
    return REST_API;
  }

  private static void setupRestClient() {

    OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
        .connectTimeout(Const.REST_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Const.REST_READ_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build();

    final Retrofit retorift = new Retrofit.Builder()
        .baseUrl(BuildConfig.HOST)
        .client(mOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    REST_API = retorift.create(RestAPI.class);
  }

  private static void sendSucessfullBroadcastMessage(String message) {
    Intent intent = new Intent(Const.CONTROLLER_SUCCESSFULL_CALLBACK);
    intent.putExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE, message);
    LocalBroadcastManager.getInstance(AkazooApplication.getInstance().getApplicationContext())
        .sendBroadcast(intent);
  }


}




