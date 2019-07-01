package pl.kapucyni.wolczyn.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.annotation.AttrRes
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import retrofit2.Response


fun Context.isChromeCustomTabsSupported(): Boolean {
    val serviceIntent = Intent("android.support.customtabs.action.CustomTabsService")
    serviceIntent.setPackage("com.android.chrome")
    val resolveInfos = packageManager.queryIntentServices(serviceIntent, 0)
    return resolveInfos.isNotEmpty()
}

fun Context.openWebsiteInCustomTabsService(url: String) {
    if (isChromeCustomTabsSupported()) {
        CustomTabsIntent.Builder().apply {
            val color = if (PreferencesManager.getNightMode()) Color.parseColor("#28292e") else Color.WHITE
            setToolbarColor(color)
            setSecondaryToolbarColor(color)
        }.build().launchUrl(this, Uri.parse(url))
    } else {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}

fun Activity.showNoInternetDialogWithTryAgain(function: () -> Unit): Unit =
    AlertDialog.Builder(this)
        .setTitle(R.string.no_internet_title)
        .setMessage(R.string.no_internet_reconnect_message)
        .setCancelable(false)
        .setPositiveButton(R.string.try_again) { dialog, _ ->
            dialog.dismiss()
            if (checkNetworkConnection()) function()
            else showNoInternetDialogWithTryAgain(function)
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
            if (this is MainActivity) goBackToHome()
        }
        .create()
        .show()

fun Activity.showNoInternetDialogDataOutOfDate(): Unit =
    AlertDialog.Builder(this)
        .setTitle(R.string.no_internet_title)
        .setMessage(R.string.no_internet_data_message)
        .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
        .create()
        .show()

@Suppress("DEPRECATION")
fun Activity.checkNetworkConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities != null
    } else {
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

fun Activity.tryToRunFunctionOnInternet(function: () -> Unit) {
    if (checkNetworkConnection()) {
        try {
            function()
        } catch (exc: Exception) {
            showNoInternetDialogWithTryAgain { function() }
        }
    } else {
        showNoInternetDialogWithTryAgain { function() }
    }
}

fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    visibility = View.VISIBLE
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            layoutParams.height = if (interpolatedTime == 1f)
                ViewGroup.LayoutParams.WRAP_CONTENT
            else
                (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean = true
    }
    // Expansion speed of 1dp/ms
    animation.duration = ((targetHeight / context.resources.displayMetrics.density).toInt()).toLong()
    startAnimation(animation)
}

fun View.collapse() {
    val initialHeight = measuredHeight

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean = true
    }
    // Collapse speed of 1dp/ms
    animation.duration = ((initialHeight / context.resources.displayMetrics.density).toInt()).toLong()
    startAnimation(animation)
}

fun Context.getAttributeColor(@AttrRes attributeId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attributeId, typedValue, true)
    return if (typedValue.resourceId == 0) typedValue.data else ContextCompat.getColor(this, typedValue.resourceId)
}

fun Context.getAttributeDrawable(@AttrRes attributeId: Int): Drawable? {
    val typedValue = TypedValue()
    theme.resolveAttribute(attributeId, typedValue, true)
    return ContextCompat.getDrawable(this, typedValue.resourceId)
}

fun <T> Response<T>.saveTokenAndReturnBody(): T? {
    if (headers().names().contains("cm3_token"))
        PreferencesManager.setBearerToken(headers().get("cm3_token") ?: "")
    return body()
}