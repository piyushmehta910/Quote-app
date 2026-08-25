package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GradientColorStop(
    val color: Long,
    val position: Float
) : Parcelable
