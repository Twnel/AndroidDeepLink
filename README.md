Android DeepLink
==================

Twnel integration example using App Links.


Dependences 
==================
App Links SDK  for Android

http://boltsframework.github.io/docs/android/ 

1) Add the following line to your 'build.gradle' file

```groovy
compile 'com.parse.bolts:bolts-android:1.2.0'
```
or if you are using eclipse download the .jar and copy to the "libs" folder
http://search.maven.org/#browse%7C1700048106

2) Copy TwnelUtils.java to your project  

3) Call navigateToChat method from your activity or fragment 

```java

    /**
     * Navigate to the chat room inside Twnel.
     * @param context
     * @param companyId: The company identifier inside Twnel.
     * @param originPackageName: A fully-qualified package name for intent generation (used to return to your app).
     * @param originActivityClassName: A fully-qualified Activity class name for intent generation (used to return to your app).
     * @param showAlertDialog: True if you want to show an alert dialog before navigating to the Play Store to download Twnel, when the app is not installed.
     * @param alertTitle: The title of the alert dialog displayed when "showAlertDialog" is true.
     * @param alertSubject: The subject of the alert dialog displayed when "showAlertDialog" is true.
     * @param nextButtonText: The text for the "next" button used in the alert dialog when "showAlertDialog" is true.
     */
      public static void navigateToChat(Context context,String companyId, String originPackageName, String originActivityClassName,  boolean showAlertDialog, String alertTitle,String alertSubject,String nextButtonText) {
 	
 	//example 
 	
 	 TwnelUtils.navigateToChat(MainActivity.this, "easytaxi", "com.twnel.easylink", "com.twnel.easylink.MainActivity",true,"Chatea gratis descargando Twnel","1.) Da click en \"Siguiente\".\n" +
                "2.) Inicia Descarga Twnel en PlayStore\n" +
                "3.) Comunicate gratis con la central Easy Taxi 24 horas al dias 7 dias a la semana.","Siguiente");


```
4) add ``` android:exported="true"``` to your activity (originActivityClassName) in the AndroidManifest.xml
About App Links 
==================
http://applinks.org/
