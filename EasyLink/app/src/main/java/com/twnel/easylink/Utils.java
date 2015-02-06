package com.twnel.easylink;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import bolts.AppLink;
import bolts.AppLinkNavigation;
import bolts.Continuation;
import bolts.Task;
import bolts.WebViewAppLinkResolver;

/**
 * Created by Yesid Lazaro on 2/6/15.
 */
public class Utils {
    private static final String APP_LINK_JID = "app_link_jid";
    private static final String APP_PACKAGE_NAME = "app_package_name";
    private static final String APP_ACTIVITY_NAME = "app_activity_name";
    private static final String AL_APPLINK_DATA = "al_applink_data";

    //URL for App Links
    private static final String TWNEL_URL = "http://twnel.com";
    private static final String TWNEL_PLAY_STORE_URL = "market://details?id=com.twnel.android&referrer=";

    /**
     * Method to navigate to chat room in Twnel App
     *
     * @param context
     * @param companyId               example easytaxi
     * @param originPackageName       A fully-qualified package name for intent generation
     * @param originActivityClassName A fully-qualified Activity class name for intent generation
     */
    public static void navigateToChat(final Context context, final String companyId, final String originPackageName, final String originActivityClassName) {
        new WebViewAppLinkResolver(context)
                .getAppLinkFromUrlInBackground(Uri.parse(TWNEL_URL))
                .continueWith(
                        new Continuation<AppLink, AppLinkNavigation.NavigationResult>() {
                            @Override
                            public AppLinkNavigation.NavigationResult then(
                                    Task<AppLink> task) {
                                AppLink link = task.getResult();
                                Intent intent = new Intent();
                                AppLink.Target target = link.getTargets().get(0);
                                intent.setClassName(target.getPackageName(), target.getClassName());
                                ResolveInfo resolveInfo = context.getPackageManager()
                                        .resolveActivity(intent,
                                                PackageManager.MATCH_DEFAULT_ONLY);
                                //Twnel App installed
                                if (resolveInfo != null) {
                                    Bundle extras = new Bundle();
                                    //company Id, package name and activity class name
                                    extras.putString(APP_LINK_JID, companyId);
                                    extras.putString(APP_PACKAGE_NAME, originPackageName);
                                    extras.putString(APP_ACTIVITY_NAME, originActivityClassName);

                                    //Extras for detect in Twnel App if starting from App Link
                                    intent.putExtra(AL_APPLINK_DATA, extras);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    //Twnel App not installed(now we redirect to play store)
                                } else {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWNEL_PLAY_STORE_URL + companyId));
                                    context.startActivity(browserIntent);
                                }
                                return null;
                            }
                        }
                );
    }
}
