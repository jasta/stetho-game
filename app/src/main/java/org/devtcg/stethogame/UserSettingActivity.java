package org.devtcg.stethogame;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by apetrescu on 8/7/15.
 */
public class UserSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}
