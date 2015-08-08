package org.devtcg.stethogame;

import android.content.Context;

import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;

import java.io.PrintWriter;
import java.util.List;

public class LoginDumperPlugin implements DumperPlugin {
    private static final String NAME = "login";

    private final Context mContext;

    public LoginDumperPlugin(Context context) {
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
        if (args.size() != 1) {
            writer.println("Valid Usage: login [YourName]");
        } else {
            StethoGameApplication.loginToFragment(args.get(0));
        }
        writer.flush();
    }
}
