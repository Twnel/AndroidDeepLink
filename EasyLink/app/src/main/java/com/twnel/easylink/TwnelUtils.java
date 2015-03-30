package com.twnel.easylink;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import bolts.AppLink;
import bolts.AppLinkNavigation;
import bolts.Continuation;
import bolts.Task;
import bolts.WebViewAppLinkResolver;

/**
 * Created by Yesid Lazaro on 2/6/15.
 */
public class TwnelUtils {
    private static final String APP_LINK_JID = "app_link_jid";
    private static final String APP_PACKAGE_NAME = "app_package_name";
    private static final String APP_ACTIVITY_NAME = "app_activity_name";
    private static final String AL_APPLINK_DATA = "al_applink_data";

    //URL for App Links
    private static final String TWNEL_URL = "http://twnel.com";
    private static final String INSTALLATION_LINK = "http://twnl.co/";
    public static final String COMPANY_ID = "companyId";

    /**
     * Method to navigate to chat room in Twnel App
     * @param context
     * @param companyId  the valid companyId
     * @param originPackageName  A fully-qualified package name for intent generation (for back your app)
     * @param originActivityClassName A fully-qualified Activity class name for intent generation (for back your app)
     * @param showAlertDialog  set true for show a alert dialog before navigate to play Store to download Twnel App if it is not installed
     * @param alertTitle  the title for the alert dialog if  "showAlertDialog" is true
     * @param alertSubject the subject for the alert dialog if  "showAlertDialog" is true
     * @param nextButtonText text for "next" button  in the alert dialog if  "showAlertDialog" is true
     */
    public static void navigateToChat(final Context context, final String companyId, final String originPackageName, final String originActivityClassName, final boolean showAlertDialog, final String alertTitle, final String alertSubject,final String nextButtonText) {
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
                                    if (showAlertDialog) {
                                        if (TextUtils.isEmpty(alertTitle) || TextUtils.isEmpty(alertSubject)) {
                                            throw new IllegalArgumentException("please set a title and a subject for the alert");
                                        } else {
                                            showAlertDialog(context, alertTitle, alertSubject,nextButtonText, companyId);
                                        }
                                    } else {
                                        Intent browserIntent = null;
                                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(INSTALLATION_LINK + companyId));
                                        context.startActivity(browserIntent);
                                    }
                                }
                                return null;
                            }
                        }
                );
    }


    public static void showAlertDialog(final Context context, String title, String subject,String nextButtonText, final String companyId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(subject);
        alertDialog.setPositiveButton(nextButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(INSTALLATION_LINK + companyId));
                context.startActivity(browserIntent);
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}
