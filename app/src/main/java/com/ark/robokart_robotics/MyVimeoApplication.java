package com.ark.robokart_robotics;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.Vimeo;
import com.vimeo.networking.VimeoClient;

public class MyVimeoApplication extends Application
{
    private static final String SCOPE = "private public interact";

    private static final boolean IS_DEBUG_BUILD = false;
    // Switch to true to see how access token auth works.
    private static final boolean ACCESS_TOKEN_PROVIDED = false;
    private static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;

        Configuration.Builder configBuilder;
        // This check is just as for the example. In practice, you'd use one technique or the other.
        if (ACCESS_TOKEN_PROVIDED)
        {
            configBuilder = getAccessTokenBuilder();
            Log.d("ACCESS_TOKEN", "PROVIDED");
        }
        else
        {
            configBuilder = getClientIdAndClientSecretBuilder();
            Log.d("ACCESS_TOKEN", "NOT PROVIDED");
        }
        if (IS_DEBUG_BUILD) {
            // Disable cert pinning if debugging (so we can intercept packets)
            configBuilder.enableCertPinning(false);
            configBuilder.setLogLevel(Vimeo.LogLevel.VERBOSE);
        }
        VimeoClient.initialize(configBuilder.build());
    }

    public Configuration.Builder getAccessTokenBuilder() {
        // The values file is left out of git, so you'll have to provide your own access token
        String accessToken = getString(R.string.access_token);
        return new Configuration.Builder(accessToken);
    }

    public Configuration.Builder getClientIdAndClientSecretBuilder() {
        // The values file is left out of git, so you'll have to provide your own id and secret
        String clientId = "0993a005480472a69fab10c2f9b8ad0d6bee7acf";//getString(R.string.client_id);
        String clientSecret="oVsWjoQ2RHeHvZ8xK3yrtdHrG7YiN+rnHh4qqBfmscDbCwplTFzytAoVIVrXMnAQShuBYuA6fZftYL+AIvX5zRP8JXOs06dQcej1yeL/ACJSGuiKoQJbqdC6CELuP+Pl";

        String codeGrantRedirectUri = "deva://robokart";
        Configuration.Builder configBuilder =
                new Configuration.Builder(clientId, clientSecret, SCOPE, null,
                        null);
        configBuilder.setCacheDirectory(this.getCacheDir())
                .setUserAgentString(getUserAgentString(this))
                // Used for oauth flow
                .setCodeGrantRedirectUri(codeGrantRedirectUri);

        return configBuilder;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static String getUserAgentString(Context context) {
        String packageName = context.getPackageName();

        String version = "unknown";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("Unable to get packageInfo: " + e.getMessage());
        }

        String deviceManufacturer = Build.MANUFACTURER;
        String deviceModel = Build.MODEL;
        String deviceBrand = Build.BRAND;

        String versionString = Build.VERSION.RELEASE;
        String versionSDKString = String.valueOf(Build.VERSION.SDK_INT);

        return packageName + " (" + deviceManufacturer + ", " + deviceModel + ", " + deviceBrand +
                ", " + "Android " + versionString + "/" + versionSDKString + " Version " + version +
                ")";
    }
}