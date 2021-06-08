package ua.yandex.jere184.c4tappydefender.util

import android.text.Editable


fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)