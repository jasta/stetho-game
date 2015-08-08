package org.devtcg.stethogame;

import android.content.Context;

import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;

import java.io.PrintWriter;
import java.util.List;

public class GoogleApiDumperPlugin implements DumperPlugin {
  private static final String NAME = "google";

  private final Context mContext;

  public GoogleApiDumperPlugin(Context context) {
    mContext = context;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void dump(DumperContext dumpContext) throws DumpException {
    List<String> args = dumpContext.getArgsAsList();
    PrintWriter writer = new PrintWriter(dumpContext.getStdout());
    try {
      GoogleApiClientInstance.get(mContext).dump(
          "",
          null /* fileDescriptor */,
          writer,
          args.toArray(new String[args.size()]));
    } finally {
      writer.flush();
    }
  }
}
