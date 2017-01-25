package xyz.electron.eventcalendar.provider;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    /**
     * The Content Authority is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.
     */
    public static final String CONTENT_AUTHORITY = "xyz.electron.eventcalendar.provider";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String PATH_SCH = "schedule";
    public static final String PATH_SPO = "sponsors";

    /**
     * Create one class for each table that handles all information regarding the table schema and
     * the URIs related to it.
     */

    public static final class SchEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCH).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_SCH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_SCH;

        // Define the table schema
        public static final String TABLE_NAME = "schTable";
        public static final String COLUMN_NAME = "schEventObj";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildSchUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SpoEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPO).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_SPO;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_SPO;

        public static final String TABLE_NAME = "spoTable";
        public static final String COLUMN_NAME = "spoDataObj";

        public static Uri buildSpoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
