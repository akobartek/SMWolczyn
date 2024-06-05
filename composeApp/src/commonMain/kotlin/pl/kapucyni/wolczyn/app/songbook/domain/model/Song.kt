package pl.kapucyni.wolczyn.app.songbook.domain.model

import pl.kapucyni.wolczyn.app.common.utils.normalizeMultiplatform
import kotlin.math.max
import kotlin.math.min

data class Song(
    val title: String = "",
    val text: String = "",
) {
    companion object {
        private const val SIMILARITY_THRESHOLD = 0.33
    }

    fun isMatchingQuery(query: String): Boolean {
        val updatedQuery = query.normalizeMultiplatform()
        val updatedTitle = title.normalizeMultiplatform()
        val updatedText = text.normalizeMultiplatform()
        return updatedTitle.contains(updatedQuery) ||
                updatedText.contains(updatedQuery) ||
                updatedTitle.getSimilarity(updatedQuery) > SIMILARITY_THRESHOLD
    }

    private fun String.getSimilarity(query: CharSequence): Double {
        val maxLength = max(this.length, query.length)
        val levenshteinDistance = levenshteinDistance(this, query)
        return (maxLength * 1.0 - levenshteinDistance) / maxLength * 1.0
    }

    private fun levenshteinDistance(x: CharSequence, y: CharSequence): Int {
        if (x == y) return 0
        if (x.isEmpty()) return y.length
        if (y.isEmpty()) return x.length

        val xLength = x.length + 1
        val yLength = y.length + 1

        var cost = Array(xLength) { it }
        var newCost = Array(xLength) { 0 }

        for (i in 1..<yLength) {
            newCost[0] = i

            for (j in 1..<xLength) {
                val match = if (x[j - 1] == y[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = min(min(costInsert, costDelete), costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[xLength - 1]
    }
}
