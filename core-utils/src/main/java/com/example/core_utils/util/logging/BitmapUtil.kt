package com.example.core_utils.util.logging

import android.graphics.Bitmap

//уменьшаем изображение по размеру
/**
 * метод позволяет маштабировать изображение
 * @param bitmap изображение которое нужно преобразовать
 * @param multiplier размер множителя для определения насколько нужно маштабировать
 * @param screenSizeX размер экрана по оси X или ширина экрана
 * @return маштабированое изображение пропорционально уменьшеное согласно multiplier и ширине экрана
 */
fun scaleBitmap(bitmap: Bitmap, multiplier: Int, screenSizeX: Int): Bitmap {

    val newHeight = getNewBitmapHeight(screenSizeX, multiplier)

    val newWidth = getNewWidth(bitmap.width, bitmap.height, newHeight)

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight.toInt(), false)
}

/**
 * @param screenSizeX размер экрана
 * @param multiplier размер на который нужно увеличить (1,2,3 ...)
 * @param constantSizeForMultiplier значение которое будет умножено за параметр multiplier для получения
 * преобразования
 * @return высота изображения которая была получена после преобразования
 */
private fun getNewBitmapHeight(
    screenSizeX: Int,
    multiplier: Int,
    constantSizeForMultiplier: Int = CONSTANT_SIZE_FOR_MULTIPLIER
): Float {
    return (screenSizeX / constantSizeForMultiplier * multiplier).toFloat()
}

/**
 * метод пропорционального уменьшения размера опираясь на высоту
 *
 * @param baseWidth базавый размер ширины изображения
 * @param baseHeight базовый размер высоты изображения
 * @param newHeight желаемый размер по высоте изображения
 * @return обьект Pair который содрежит первое значение обозначающий новый размер по ширене (width),
 * а второй содержит новый размер по высоте (height) который был передан в сигнатуре метода
 */
private fun getNewSizeByHeight(
    baseWidth: Int,
    baseHeight: Int,
    newHeight: Float
): Pair<Int, Int> {
    val newWidth: Int = (baseWidth * newHeight / baseHeight).toInt()
    return Pair(newWidth, newHeight.toInt())
}

/**
 * метод пропорционального уменьшения размера опираясь на ширину
 *
 * @param baseWidth базавый размер ширины изображения
 * @param baseHeight базовый размер высоты изображения
 * @param newWidth желаемый размер по ширене изображения
 * @return обьект Pair который содрежит первое значение обозначающий новый размер по ширене (width),
 * а второй содержит новый размер по высоте (height) который был передан в сигнатуре метода
 */
private fun getNewSizeByWidth(
    baseWidth: Int,
    baseHeight: Int,
    newWidth: Float
): Pair<Int, Int> {
    val newHeight: Int = (baseHeight * newWidth / baseWidth).toInt()
    return Pair(newWidth.toInt(), newHeight)
}

/**
 * метод пропорционального уменьшения размера опираясь на ширину
 *
 * @param baseWidth базавый размер ширины изображения
 * @param baseHeight базовый размер высоты изображения
 * @param newHeight желаемый размер по высоте изображения
 * @return пропорционально уменьшеный размер ширины по высоте
 */
private fun getNewWidth(
    baseWidth: Int,
    baseHeight: Int,
    newHeight: Float
): Int {
    return (baseWidth * newHeight / baseHeight).toInt()
}

/**
 * цифра которая будет использоватся для вычисления велечины изображения которое будет умножено на
 * доп значение взависимости от обстоятельств
 */
private const val CONSTANT_SIZE_FOR_MULTIPLIER = 70