package org.davilo.app.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.davilo.app.databinding.FragmentGeneralInfoBinding
import org.davilo.app.databinding.FragmentGeneralInfoBindingImpl

class GeneralInfoFragment : Fragment() {

    private lateinit var binding: FragmentGeneralInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralInfoBindingImpl.inflate(inflater)
        return binding.root
    }
}
