package com.twnel.easylink;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import bolts.AppLink;
import bolts.AppLinkNavigation;
import bolts.Continuation;
import bolts.Task;
import bolts.WebViewAppLinkResolver;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    private Button butChatNow;
    private final String TWNEL_URL="http://twnel.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butChatNow= (Button) findViewById(R.id.but_chat);
        butChatNow.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void navigateToChat(final String companyId){
        Log.d("onClick","navigateToChat");
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

                                ResolveInfo resolveInfo = getPackageManager()
                                        .resolveActivity(intent,
                                                PackageManager.MATCH_DEFAULT_ONLY);
                                if (resolveInfo != null) {
                                    Bundle extras = new Bundle();
                                    extras.putString("target_url", TWNEL_URL);
                                    extras.putString("app_link_jid", companyId);
                                    extras.putBoolean("from_app_link", true);
                                    intent.putExtra("al_applink_data", extras);
                                    startActivity(intent);
                                } else {
                                    //todo start navigation to web browser with play store url and campaign parameters
                                }
                                return null;
                            }
                        }
                );
    }

    @Override
    public void onClick(View v) {
        navigateToChat("easytaxi");
    }
}
