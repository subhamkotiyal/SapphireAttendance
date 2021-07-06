package com.example.baseproject.utils

import android.app.Activity
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.webgurus.MyService
import com.webgurus.attendanceportal.LocationPermissionCheck
import com.webgurus.attendanceportal.R
import okhttp3.MediaType
import okhttp3.RequestBody
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by android on 2/11/17.
 * *
 */
object Utils {



    private var mLastClickTime = 0L
    private val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
     val LOCATION_SERVICE = "location"
    private var mIsPAsswordShown: Boolean = false
    private val PROXIMITY_RADIUS = 10000
    private val pattern: Pattern? = null
    private var matcher: Matcher? = null


    const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun requestingLocationUpdates(context: Context?): Boolean {

        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
            .apply()
    }

    /**
     * Returns the `location` object as a human readable string.
     * @param location  The [Location].
     */
    fun getLocationText(location: Location?): String? {
        return if (location == null) "Unknown location" else "(" + location.latitude + ", " + location.longitude + ")"
    }

    fun getLocationTitle(context: Context): String? {
        return context.getString(
            R.string.location_updated,
            DateFormat.getDateTimeInstance().format(Date())
        )
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun getPathFromURI(context: Context, uri: Uri?): String
    {
        try
        {
            if (uri == null)
            {
                throw NullPointerException("Uri must not be null")
            }
            val path: String
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null)
            {
                val column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                path = cursor.getString(column_index)
                cursor.close()
                return path
            }
            return uri.path!!
        }
        catch (e: Exception)
        {
            e.printStackTrace();
            return "";
        }

    }

    fun hideKeyboard(activity: Activity?) {
        val view = activity?.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



    /**
     * Show toast.
     *
     * @param toast String value which needs to shown in the toast.
     * @description if you want to print a toast just call this method and pass
     * what you want to be shown.
     */

    /**
     ************* logout user from app and clear all local data **************
     */



    /**
     * Is email valid.
     *
     * @param email Pass a email in string format and this method check if it is
     * a valid address or not.
     * @return True if email is valid otherwise false.
     */
    fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length==10
        } else false
    }
    fun isConnected(mContext: Activity): Boolean {
        var connected = false

        try {
            val cm =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo: NetworkInfo? = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected()
            return connected
        } catch (e: java.lang.Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }


    @Synchronized
    fun getOriginalImagePath(fragmentActivity: FragmentActivity?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = fragmentActivity?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )
        val column_index_data = cursor!!
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToLast()

        return cursor.getString(column_index_data)
    }


    // Function to validate the password.

//    ^ represents starting character of the string.
//    (?=.*[0-9]) represents a digit must occur at least once.
//    (?=.*[a-z]) represents a lower case alphabet must occur at least once.
//    (?=.*[A-Z]) represents an upper case alphabet that must occur at least once.
//    (?=.*[@#$%^&-+=()] represents a special character that must occur at least once.
//    (?=\\S+$) white spaces don’t allowed in the entire string.
//    .{8, 20} represents at least 8 characters and at most 20 characters.
//    $ represents the end of the string.
    fun isValidPassword(password: String?): Boolean {
        val regex = ("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$")
        val p = Pattern.compile(regex)
        if (password == null) {
            return false
        }
        val m = p.matcher(password)
        return m.matches()
    }

      fun  getCurrentDateandTime() : String {
          val c = Calendar.getInstance()
          System.out.println("Current time => " + c.time)
          val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          val formattedDate = df.format(c.time)
          return  formattedDate
    }

    fun checkLocation(context: Activity) {
        val lm: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Location")
            builder.setMessage("To Continue , Turn On Device Location, Which Uses Google's location service")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            // Create the AlertDialog
            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

    }

  fun checkLocationEnable(context: Activity) : Boolean{
      val lm: LocationManager =
          context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      var gps_enabled = false
      var network_enabled = false
      try {
          gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
      } catch (ex: java.lang.Exception) {
      }
      try {
          network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
      } catch (ex: java.lang.Exception) {
      }
      return gps_enabled

  }



    fun stringContainsNumber(s: String?): Boolean {
        val p = Pattern.compile("[0-9]")
        val m = p.matcher(s)
        return m.find()
    }


    fun removeSpecialChars(temp: String): String {
        var finalStr: String = temp;
        // '.', '#', '$', '[', or ']'
        //var finalStr = "Rilyo_102_check1.@.#[]"
        // '.', '#', '$', '[', or ']'
        if (finalStr.contains(".")) {
            finalStr = finalStr.replace(".", "")
        }
        if (finalStr.contains("#")) {
            finalStr = finalStr.replace("#", "")
        }
        if (finalStr.contains("[")) {
            finalStr = finalStr.replace("[", "")
        }
        if (finalStr.contains("]")) {
            finalStr = finalStr.replace("]", "")
        }

        return finalStr;
    }


    fun isPasswordValid(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\$@\$!%*?&])[A-Za-z\\d\$@\$!%*?&]{8,}"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }

    /*add events on google calender*/

    /**
     *************** formatting date and time *******************
     */

    fun datetimeFormat(format: String, dateString: String?): String? {

        var formatedResult = dateString

        /* first change the datetime string to date*/
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())

        try {
            /* in this method we are separate the time and date by format of date and time*/
            val date = dateFormat.parse(dateString)

            val formateDate = SimpleDateFormat(format, Locale.getDefault()) //

            formatedResult = formateDate.format(date.time)


        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedResult


    }


    /**
     * This method will handle single click
     *
     * @return
     */
    fun singleClick(): Boolean {
        val minInterval = 400L
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime

        return elapsedTime >= minInterval
    }



    fun checkNetworkAvailable(content: View?): Boolean {
        val cm = content?.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting) {
            return true
        }
        return false
    }


    fun clearNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    /**
     * This method will hide or show the password
     *
     */


    /**
     * Apply Scale animation on textView
     *
     * @param view
     */



    fun printFacebookHashKey(context: Context?) {
        try {
            context?.let {
                val info = it.packageManager.getPackageInfo(
                    "project.rilyo.elad",
                    PackageManager.GET_SIGNATURES
                )
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }

        } catch (e: PackageManager.NameNotFoundException) {


        } catch (e: NoSuchAlgorithmException) {

        }
    }


    /*
   *  Getting the real path of image through camera
   * */


    fun createRequestBody(data: String?): RequestBody {

        return RequestBody.create(MediaType.parse("multipart/form-data"), data)

    }




   fun isValidEmailId(email: String):Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }




    fun chnageDateFormat(datess: String) : String{
        val originalFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val date: Date = originalFormat.parse(datess)
        val formattedDate: String = targetFormat.format(date) // 20120821
        return formattedDate

    }



    fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    fun View.snackbar(message: String) {
        Snackbar.make(
            this,
            message,
            Snackbar.LENGTH_LONG
        ).also { snackbar ->
            snackbar.setAction(" ") {
                snackbar.dismiss()
            }
        }.show()
    }

    // schedule the start of the service every 10 - 30 seconds
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
       fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, MyService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
//        val jobScheduler = Context.getSystemService(JobScheduler::class.java)
         val jobScheduler=context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
    }

}


