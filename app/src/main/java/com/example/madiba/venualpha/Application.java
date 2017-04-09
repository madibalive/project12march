package com.example.madiba.venualpha;

import com.adobe.creativesdk.aviary.IAdobeAuthRedirectCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import timber.log.Timber;

import static com.example.madiba.venualpha.BuildConfig.ADOBE_KEY;
import static com.example.madiba.venualpha.BuildConfig.ADOBE_SECRET;
import static com.example.madiba.venualpha.BuildConfig.ADOBE_URL;


/**
 * Created by Madiba on 12/1/2016.
 */

public class Application extends android.app.Application implements IAdobeAuthClientCredentials, IAdobeAuthRedirectCredentials {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
//        HockeyLog.setLogLevel(Log.ERROR); // show only errors – the default log level
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("oARVgHNnYTJZH2xKhyoQBOzsyuQjHl0YwGt8V4J7")
                .clientKey("BR9uoqKFnvckxKjzeCR9Q9BoFaslIwa1xYsssufe")
                .server("https://parseapi.back4app.com/")
                .enableLocalDataStore()
                .build()
        );

        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
//


    }
    @Override
    public String getClientID() {
        return ADOBE_KEY;
    }

    @Override
    public String getClientSecret() {
        return ADOBE_SECRET;
    }

    @Override
    public String getRedirectUri() {
        return ADOBE_URL;
    }


}
