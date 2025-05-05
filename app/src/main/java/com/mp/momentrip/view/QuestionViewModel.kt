package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.mp.momentrip.data.Answer
import com.mp.momentrip.data.QuestionSetList
import com.mp.momentrip.service.PreferenceAnalyzer
import com.mp.momentrip.service.RecommendService
import com.mp.momentrip.ui.UserDestinations

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestionViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false) // 로딩 상태 추가
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 상태는 모두 MutableStateFlow로 통일
    private val _currentIndex = MutableStateFlow(0)
    private val _questionList = MutableStateFlow(QuestionSetList())
    private val _analyzer = MutableStateFlow(PreferenceAnalyzer())

    // 상태 노출은 StateFlow로 일관되게
    val currentIndex: StateFlow<Int>  = _currentIndex.asStateFlow()
    val questionSetSize: Int = _questionList.value.questionSet.size
    val questionList: StateFlow<QuestionSetList> = _questionList.asStateFlow()
    private fun incrementcurrentIndex() {
        _currentIndex.value += 1
        Log.d("QuestionVM", "Index incremented to ${_currentIndex.value}")
    }

    fun resetIndex() {
        _currentIndex.value = 0
        Log.d("QuestionVM", "Index reset to 0")
    }
    fun getQuestion(index : Int) : String{
        return _questionList.value.questionSet[index].question
    }
    fun getAnswers(index : Int) : List<Answer>{
        return _questionList.value.questionSet[index].answers
    }
    fun getAnalyzer() : PreferenceAnalyzer{
        return _analyzer.value
    }


    suspend fun setNextQuestion(navController: NavController, userState: UserViewModel) {
        if (_currentIndex.value < questionSetSize - 1) {
            incrementcurrentIndex()
        } else {
            _isLoading.value = true // 로딩 시작

            val preference = _analyzer.value.createUserPreference()

            userState.getUser()?.let { currentUser ->

                userState.updateUser(currentUser.copy(userPreference = preference))

                val region = RecommendService.getRegionByPreference(preference)

                _isLoading.value = false // 로딩 끝

                navController.navigate(UserDestinations.ANALYZE_RESULT + "/$region") {
                    popUpTo(0)
                }
            } ?: run {
                Log.d("test","fail")
                _isLoading.value = false // 예외처리: currentUser 없을 때도 로딩 false
            }
        }
    }
}