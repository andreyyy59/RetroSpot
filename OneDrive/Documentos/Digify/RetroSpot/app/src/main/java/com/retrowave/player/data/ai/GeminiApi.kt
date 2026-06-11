package com.retrowave.player.data.ai

import com.retrowave.player.data.ai.models.GeminiRequest
import com.retrowave.player.data.ai.models.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApi {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generate(@Body request: GeminiRequest): GeminiResponse
}
