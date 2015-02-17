Android DeepLink
==================

Twnel integration example using App Links.


Dependences 
==================
App Links SDK  for Android

http://boltsframework.github.io/docs/android/ 

1) Add the following line to your 'build.gradle' file

```groovy
compile 'com.parse.bolts:bolts-android:1.1.4'
```
or if you are using eclipse download the .jar and copy to the "libs" folder
http://search.maven.org/#browse%7C1700048106

2) Copy TwnelUtils.java to your project  

3) Call navigateToChat method from your activity or fragment 

```java

    /**
     * Method to navigate to chat room in Twnel App
     * @param context
     * @param companyId  the valid companyId
     * @param invitationCode a valid invitation code for your company
     * @param originPackageName  A fully-qualified package name for intent generation (for back your app)
     * @param originActivityClassName A fully-qualified Activity class name for intent generation (for back your app)
     * @param showAlertDialog  set true for show a alert dialog before navigate to play Store to download Twnel App if it is not installed
     * @param alertTitle  the title for the alert dialog if  "showAlertDialog" is true
     * @param alertSubject the subject for the alert dialog if  "showAlertDialog" is true
     * @param nextButtonText text for "next" button  in the alert dialog if  "showAlertDialog" is true
     */
      public static void navigateToChat(Context context,String companyId,String invitationCode, String originPackageName, String originActivityClassName,  boolean showAlertDialog, String alertTitle,String alertSubject,String nextButtonText) {
 	
 	//example 
 	
 	 TwnelUtils.navigateToChat(MainActivity.this, "easytaxi", "EASYTAXI", "com.twnel.easylink", "com.twnel.easylink.MainActivity",true,"Chatea gratis descargando Twnel","1.) Da click en \"Siguiente\".\n" +
                "2.) Inicia Descarga Twnel en PlayStore\n" +
                "3.) Comunicate gratis con la central Easy Taxi 24 horas al dias 7 dias a la semana.","Siguiente");


```

About App Links 
==================
http://applinks.org/
