package org.davilo.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun urlBrowserIntent(context: Context?, url: String) {
    var url = url
    try {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url"
        context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}