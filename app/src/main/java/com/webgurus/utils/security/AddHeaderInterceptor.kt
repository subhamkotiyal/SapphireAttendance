package com.orien.bronsonjones.utils.security

import com.example.baseproject.utils.getValue
import com.webgurus.Constants
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*


/**
 * Created by shivam on 9/4/18.
 */


class AddHeaderInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()
        val token = PreferencesClass.prefs?.getValue(Constants.ACCESS_TOKEN, "")
      //  builder.addHeader("Authorization", token)
      //  builder.addHeader("Accept","application/json")
        getTimeZone()
        return chain.proceed(builder.build())

    }

    /**
     ******** get current timezone string *****************
     */
    private fun getTimeZone(): String {
        val tz = TimeZone.getDefault()
        val offset = tz.rawOffset
        return String.format("%s%02d:%02d", if (offset >= 0) "+" else "-", offset / 3600000, offset / 60000 % 60)
    }

}