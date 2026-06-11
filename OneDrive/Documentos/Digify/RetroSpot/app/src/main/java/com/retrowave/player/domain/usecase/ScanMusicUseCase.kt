package com.retrowave.player.domain.usecase

import com.retrowave.player.data.repository.MusicRepository
import javax.inject.Inject

class ScanMusicUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() {
        musicRepository.scanAndSaveMusic()
    }
}
