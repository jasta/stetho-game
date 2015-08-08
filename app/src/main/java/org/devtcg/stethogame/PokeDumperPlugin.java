package org.devtcg.stethogame;

import com.facebook.stetho.dumpapp.ArgsHelper;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;

import java.util.Iterator;

public class PokeDumperPlugin implements DumperPlugin {
    private static final String NAME = "poke";

    public PokeDumperPlugin() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void dump(DumperContext dumpContext) throws DumpException {
        Iterator<String> args = dumpContext.getArgsAsList().iterator();
        String message = ArgsHelper.nextArg(args, "Usage: poke <message>");
        PokeManager.get().poke(message);
        dumpContext.getStdout().println("Poke sent!");
    }
}
