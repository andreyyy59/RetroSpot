package com.retrowave.player.data.ai

import com.retrowave.player.data.ai.models.AIRecommendation
import com.retrowave.player.data.ai.models.GeminiConfig
import com.retrowave.player.data.ai.models.GeminiContent
import com.retrowave.player.data.ai.models.GeminiPart
import com.retrowave.player.data.ai.models.GeminiRequest
import com.retrowave.player.domain.model.Song
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationEngine @Inject constructor(
    private val geminiApi: GeminiApi
) {
    suspend fun getRecommendations(songs: List<Song>): List<AIRecommendation> {
        val genres = songs.mapNotNull { it.genre }.distinct()
        val artists = songs.map { it.artist }.distinct()

        val songsByGenre = genres.associateWith { g ->
            songs.filter { it.genre == g }.take(10)
        }
        val songsByArtist = artists.take(20).associateWith { a ->
            songs.filter { it.artist == a }.take(5)
        }

        val prompt = """
            Eres un curador musical experto. Analiza esta biblioteca y crea 5 playlists únicas combinando géneros, artistas similares, y moods.

            Géneros disponibles: ${genres.take(15).joinToString(", ")}
            Artistas principales: ${artists.take(25).joinToString(", ")}

            Canciones por género:
            ${songsByGenre.entries.joinToString("\n") { (g, ss) -> "$g: ${ss.joinToString(", ") { "${it.title} - ${it.artist}" }}" }}

            Canciones por artista:
            ${songsByArtist.entries.joinToString("\n") { (a, ss) -> "$a: ${ss.joinToString(", ") { it.title }}" }}

            Reglas:
            - Mezcla artistas similares entre sí (mismo género, misma época, mismo estilo)
            - Sugiere artistas que el usuario no tenga pero que sean del mismo estilo
            - Asigna un mood coherente (Energético, Relajado, Melancólico, Festivo, Intenso, etc.)
            - En "suggestedSongTitles" incluye SOLO títulos exactos de canciones de la biblioteca que encajen en la playlist

            Responde SOLO con JSON válido (sin markdown, sin bloques):
            [
              {
                "playlistName": "nombre",
                "description": "descripción con vibra y estilo",
                "suggestedGenres": ["genero1", "genero2"],
                "suggestedArtists": ["artista1", "artista2"],
                "suggestedSongTitles": ["Título exacto canción 1", "Título exacto canción 2"],
                "mood": "mood"
              }
            ]
        """.trimIndent()

        return try {
            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                generationConfig = GeminiConfig(temperature = 0.7, maxOutputTokens = 2048)
            )
            val response = geminiApi.generate(request)
            val text = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
            if (text != null) {
                parseResponse(text)
            } else {
                getFallbackRecommendations(songs)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting Gemini recommendations")
            getFallbackRecommendations(songs)
        }
    }

    suspend fun getSimilarSongs(song: Song, allSongs: List<Song>): List<Song> {
        val prompt = """
            Tengo esta canción: "${song.title}" de ${song.artist} (género: ${song.genre}).
            
            Basado en mi biblioteca, recomiéndame canciones similares. Dame solo los títulos en un array JSON.
            Canciones disponibles: ${allSongs.take(100).joinToString("; ") { "${it.title} - ${it.artist}" }}
        """.trimIndent()

        return try {
            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                generationConfig = GeminiConfig(temperature = 0.5, maxOutputTokens = 512)
            )
            val response = geminiApi.generate(request)
            allSongs.shuffled().take(10)
        } catch (e: Exception) {
            allSongs
                .filter { it.genre == song.genre && it.id != song.id }
                .take(10)
        }
    }

    private fun parseResponse(json: String): List<AIRecommendation> {
        return try {
            val cleaned = json.trim()
                .removePrefix("```json").removePrefix("```")
                .removeSuffix("```").trim()
            val gson = com.google.gson.Gson()
            val arr = gson.fromJson(cleaned, Array<AIRecommendation>::class.java)
            arr?.toList() ?: emptyList()
        } catch (e: Exception) {
            Timber.e(e, "Failed to parse AI response: $json")
            emptyList()
        }
    }

    private fun getFallbackRecommendations(songs: List<Song>): List<AIRecommendation> {
        val genres = songs.mapNotNull { it.genre }.distinct()
        val artists = songs.map { it.artist }.distinct()
        val allMoods = listOf("Energético", "Relajado", "Melancólico", "Festivo", "Intenso", "Versátil", "Soñador", "Oscuro", "Eufórico", "Íntimo")
        val rand = kotlin.random.Random(System.currentTimeMillis())

        val nameTemplates = listOf(
            { g: String -> "Esencia $g" },
            { g: String -> "$g total" },
            { g: String -> "Ritmo $g" },
            { g: String -> "Alma $g" },
            { g: String -> "$g en llamas" },
            { g: String -> "Viaje $g" },
            { g: String -> "Noche $g" },
            { g: String -> "Frecuencia $g" }
        )
        val descTemplates = listOf(
            "Género que define el momento",
            "Selección curada para ti",
            "Lo mejor del estilo en tu biblioteca",
            "Atmósfera y groove en cada track",
            "Perfecto para cualquier hora"
        )
        val artistAdj = listOf("Grandes", "Imprescindibles", "Esenciales", "Favoritos", "Inolvidables", "Clásicos")

        if (genres.isNotEmpty()) {
            val shuffledGenres = genres.shuffled(rand).take(5)
            val results = mutableListOf<AIRecommendation>()

            // Varied mix: some genre-based, some mood-based, some artist-mashup
            val mixedIndices = (0 until 5).toList().shuffled(rand)
            for (i in 0 until 5) {
                if (i < shuffledGenres.size && mixedIndices[i] % 3 != 0) {
                    val genre = shuffledGenres[i]
                    val tmpl = nameTemplates[rand.nextInt(nameTemplates.size)]
                    results.add(
                        AIRecommendation(
                            playlistName = tmpl(genre),
                            description = descTemplates[rand.nextInt(descTemplates.size)],
                            suggestedGenres = listOf(genre),
                            suggestedArtists = songs.filter { it.genre == genre }
                                .map { it.artist }.distinct().shuffled(rand).take(5),
                            mood = allMoods[rand.nextInt(allMoods.size)]
                        )
                    )
                } else if (artists.isNotEmpty()) {
                    val artist = artists.shuffled(rand).first()
                    val adj = artistAdj[rand.nextInt(artistAdj.size)]
                    results.add(
                        AIRecommendation(
                            playlistName = "$adj: $artist",
                            description = "Homenaje a ${artist} con sus mejores tracks",
                            suggestedGenres = emptyList(),
                            suggestedArtists = listOf(artist),
                            mood = allMoods[rand.nextInt(allMoods.size)]
                        )
                    )
                }
            }
            if (results.isNotEmpty()) return results
        }

        // Fallback by artist when no genre metadata
        val artistGroups = songs.groupBy { it.artist }.entries.toList().shuffled(rand).take(5)
        return artistGroups.map { (artist, artistSongs) ->
            val adj = artistAdj[rand.nextInt(artistAdj.size)]
            AIRecommendation(
                playlistName = "$adj: $artist",
                description = "${artistSongs.size} tracks que definen a $artist",
                suggestedGenres = artistSongs.mapNotNull { it.genre }.distinct(),
                suggestedArtists = listOf(artist),
                mood = allMoods[rand.nextInt(allMoods.size)]
            )
        }
    }
}
