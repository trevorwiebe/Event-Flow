package com.trevorwiebe.caldav.data

import com.trevorwiebe.caldav.data.model.CalDavRequestBody
import com.trevorwiebe.caldav.data.util.getCalendarRequest
import com.trevorwiebe.caldav.data.util.getEventsRequest
import com.trevorwiebe.caldav.data.util.toFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class CalDavApiImpl (
    private val requestBuilder: Request.Builder,
    private val okHttpClient: OkHttpClient
): CalDavApi {

    override suspend fun getCalendar(
        username: String,
        password: String,
        url: String
    ): Flow<String> {
        val credential = Credentials.basic(username, password)
        val requestBody = getCalendarRequest()
        val request = generateRequest(credential, requestBody, url)
        return okHttpClient.newCall(request).toFlow()
    }

    override suspend fun getEvents(
        username: String, password: String, url: String
    ): Flow<String> {
        val credential = Credentials.basic(username, password)
        val requestBody = getEventsRequest()
        val request = generateRequest(credential, requestBody, url)
        return okHttpClient.newCall(request).toFlow()
    }

    private fun generateRequest(
        credential: String,
        requestBody: CalDavRequestBody,
        url: String
    ): Request{
        return requestBuilder.url(url)
            .addHeader("DEPTH", requestBody.depth)
            .addHeader("Content-Type", "application/xml; charset=utf-8")
            .addHeader("Prefer", "return-minimal")
            .method(requestBody.method, requestBody.body.toRequestBody(
                requestBody.body.toMediaTypeOrNull())
            )
            .header("Authorization", credential)
            .build()
    }

}