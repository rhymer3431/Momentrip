package com.mp.momentrip.service

import android.content.Context
import android.util.Log
import com.mp.momentrip.data.place.Place

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sqrt

object Word2VecModel {
    private var interpreter: Interpreter? = null
    private var wordIndex: Map<String, Int>? = null

    suspend fun initialize(context: Context) = withContext(Dispatchers.IO) {
        val modelBuffer = loadModelFile(context, "word2vec.tflite")
        val wordIndexMap = loadWordIndex(context, "word_to_index.json")
        interpreter = Interpreter(modelBuffer)
        wordIndex = wordIndexMap
    }

    private fun loadModelFile(context: Context, fileName: String): MappedByteBuffer {
        context.assets.openFd(fileName).use { fd ->
            FileInputStream(fd.fileDescriptor).use { inputStream ->
                val channel = inputStream.channel
                return channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fd.startOffset,
                    fd.declaredLength
                )
            }
        }
    }

    private fun loadWordIndex(context: Context, fileName: String): Map<String, Int> {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        return jsonObject.keys().asSequence().associateWith { jsonObject.getInt(it) }
    }

    suspend fun getEmbedding(word: String): List<Float>? = withContext(Dispatchers.Default) {
        val interpreter = interpreter ?: return@withContext null
        val wordIndex = wordIndex ?: return@withContext null
        val index = wordIndex[word] ?: return@withContext null

        val input = arrayOf(intArrayOf(index))
        val output = Array(1) { Array(1) { FloatArray(200) } }

        interpreter.run(input, output)
        output[0][0].toList()
    }

    suspend fun getVectorByPlace(place: Place): List<Float>? {
        // 우선 카테고리로 시도
        val categoryVector = getVectorByCategories(place)
        if (categoryVector != null) return categoryVector

        return null
    }
    suspend fun getVectorByCategories(place: Place): List<Float>? {
        val categories = listOfNotNull(place.cat1, place.cat2, place.cat3)

        if (categories.isEmpty()) return null

        return getVectorByList(categories)
    }




    suspend fun getSimilarity(word1: String, word2: String): Float? = withContext(Dispatchers.Default) {
        val vec1 = getEmbedding(word1) ?: return@withContext null
        val vec2 = getEmbedding(word2) ?: return@withContext null

        val dotProduct = vec1.zip(vec2).sumOf { (a, b) -> (a * b).toDouble() }.toFloat()
        val normVec1 = sqrt(vec1.sumOf { (it * it).toDouble() }).toFloat()
        val normVec2 = sqrt(vec2.sumOf { (it * it).toDouble() }).toFloat()

        if (normVec1 == 0f || normVec2 == 0f) return@withContext null

        dotProduct / (normVec1 * normVec2)
    }

    suspend fun getVectorByList(list: List<String>): List<Float> = coroutineScope {
        val result = MutableList(200) { 0f }

        val deferreds = list.toSet().map { word ->
            async {
                getEmbedding(word)?.let { word to it }
            }
        }

        deferreds.awaitAll().filterNotNull().forEach { (_, embedding) ->
            embedding.forEachIndexed { index, value ->
                result[index] += value
            }
        }
        result
    }

    suspend fun like(preferenceVector: MutableList<Float>?, place: Place) {
        val placeVector = getVectorByPlace(place)
        Log.d("test",preferenceVector?.get(0).toString())
        preferenceVector?.let { current ->
            for (index in current.indices) {

                current[index] = ((current[index] + (placeVector?.get(index) ?: current[index])) / 2.0f)
            }
        }
        Log.d("test",preferenceVector?.get(0).toString())
    }

    suspend fun dislike(preferenceVector: MutableList<Float>?, place: Place) {
        val placeVector = getVectorByPlace(place)
        Log.d("test",preferenceVector?.get(0).toString())
        preferenceVector?.let { current ->
            for (index in current.indices) {
                current[index] = (2*(current[index] - (placeVector?.get(index) ?: current[index])))
            }
        }
        Log.d("test",preferenceVector?.get(0).toString())
    }
}
