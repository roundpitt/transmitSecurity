package com.transmit.authenticationcontroldemo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import com.ts.common.api.SDKBase;
import com.ts.common.api.TSLog;
import com.ts.sdk.api.SDK;

import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AuthControlApplication extends Application {

    private static final String TAG = AuthControlApplication.class.getName();

    private static final CookieManager sCookieManager = new CookieManager();

    private static AuthControlApplication instance;
    private String mTsAuthToken;

    public void setTSAuthToken(String _token) {
        mTsAuthToken = _token;
    }

    public String getTSAuthToken() {
        return mTsAuthToken;
    }

    public String getPrefsServerUri() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_SERVER_URI, "test.transmitsecurity.net");
    }

    public String getPrefsServerPathPrefix() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_SERVER_PATH_PREFIX, null);
    }

    public String getPrefsHttpPort() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_HTTP_PORT, "8080");
    }

    public String getPrefsHttpsPort() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_HTTPS_PORT, "8443");
    }

    public boolean getPrefsIsSecuredConnection() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.contains(SettingsFragment.PREF_KEY_IS_SECURE) ? prefs.getBoolean(SettingsFragment.PREF_KEY_IS_SECURE, false) : Boolean.FALSE;
    }

    public String getPrefsApiTokenName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_API_TOKEN_NAME, "demo");
    }

    public String getPrefsApiToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_API_TOKEN, "demo");
    }

    public String getPrefsAppID() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_APP_ID, "mobile_app");
    }

    public String getPrefsFaceAuthLicense() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_FACEAUTH_LICENSE, "kFNac8YjzPvEnD9v");
    }

    public String getPrefsEyeprintAuthLicense() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(SettingsFragment.PREF_KEY_EYEAUTH_LICENSE, "ATSS8V");
    }

    private TrustManager[] createAllTrustingTrustManager() {
        return new TrustManager[] {
            new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
        };
    }

    private KeyManager[] loadKeyManagers(@RawRes int id, String _password) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream instream = getResources().openRawResource(id);
            keyStore.load(instream, _password.toCharArray());
            instream.close();

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, _password.toCharArray());
            return kmf.getKeyManagers();
        } catch (Throwable t) {
            throw new IllegalArgumentException("Could not initiate key store", t);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        CookieHandler.setDefault(sCookieManager);

        SDK.ConnectionDetails connectionDetails = new SDK.ConnectionDetails(getPrefsServerUri(),
                getPrefsServerPathPrefix(),
                getPrefsHttpPort(),
                getPrefsHttpsPort(),
                getPrefsIsSecuredConnection());

        SDK.AuthenticatorsProperties authenticatorsProperties = new SDK.AuthenticatorsProperties(getPrefsFaceAuthLicense(), getPrefsEyeprintAuthLicense());

        List<String> pinnedCertHashes = null;

//        if (connectionDetails.isSecure) {

//            connectionDetails.setSslParams(loadKeyManagers(R.raw.client_p12, "transmit"), createAllTrustingTrustManager());

//            pinnedCertHashes = new LinkedList<>();
//            pinnedCertHashes.add("sha1/S9hPC7oSRfCpjEhvANpqGsnbNgI=");
//            pinnedCertHashes.add("sha1/SXxoaOSEzPC6BgGmxAt/EAcsajw=");
//        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SDKBase.UsernameValidator sampleUsernameValidator = new SDKBase.UsernameValidator() {

            private List<Character> mForbidden = new ArrayList<>(Arrays.asList('_', ' ', '/', '\\', '\'', '"', ':', '\t'));

            @NonNull
            @Override
            public Result validateUsername(@NonNull String _username) {
                for (Character c : mForbidden) {
                    if (_username.indexOf(c) != -1) {
                        return new Result(false, "Invalid username - must not include \"" + c + "\"");
                    }
                }

                return new Result(true);
            }
        };

        SDK.getInstance().initialize(this,
                getPrefsAppID(),
                getPrefsApiTokenName(),
                getPrefsApiToken(),
                connectionDetails,
                authenticatorsProperties, new SDK.InitializationListener() {
                    @Override
                    public void initialized() {
                        Log.d(TAG, "TS SDK Initialized successfully");
                    }

                    @Override
                    public void failedToInitialize(int _errorCode) {
                        Log.e(TAG, "TS SDK failed to initialize! Error code " + _errorCode);
                    }
                }, null, pinnedCertHashes, sampleUsernameValidator);

        SDK.getInstance().setLogLevel(TSLog.LOG_LEVEL_VERBOSE);
    }

    public static AuthControlApplication getInstance() {
        return instance;
    }
}
