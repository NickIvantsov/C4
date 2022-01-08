package com.example.feature_game.tmp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.core.interactor.SpaceDustUseCase

class SpaceDustManager(
    private val spaceDustUseCase: SpaceDustUseCase,
    private val spaceViewModel: SpaceViewModel
) {
    private val paint = Paint()

    init {
        paint.color = Color.WHITE
    }

    fun drawSpaceDust(
        canvas: Canvas
    ) {
        for (spaceDustElementIndex in 0 until spaceDustUseCase.getSize()) {

            val currentSpaceDustElement = spaceDustUseCase.getByIndex(spaceDustElementIndex)

            if (currentSpaceDustElement.downLight) currentSpaceDustElement.counter++ else currentSpaceDustElement.counter--

            if (currentSpaceDustElement.counter < 10) {
                currentSpaceDustElement.downLight = true
            } else if (currentSpaceDustElement.counter >= 125) {
                currentSpaceDustElement.downLight = false
            }

            val opacity = spaceViewModel.getSpaceDustOpacity(currentSpaceDustElement.counter)
            //region мигание синим и ораньжевым цветом
            paint.color = spaceViewModel.getCurrentSpaceDustColor(
                opacity,
                currentSpaceDustElement.counter
            )
            //endregion
            canvas.drawPoint(
                currentSpaceDustElement.x.toFloat(),
                currentSpaceDustElement.y.toFloat(),
                paint
            )
        }
    }
}