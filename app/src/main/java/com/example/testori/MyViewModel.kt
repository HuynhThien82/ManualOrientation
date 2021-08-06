package com.example.testori

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MyViewModel(context: Application): AndroidViewModel(context), OrientationUtil.OrientationChangeListener {
    val orientationLiveData = MutableLiveData<Int>()
    val util = OrientationUtil(context,this,false)

    override fun onRotateNormal(previousOrientation: Int) {
        orientationLiveData.postValue(OrientationUtil.ORIENTATION_NORMAL)
    }

    override fun onRotateLeft(previousOrientation: Int) {
        orientationLiveData.postValue(OrientationUtil.ORIENTATION_ROTATE_LEFT_270)
    }

    override fun onRotateRight(previousOrientation: Int) {
        orientationLiveData.postValue(OrientationUtil.ORIENTATION_ROTATE_RIGHT_90)
    }

    override fun onFlipVertical(previousOrientation: Int) {
        orientationLiveData.postValue(OrientationUtil.ORIENTATION_FLIP_VERTICAL_180)
    }

    companion object{
        const val TAG = "Thien debug"
    }
}