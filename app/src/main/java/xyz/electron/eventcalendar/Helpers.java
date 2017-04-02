package xyz.electron.eventcalendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Helpers {

    //helper function to open urls
    public static void goToUrl(String url, Context context) {
        Uri openUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, openUrl);
        context.startActivity(launchBrowser);
    }

    //helper function to send email
    public static void sendEmail(String emailId, String subject , String body, Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", emailId, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    //helper function to open dialer a number
    public static void dialThis(String number, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    //helper function to open map with geo: URI
    public static void mapThis(String lat, String lng, String zoom, Context context) {
        // map point based on latitude/longitude, z param is zoom level.
        String loc = "geo:" + lat +","+ lng +"?z=" + zoom;
        Uri location = Uri.parse(loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, location);
        // TODO: 02-04-17 handle case when there is no App to handle geo: queries
        context.startActivity(intent);
    }
}
