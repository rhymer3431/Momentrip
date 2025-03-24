package com.mp.momentrip.util.vector

import kotlin.math.sqrt

fun textToVector(text: String): Map<String, Int> {
    return text.split(" ").groupingBy { it }.eachCount()
}

fun cosineSimilarity(vec1: Map<String, Int>, vec2: Map<String, Int>): Double {
    val commonWords = vec1.keys.intersect(vec2.keys)
    val dotProduct = commonWords.sumOf { vec1[it]!! * vec2[it]!! }
    val magnitude1 = sqrt(vec1.values.sumOf { it * it }.toDouble())
    val magnitude2 = sqrt(vec2.values.sumOf { it * it }.toDouble())
    return if (magnitude1 > 0 && magnitude2 > 0) dotProduct / (magnitude1 * magnitude2) else 0.0
}

