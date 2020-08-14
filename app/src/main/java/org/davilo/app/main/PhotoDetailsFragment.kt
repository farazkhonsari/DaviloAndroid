package org.davilo.app.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.davilo.app.databinding.FragmentPhotoDetailsBinding
import org.davilo.app.databinding.FragmentPhotoDetailsBindingImpl

class PhotoDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPhotoDetailsBinding
    private lateinit var cameraStr: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    private fun loadArguments() {
        arguments?.getString("PHOTO_NAME")?.let {
            cameraStr = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoDetailsBindingImpl.inflate(inflater)
        binding.camera = cameraStr
        return binding.root
    }
}
