package com.example.testori

import android.app.Application
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.provider.Settings
import android.view.OrientationEventListener
import android.view.WindowManager

class OrientationUtil(
    val context: Application,
    val orientationChangeListener: OrientationChangeListener,
    val allowAutoRotateCheck: Boolean
) {
    private var isAutoRotateEnabled = Settings.System.getInt(
        context.contentResolver,
        Settings.System.ACCELEROMETER_ROTATION,
        0
    ) == 1
    private val orientationListener = object : OrientationEventListener(context) {
        private var previousOrientation = getCurrentOrientation()
        override fun onOrientationChanged(orientation: Int) {
            if ((allowAutoRotateCheck && isAutoRotateEnabled) || !allowAutoRotateCheck) {
                if (orientation == ORIENTATION_UNCHANGED)
                    return
                if ((orientation >= 335 || orientation <= 25) && previousOrientation != ORIENTATION_NORMAL) {
                    orientationChangeListener.onRotateNormal(previousOrientation)
                    previousOrientation = ORIENTATION_NORMAL
                } else if (orientation in 65..115 && previousOrientation != ORIENTATION_ROTATE_RIGHT_90) {
                    orientationChangeListener.onRotateRight(previousOrientation)
                    previousOrientation = ORIENTATION_ROTATE_RIGHT_90
                } else if (orientation in 155..205 && previousOrientation != ORIENTATION_FLIP_VERTICAL_180) {
                    orientationChangeListener.onFlipVertical(previousOrientation)
                    previousOrientation = ORIENTATION_FLIP_VERTICAL_180
                } else if (orientation in 245..295 && previousOrientation != ORIENTATION_ROTATE_LEFT_270) {
                    orientationChangeListener.onRotateLeft(previousOrientation)
                    previousOrientation = ORIENTATION_ROTATE_LEFT_270
                }
            }
        }
    }

    private val rotationObserver: ContentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            isAutoRotateEnabled = Settings.System.getInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1
        }
    }

    fun enable() {
        context.contentResolver.registerContentObserver(
            Settings.System.getUriFor
                (Settings.System.ACCELEROMETER_ROTATION),
            true, rotationObserver
        )
        orientationListener.enable()
    }

    fun disable() {
        context.contentResolver.unregisterContentObserver(rotationObserver)
        orientationListener.disable()
    }

    fun getCurrentOrientation(): Int {
        val service = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return service.defaultDisplay.rotation
    }

    interface OrientationChangeListener {
        fun onRotateNormal(previousOrientation: Int)
        fun onRotateLeft(previousOrientation: Int)
        fun onRotateRight(previousOrientation: Int)
        fun onFlipVertical(previousOrientation: Int)
    }

    companion object {
        const val ORIENTATION_NORMAL = 0
        const val ORIENTATION_ROTATE_LEFT_270 = 270
        const val ORIENTATION_FLIP_VERTICAL_180 = 180
        const val ORIENTATION_ROTATE_RIGHT_90 = 90

        /**
         * When the device orientation cannot be determined
         * (typically when the device is in a close to flat position).
         */
        const val ORIENTATION_UNCHANGED = -1
    }
}