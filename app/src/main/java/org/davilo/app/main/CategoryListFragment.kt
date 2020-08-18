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
import org.davilo.app.databinding.CategoryFragmentListBinding
import org.davilo.app.model.Category
import org.davilo.app.model.CategoryListViewModel
import org.davilo.app.ui.CategoryCell

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    private lateinit var homeAdapter: Adapter
    private lateinit var objectId: String
    private lateinit var binding: CategoryFragmentListBinding
    private val viewModel: CategoryListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CategoryFragmentListBinding.inflate(inflater)
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
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            homeAdapter.setCategories(it)
        })

    }

    private fun loadArguments() {
        arguments?.getString("object_id")?.let {
            objectId = it
        }


    }


    inner class Adapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var array: ArrayList<Category> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: CategoryCell? =
                this@CategoryListFragment.context?.let { CategoryCell(context = it) }
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


            return Holder(view!!)

        }

        override fun getItemCount(): Int {


            return array.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView is CategoryCell) {
                (holder.itemView as CategoryCell).bindCategory(array[position])
            }
        }

        fun setCategories(categories: ArrayList<Category>?) {
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
