package rewheeldev.tappycosmicevasion.logging

import timber.log.Timber

fun logD(msg: String) {
    Timber.d(msg)
}

fun logE(throwable: Throwable, msg: String = "") {
    when {
        msg.isEmpty() -> {
            Timber.e(throwable)
        }
        else -> {
            Timber.e(throwable, msg)
        }
    }

}