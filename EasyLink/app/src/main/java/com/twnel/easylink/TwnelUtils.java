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

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String TWNEL_PLAY_STORE_URL = "market://details?id=com.twnel.android&referrer=";
    public static final String COMPANY_ID = "companyId";
    public static final String INVITATION_CODE = "invitationCode";

    /**
     * Method to navigate to chat room in Twnel App
     *
     * @param context
     * @param companyId               example easytaxi
     * @param originPackageName       A fully-qualified package name for intent generation
     * @param originActivityClassName A fully-qualified Activity class name for intent generation
     */
    public static void navigateToChat(final Context context, final String companyId, final String invitationCode, final String originPackageName, final String originActivityClassName, final boolean showAlertDialog, final String alertTitle, final String alertSubject) {
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
                                            showAlertDialog(context, alertTitle, alertSubject, companyId, invitationCode);
                                        }
                                    } else {
                                        Intent browserIntent = null;
                                        try {
                                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWNEL_PLAY_STORE_URL + buildReferrerJSONString(companyId, invitationCode)));
                                            context.startActivity(browserIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                return null;
                            }
                        }
                );
    }


    public static String buildReferrerJSONString(String companyId, String invitationCode) throws JSONException {
        JSONObject referrerJSON = new JSONObject();
        referrerJSON.put(COMPANY_ID, companyId);
        referrerJSON.put(INVITATION_CODE, invitationCode);
        return referrerJSON.toString();
    }


    public static void showAlertDialog(final Context context, String title, String subject, final String companyId, final String companyInvitationCode) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(subject);
        alertDialog.setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = null;
                try {

                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWNEL_PLAY_STORE_URL + buildReferrerJSONString(companyId, companyInvitationCode)));
                    context.startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}
