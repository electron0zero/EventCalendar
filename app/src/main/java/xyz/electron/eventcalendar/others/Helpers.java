package xyz.electron.eventcalendar.others;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Helpers {

    //helper function to open urls
    public static void goToUrl(String url, Context context) {
        Uri openUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, openUrl);
        context.startActivity(launchBrowser);
    }

    //helper function to send email
    public static void sendEmail(String emailId, String subject , String body, Context context){
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", emailId, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, "Send email..."));

    }

    //helper function to open dialer a number
    public static void dialThis(String number, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
    }

    //helper function to open map with geo: URI
    public static void mapThis(String lat, String lng, String zoom, Context context) {
        // map point based on latitude/longitude, z param is zoom level.
        String loc = "geo:" + lat +","+ lng +"?z=" + zoom;
        Uri location = Uri.parse(loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, location);
        // TODO: 02-04-17 handle case when there is no App to handle geo: queries
        try{
            context.startActivity(intent);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(context, "No Activity Found to Open Maps," +
                    " Install a App that can Open Maps", Toast.LENGTH_LONG).show();
        }
    }
}
