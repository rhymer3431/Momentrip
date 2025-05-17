package com.mp.momentrip

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.common.KakaoSdk


import com.kakao.vectormap.KakaoMapSdk
import com.mp.momentrip.service.Word2VecModel
import com.mp.momentrip.service.TourService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MomenTripApp : Application() {

    override fun onCreate() {
        super. onCreate()

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        // FirebaseAuth.getInstance().signOut()
        KakaoSdk.init(this,"f91be847a00a385e5585d6f39c0a5e91")
        KakaoMapSdk.init(this, "f91be847a00a385e5585d6f39c0a5e91")
        TourService.init(BuildConfig.TOUR_API_KEY)
        applicationScope.launch {
            Word2VecModel.initialize(this@MomenTripApp)
        }
    }

}
