package org.devtcg.stethogame;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class StethoGameApplication extends Application {
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
