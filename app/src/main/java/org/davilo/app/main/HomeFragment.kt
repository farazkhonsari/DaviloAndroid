package org.davilo.app.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.FragmentHomeBinding
import org.davilo.app.databinding.FragmentHomeBindingImpl
import org.davilo.app.model.Enroll
import org.davilo.app.model.HomeViewModel
import org.davilo.app.model.ModuleInfo
import org.davilo.app.ui.Delegate
import org.davilo.app.ui.EnrolledLevelCell


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeAdapter: Adapter
    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    var enroll: Enroll? = null
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
        homeAdapter = Adapter()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.homePhotosList.apply {
            this.adapter = homeAdapter
            layoutManager = linearLayoutManager

        }
        binding.showLevels.setOnClickListener {
            (activity as MainActivity).showLevelsTab()

        }
        observeData()
        viewModel.loadCurrentEnroll()
    }

    private fun observeData() {
        viewModel.currentEnroll.observe(viewLifecycleOwner, Observer {
            enroll = it
            homeAdapter.setEnroll(enroll)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.loading.show()
            } else {
                binding.loading.hide()
            }
        })
        viewModel.isNotEnrolled.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.notEnrolledContainer.visibility = View.VISIBLE
                binding.homePhotosList.visibility = View.GONE
            } else {
                binding.notEnrolledContainer.visibility = View.GONE
                binding.homePhotosList.visibility = View.VISIBLE
            }
        })


    }


    inner class Adapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var enroll: Enroll? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: EnrolledLevelCell? =
                this@HomeFragment.context?.let { EnrolledLevelCell(context = it) }
            view?.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view?.delegate = object : Delegate {
                override fun onModuleDidPressed(moduleInfo: ModuleInfo?) {
                    println(moduleInfo?.module)
                    val bundle = bundleOf(
                        "object_id" to moduleInfo?.id,
                        "object_name" to moduleInfo?.module
                    )
                    Navigation.findNavController(view!!)
                        .navigate(R.id.go_to_category, bundle)
                }
            }

            return Holder(view!!)

        }

        override fun getItemCount(): Int {

            return if (enroll != null) {
                1
            } else {
                0
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView is EnrolledLevelCell) {
                (holder.itemView as EnrolledLevelCell).setEnrollObject(enroll)
            }
        }

        fun setEnroll(enroll: Enroll?) {
            this.enroll = enroll
            notifyDataSetChanged()
        }

        inner class Holder(view: View) :
            RecyclerView.ViewHolder(view) {

        }
    }
}
