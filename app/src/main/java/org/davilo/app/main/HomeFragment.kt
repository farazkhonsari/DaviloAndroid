package org.davilo.app.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.davilo.app.R
import org.davilo.app.databinding.CurrentEnroledLevelBinding

import org.davilo.app.databinding.FragmentHomeBinding
import org.davilo.app.databinding.FragmentHomeBindingImpl


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBindingImpl.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        val photosAdapter = PhotosAdapter()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.homePhotosList.apply {
            adapter = photosAdapter
            layoutManager = linearLayoutManager

        }
    }


    class PhotosAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return PhotosViewHolder(
                CurrentEnroledLevelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount() = 1

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is PhotosViewHolder) {
                holder.bind("salam")
            }
        }

        class PhotosViewHolder(rowBinding: CurrentEnroledLevelBinding) :
            RecyclerView.ViewHolder(rowBinding.root) {
            private val binding = rowBinding
            fun bind(str: String) {
                binding.camera = str
                val bundle = bundleOf("PHOTO_NAME" to str)
                binding.root.setOnClickListener { view ->
                    Navigation.findNavController(view).navigate(R.id.action_home_to_details, bundle)
                }
            }
        }
    }
}
