package com.retrowave.player.domain.usecase

import com.retrowave.player.data.ai.RecommendationEngine
import com.retrowave.player.data.ai.models.AIRecommendation
import com.retrowave.player.domain.model.Song
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val recommendationEngine: RecommendationEngine
) {
    suspend operator fun invoke(songs: List<Song>): List<AIRecommendation> {
        if (songs.isEmpty()) return emptyList()
        return recommendationEngine.getRecommendations(songs)
    }
}
