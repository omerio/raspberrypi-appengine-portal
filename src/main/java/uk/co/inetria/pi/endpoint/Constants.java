package uk.co.inetria.pi.endpoint;

/**
 * Contains the client IDs and scopes for allowed clients consuming your API.
 */
public class Constants {
  public static final String WEB_CLIENT_ID = "793585242699-2h42ldljgpo3so38cdkir0667omm8t1p.apps.googleusercontent.com";
  public static final String ANDROID_CLIENT_ID = "793585242699-2h42ldljgpo3so38cdkir0667omm8t1p.apps.googleusercontent.com";
  public static final String IOS_CLIENT_ID = "793585242699-2h42ldljgpo3so38cdkir0667omm8t1p.apps.googleusercontent.com";
  public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

  public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
  
  // replace with the Google account you use to authenticate your PI
  public static final String EMAIL_ADDRESS = "omer.dawelbeit@gmail.com";
}
