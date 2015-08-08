package org.devtcg.stethogame;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

public class StethoGameApplication extends Application {

  private static OkHttpClient client;

  static {
    client = new OkHttpClient();
    client.networkInterceptors().add(new StethoInterceptor());
  }

  public static OkHttpClient getClient() {
    return client;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // ...let the fun begin :)
    Context context = this;
    Stetho.initialize(Stetho.newInitializerBuilder(context)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
        .build());
  }
}
