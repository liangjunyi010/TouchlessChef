package app.touchlessChef.utils;

import android.content.Context;
import android.net.Uri;

public class Files {

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        return GetURIPath.getRealPathFromURI_API19(context, contentURI);
    }
}


