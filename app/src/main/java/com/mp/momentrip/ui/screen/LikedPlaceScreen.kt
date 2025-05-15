package com.mp.momentrip.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.view.UserViewModel

@Composable
fun LikedPlaceScreen(
    userState: UserViewModel
){
    Column {
        userState.getUser()?.liked?.forEach {
            place->
            PlaceCard(
                place!!,
                onClick = {},
            )
        }
    }
}