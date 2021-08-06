package com.example.testori

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.testori.databinding.FragmentCameraBinding
import com.example.testori.databinding.FragmentCameraLandBinding
import java.util.*
import kotlin.concurrent.schedule

class CameraFragment : Fragment() {
    private val viewModel: MyViewModel by viewModels()
    private lateinit var portraitBinding: FragmentCameraBinding
    private lateinit var landscapeBinding: FragmentCameraLandBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.util.enable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        portraitBinding = FragmentCameraBinding.inflate(inflater)
        landscapeBinding = FragmentCameraLandBinding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_camera,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(MainActivity.TAG, "onViewCreated vao")
        portraitBinding.txtMain.setOnClickListener {
            Log.d(MainActivity.TAG, "port clicked")
        }
        landscapeBinding.txtMain.setOnClickListener {
            Log.d(MainActivity.TAG, "land clicked")
        }

//        landscapeBinding.txtMain.setOnClickListener {
//            Log.d(MainActivity.TAG, "land clicked")
//        }
        viewModel.orientationLiveData.observe(viewLifecycleOwner, {
            Log.d(MainActivity.TAG, "orientation: $it")
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            Timer("ChangeLayout", false).schedule(500) {
                requireActivity().runOnUiThread {
                    if (it == OrientationUtil.ORIENTATION_ROTATE_RIGHT_90 || it == OrientationUtil.ORIENTATION_ROTATE_LEFT_270) {
                        Log.d(MainActivity.TAG, "vao change landscape")
//                        requireActivity()
                        requireActivity().setContentView(landscapeBinding.root)
                        landscapeBinding.txtResult.text = it.toString()
                    } else if (it == OrientationUtil.ORIENTATION_NORMAL) {
                        Log.d(MainActivity.TAG, "vao change portrait")
                        requireActivity().setContentView(portraitBinding.root)
                        portraitBinding.txtResult.text = it.toString()
                    }
                }
            }
        })
    }
}