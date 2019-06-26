package pl.kapucyni.wolczyn.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import android.view.animation.Animation
import android.view.animation.Transformation
import pl.kapucyni.wolczyn.app.R
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat


fun Context.isChromeCustomTabsSupported(): Boolean {
    val serviceIntent = Intent("android.support.customtabs.action.CustomTabsService")
    serviceIntent.setPackage("com.android.chrome")
    val resolveInfos = packageManager.queryIntentServices(serviceIntent, 0)
    return !(resolveInfos == null || resolveInfos.isEmpty())
}

fun Activity.showNoInternetDialog(function: () -> Unit): Unit =
    AlertDialog.Builder(this)
        .setTitle(R.string.no_internet_title)
        .setMessage(R.string.no_internet_reconnect_message)
        .setCancelable(false)
        .setPositiveButton(R.string.try_again) { dialog, _ ->
            dialog.dismiss()
            if (checkNetworkConnection()) function()
            else showNoInternetDialog(function)
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
            if (this is MainActivity) goBackToSchedule()
        }
        .create()
        .show()

fun Activity.checkNetworkConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
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