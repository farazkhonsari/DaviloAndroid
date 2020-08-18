package org.davilo.app.main

import android.content.Intent
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
import org.davilo.app.databinding.LevelFragmentListBinding
import org.davilo.app.model.Level
import org.davilo.app.model.LevelListViewModel
import org.davilo.app.ui.CategoryCell
import org.davilo.app.ui.LevelCell

@AndroidEntryPoint
class LevelListFragment : Fragment() {

    private lateinit var homeAdapter: Adapter
    private lateinit var objectId: String
    private lateinit var binding: LevelFragmentListBinding
    private val viewModel: LevelListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LevelFragmentListBinding.inflate(inflater)
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
        viewModel.loadLevels()
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.loading.show()
            } else {
                binding.loading.hide()
            }
        })
        viewModel.levels.observe(viewLifecycleOwner, Observer {
            homeAdapter.setCategories(it)
        })
        viewModel.isEnrollChanged.observe(viewLifecycleOwner, Observer {
            if (it) {
                activity?.finish()
                startActivity(Intent(context, MainActivity::class.java))

            }
        })

    }

    private fun loadArguments() {


    }


    inner class Adapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var array: ArrayList<Level> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: LevelCell? =
                this@LevelListFragment.context?.let { LevelCell(context = it) }
            view?.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view?.setOnClickListener { view ->
                var cell = view as CategoryCell
                val bundle = bundleOf(
                    "object_id" to (cell.category?.id ?: "")
                )
                Navigation.findNavController(view)
                    .navigate(R.id.go_to_apps, bundle)
            }
            view?.delegate = object : LevelCell.Delegate {

                override fun onEnrollSelected(level: Level?) {
                    viewModel.enrollLevel(level)
                }


            }


            return Holder(view!!)

        }

        override fun getItemCount(): Int {


            return array.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView is LevelCell) {
                (holder.itemView as LevelCell).bindLevel(array[position])
            }
        }

        fun setCategories(categories: ArrayList<Level>?) {
            if (categories != null) {
                this.array = categories
            }
            notifyDataSetChanged()
        }

        inner class Holder(view: View) :
            RecyclerView.ViewHolder(view) {

        }
    }

}
