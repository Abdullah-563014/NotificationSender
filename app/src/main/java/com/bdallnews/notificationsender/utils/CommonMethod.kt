package com.bdallnews.notificationsender.utils

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object CommonMethod {


    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager: ActivityManager=context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services: List<ActivityManager.RunningServiceInfo> =activityManager.getRunningServices(Int.MAX_VALUE)
        for (element in services) {
            if (serviceClass.name.equals(element.service.className)) {
                return true
            }
        }
        return false
    }

    fun sendNotification(title: String?, description: String?, targetUrl: String?): String? {
        val message: String=if (description.isNullOrEmpty()) "Click here for more details." else description
        if (title!=null && targetUrl!=null) {
            try {
                var jsonResponse: String
                val url: URL= URL("https://onesignal.com/api/v1/notifications")
                val con: HttpURLConnection=url.openConnection() as HttpURLConnection
                con.useCaches = false
                con.doOutput = true
                con.doInput = true

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                con.setRequestProperty("Authorization", "Basic MDRiYzEzMWItMTcyYi00M2Y1LWFlZTUtNmI0MWNiMDQ2ZTY2")
                con.setRequestProperty("Accept", "application/json")
                con.requestMethod = "POST"

                val notificationObject: JSONObject= JSONObject("{\n" +
                        "    \"app_id\":\"eef3128d-7113-4ee2-b826-019a2540ace0\",\n" +
                        "    \"included_segments\": [\"Subscribed Users\"],\n" +
                        "    \"headings\": {\"en\":\"$title\"},\n" +
                        "    \"app_url\":\"$targetUrl\",\n" +
                        "    \"contents\":{\"en\":\"$message\"}\n" +
                        "}")

                var sendBytes: ByteArray= notificationObject.toString().toByteArray(Charsets.UTF_8)
                con.setFixedLengthStreamingMode(sendBytes.size)
                var outputStream: OutputStream=con.outputStream
                outputStream.write(sendBytes)
                var httpResponse: Int=con.responseCode
                outputStream.close()
                if (httpResponse>=HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    val scanner: Scanner= Scanner(con.inputStream,"UTF-8")
                    jsonResponse= if (scanner.useDelimiter("\\A").hasNext()) scanner.next() else ""
                    scanner.close()
                } else {
                    val scanner: Scanner= Scanner(con.errorStream,"UTF-8")
                    jsonResponse= if (scanner.useDelimiter("\\A").hasNext()) scanner.next() else ""
                    scanner.close()
                }
                con.disconnect()
                return jsonResponse
            } catch (e: Exception) {
                return null
            }
        }
        return null
    }







}