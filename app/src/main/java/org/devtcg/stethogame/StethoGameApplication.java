package org.devtcg.stethogame;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

public class StethoGameApplication extends Application {

  public static final boolean USE_GOOGLE_PLAY = true;

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

    final Thread.UncaughtExceptionHandler previousHandler =
        Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread thread, Throwable ex) {
        Achievements.unlock(
            StethoGameApplication.this,
            Achievements.Achievement.CRASH);

        // Provide a more graceful experience allowing the user to notice the crash before
        // we go boom...
        try {
          Thread.sleep(4000, 0 /* nanos */);
        } catch (InterruptedException e) {
        }
        previousHandler.uncaughtException(thread, ex);
      }
    });

    // This blocks, *sigh*...
    Achievements.syncStateFromDisk(this);

    // ...let the fun begin :)
    Context context = this;
    Stetho.initialize(Stetho.newInitializerBuilder(context)
        .enableDumpapp(new GameDumperPluginProvider(context))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
        .build());
  }

  private static class GameDumperPluginProvider implements DumperPluginsProvider {
    private final Context mContext;

    public GameDumperPluginProvider(Context context) {
      mContext = context;
    }

    @Override
    public Iterable<DumperPlugin> get() {
      ArrayList<DumperPlugin> plugins = new ArrayList<>();
      for (DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
        plugins.add(defaultPlugin);
      }
      plugins.add(new GoogleApiDumperPlugin(mContext));
      plugins.add(new PokeDumperPlugin());
      return plugins;
    }
  }
}
