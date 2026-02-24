package com.example.githubuserbrowser.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.githubuserbrowser.R

val Capriola = FontFamily(
    Font(resId = R.font.capriola, weight = FontWeight.Normal),
    Font(resId = R.font.capriola, weight = FontWeight.ExtraBold)
)
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W700, fontSize = 40.sp, lineHeight = 48.sp,),
    displayMedium = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W600, fontSize = 36.sp, lineHeight = 44.sp),
    displaySmall = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W500, fontSize = 32.sp, lineHeight = 40.sp),

    headlineLarge = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W700, fontSize = 28.sp, lineHeight = 36.sp),
    headlineMedium = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W600, fontSize = 24.sp, lineHeight = 32.sp),
    headlineSmall = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W500, fontSize = 22.sp, lineHeight = 28.sp),

    titleLarge = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W600, fontSize = 20.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W500, fontSize = 18.sp, lineHeight = 24.sp),
    titleSmall = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W400, fontSize = 16.sp, lineHeight = 22.sp),

    bodyLarge = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W400, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W400, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W400, fontSize = 12.sp, lineHeight = 16.sp),

    labelLarge = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W500, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp),
    labelMedium = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W500, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.25.sp),
    labelSmall = TextStyle(fontFamily = Capriola, fontWeight = FontWeight.W400, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 0.5.sp),
)
