package com.transmit.authenticationcontroldemo;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    public static final String PREF_KEY_APP_ID = "pref_key_app_id";

    public static final String PREF_KEY_SERVER_URI = "pref_key_server_uri";
    public static final String PREF_KEY_SERVER_PATH_PREFIX = "pref_key_server_path_prefix";
    public static final String PREF_KEY_HTTP_PORT = "pref_key_http_port";
    public static final String PREF_KEY_HTTPS_PORT = "pref_key_https_port";
    public static final String PREF_KEY_IS_SECURE = "pref_key_is_secure";
    public static final String PREF_KEY_API_TOKEN_NAME = "pref_key_api_token_name";
    public static final String PREF_KEY_API_TOKEN = "pref_key_api_token";
    public static final String PREF_KEY_FACEAUTH_LICENSE = "pref_key_faceauth_license";
    public static final String PREF_KEY_EYEAUTH_LICENSE = "pref_key_eyeauth_license";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference epref = (EditTextPreference) findPreference(PREF_KEY_APP_ID);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_SERVER_URI);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_SERVER_PATH_PREFIX);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_HTTP_PORT);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_HTTPS_PORT);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_API_TOKEN_NAME);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_API_TOKEN);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_FACEAUTH_LICENSE);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());

        epref = (EditTextPreference) findPreference(PREF_KEY_EYEAUTH_LICENSE);
        epref.setSummary(epref.getText());
        epref.setOnPreferenceChangeListener(new EditTextPreferenceSummaryUpdater());
    }

    private static class EditTextPreferenceSummaryUpdater implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // Set the value as the new value
            EditTextPreference pref = (EditTextPreference) preference;
            pref.setText(newValue.toString());
            // Get the entry which corresponds to the current value and set as summary
            preference.setSummary(pref.getText());
            return false;
        }
    }

}