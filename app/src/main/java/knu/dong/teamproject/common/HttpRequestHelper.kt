package knu.dong.teamproject.common

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import knu.dong.teamproject.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HttpRequestHelper(context: Context) {
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val TAG = "HttpRequestHelper"
    private val domain: String = BuildConfig.SERVER_URL


    suspend fun get(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse? =
        withContext(Dispatchers.IO) {
            try {
                client.get("$domain/$path", block)
            } catch (err: Exception) {
                Log.e("${TAG}_get", err.toString())

                null
            }
        }

    suspend fun <T> get(path: String, clazz: Class<T>, block: HttpRequestBuilder.() -> Unit = {}): T? =
        withContext(Dispatchers.IO) {
            try {
                val url = "$domain/$path"

                val response: HttpResponse = client.get(url, block)
                val responseStatus = response.status

                if (responseStatus == HttpStatusCode.OK) {
                    val jsonObject = JsonParser.parseString(response.bodyAsText()).asJsonObject
                    val httpApiResponse = Gson().fromJson(jsonObject, clazz)

                    httpApiResponse
                } else {
                    Log.e("${TAG}_get", "$responseStatus")

                    null
                }
            }
            catch (err: Exception) {
                Log.e("${TAG}_get", err.toString())

                null
            }
        }

    suspend fun post(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse? =
        withContext(Dispatchers.IO) {
            try {
                client.post("$domain/$path", block)
            } catch (err: Exception) {
                Log.e("${TAG}_get", err.toString())

                null
            }
        }

    suspend fun <T> post(path: String, clazz: Class<T>, block: HttpRequestBuilder.() -> Unit = {}): T? =
        withContext(Dispatchers.IO) {
            try {
                val url = "$domain/$path"

                val response: HttpResponse = client.post(url, block)
                val responseStatus = response.status

                if (responseStatus.value / 100 == 2) {
                    val jsonObject = JsonParser.parseString(response.bodyAsText()).asJsonObject
                    val httpApiResponse = Gson().fromJson(jsonObject, clazz)

                    httpApiResponse
                } else {
                    Log.e("${TAG}_get", "$responseStatus")

                    null
                }
            }
            catch (err: Exception) {
                Log.e("${TAG}_get", err.toString())

                null
            }
        }
}
