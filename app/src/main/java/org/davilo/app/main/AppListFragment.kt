package org.davilo.app.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.databinding.AppFragmentListBinding
import org.davilo.app.model.App
import org.davilo.app.model.AppListViewModel
import org.davilo.app.ui.AppCell

@AndroidEntryPoint
class AppListFragment : Fragment() {

    private lateinit var homeAdapter: Adapter
    private lateinit var objectId: String
    private lateinit var binding: AppFragmentListBinding
    private val viewModel: AppListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AppFragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        homeAdapter = Adapter()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.apply {
            this.adapter = homeAdapter
            layoutManager = linearLayoutManager

        }
        observeData()
        viewModel.loadCurrentEnroll(objectId)
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.loading.show()
            } else {
                binding.loading.hide()
            }
        })
        viewModel.apps.observe(viewLifecycleOwner, Observer {
            homeAdapter.setApps(it)
        })

    }

    private fun loadArguments() {
        arguments?.getString("object_id")?.let {
            objectId = it
        }


    }


    inner class Adapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var array: ArrayList<App> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: AppCell? =
                this@AppListFragment.context?.let { AppCell(context = it) }
            view?.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            view?.setOnClickListener { view ->
                var intent = Intent(context, WebAppActivity::class.java)
                intent.putExtra(
                    "json", Gson().toJson((view as AppCell).app)
                )
                startActivity(intent)
            }
            return Holder(view!!)

        }

        override fun getItemCount(): Int {


            return array.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView is AppCell) {
                (holder.itemView as AppCell).bindCategory(array[position])
            }
        }

        fun setApps(apps: ArrayList<App>?) {
            if (apps != null) {
                this.array = apps
            }
            notifyDataSetChanged()
        }

        inner class Holder(view: View) :
            RecyclerView.ViewHolder(view) {

        }
    }

}
