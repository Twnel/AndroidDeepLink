Easy-Link
==================

Twnel integration example using App Links.


Dependences 
==================
App Links SDK  for Android

http://boltsframework.github.io/docs/android/ 

Add the following line to your 'build.gradle' file

```groovy
compile 'com.parse.bolts:bolts-android:1.1.4'
```

call  navigateToChat method in TwnelUtils.java from your activity or fragment 

```java

    /**
     * Method to navigate to chat room in Twnel App
     *
     * @param context
     * @param companyId               example easytaxi
     * @param originPackageName       A fully-qualified package name for intent generation (for back your app)
     * @param originActivityClassName A fully-qualified Activity class name for intent generation (for back your app)
     */
     public static void navigateToChat( Context context,  String companyId,String invitationCode, String originPackageName, String originActivityClassName) 


 	//example 
 	 Utils.navigateToChat(MainActivity.this,"easytaxi","EASYTAXI", "com.twnel.easylink", "com.twnel.easylink.MainActivity");


```


About App Links 
==================
http://applinks.org/
