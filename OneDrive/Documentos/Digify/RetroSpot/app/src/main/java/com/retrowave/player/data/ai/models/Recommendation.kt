package com.retrowave.player.data.ai.models

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiConfig = GeminiConfig()
)

data class GeminiContent(
    val parts: List<GeminiPart>?
)

data class GeminiPart(
    val text: String
)

data class GeminiConfig(
    val temperature: Double = 0.7,
    val maxOutputTokens: Int = 1024
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

data class GeminiCandidate(
    val content: GeminiContent?
)

data class AIRecommendation(
    val playlistName: String,
    val description: String,
    val suggestedGenres: List<String>,
    val suggestedArtists: List<String>,
    val suggestedSongTitles: List<String> = emptyList(),
    val mood: String
)
