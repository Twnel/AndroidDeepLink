package com.twnel.easylink;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import bolts.AppLink;
import bolts.AppLinkNavigation;
import bolts.Continuation;
import bolts.Task;
import bolts.WebViewAppLinkResolver;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    private Button butChatNow;
    //URL for App Links
    private final String TWNEL_URL="http://twnel.com";
    private final String TWNEL_PLAY_STORE_URL="market://details?id=com.twnel.android&referrer=easytaxi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butChatNow= (Button) findViewById(R.id.but_chat);
        butChatNow.setOnClickListener(this);
    }

    /**
     * Method to navigate to Twnel App
     * @param companyId company identification in Twnel Service
     */
    private void navigateToChat(final String companyId){
        new WebViewAppLinkResolver(this)
                .getAppLinkFromUrlInBackground(Uri.parse(TWNEL_URL))
                .continueWith(
                        new Continuation<AppLink, AppLinkNavigation.NavigationResult>() {
                            @Override
                            public AppLinkNavigation.NavigationResult then(
                                    Task<AppLink> task) {
                                AppLink link = task.getResult();

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                AppLink.Target target = link.getTargets().get(0);
                                intent.setPackage(target.getPackageName());
                                intent.setData(link.getSourceUrl());

                                //lookup to detect if Twnel App is available to resolve intent
                                ResolveInfo resolveInfo = getPackageManager()
                                        .resolveActivity(intent,
                                                PackageManager.MATCH_DEFAULT_ONLY);
                                //Twnel App installed
                                if (resolveInfo != null) {
                                    Bundle extras = new Bundle();
                                    //company Id
                                    extras.putString("app_link_jid", companyId);
                                    //Extras for detect in Twnel App if starting from App Link
                                    intent.putExtra("al_applink_data", extras);
                                    startActivity(intent);
                                //Twnel App not installed(now we redirect to play store)
                                } else {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWNEL_PLAY_STORE_URL));
                                    startActivity(browserIntent);
                                }
                                return null;
                            }
                        }
                );
    }

    @Override
    public void onClick(View v) {
        //start navigation to easytaxi chat room in Twnel app
        navigateToChat("easytaxi");
    }
}
