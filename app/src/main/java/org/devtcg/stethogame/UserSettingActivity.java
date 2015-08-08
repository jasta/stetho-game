package org.devtcg.stethogame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserSettingActivity extends PreferenceActivity {

    public static void show(Activity context) {
        context.startActivity(new Intent(context, UserSettingActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}
