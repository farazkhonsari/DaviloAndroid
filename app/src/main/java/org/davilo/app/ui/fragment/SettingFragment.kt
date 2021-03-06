package org.davilo.app.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.FragmentSettingBinding
import org.davilo.app.model.Enroll
import org.davilo.app.model.SettingViewModel
import org.davilo.app.ui.activity.intro.ActivityIntro
import org.davilo.app.ui.viewHolder.SettingHeaderCell
import org.davilo.app.ui.viewHolder.SettingSimpleRowCell
import org.davilo.app.utils.urlBrowserIntent


@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var settingAdapter: Adapter
    lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    var enroll: Enroll? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }


    private fun initBindings() {
        settingAdapter = Adapter()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.apply {
            this.adapter = settingAdapter
            layoutManager = linearLayoutManager

        }

    }


    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var contactUsRow: Int
        private var aboutUsRow: Int

        //        private var favoritesRow: Int
        private var headerRow: Int

        private var rowCount: Int = 0
        private var enroll: Enroll? = null

        init {
            rowCount = 0
            headerRow = rowCount++
//            favoritesRow = rowCount++
            aboutUsRow = rowCount++
            contactUsRow = rowCount++
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 0) {
                val view: SettingHeaderCell? =
                    context?.let {
                        SettingHeaderCell(
                            context = it
                        )
                    }
                view?.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                view?.binding?.logout?.setOnClickListener {
                    viewModel.logOut()
                    activity?.finishAffinity()
                    ActivityIntro.navigate(activity)
                }
                return Holder(view!!)
            } else {
                val view =
                    context?.let {
                        SettingSimpleRowCell(
                            context = it
                        )
                    }
                view?.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                view?.setOnClickListener {
                    urlBrowserIntent(context, "https://davilo.org")
                }
                return Holder(view!!)
            }

        }

        override fun getItemCount(): Int {
            return rowCount
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == headerRow) {
                0
            } else {
                1
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView is SettingSimpleRowCell) {
                when (position) {
//                    favoritesRow -> {
//                        (holder.itemView as SettingSimpleRowCell).setValue(
//                            R.drawable.ic_favourite,
//                            R.string.favorite
//                        )
//                    }
                    aboutUsRow -> {
                        (holder.itemView as SettingSimpleRowCell).setValue(
                            R.drawable.ic_information,
                            R.string.aboutUs
                        )
                    }
                    contactUsRow -> {
                        (holder.itemView as SettingSimpleRowCell).setValue(
                            R.drawable.ic_support,
                            R.string.support
                        )
                    }
                }
            } else if (holder.itemView is SettingHeaderCell) {
                viewModel.getEmail()?.let { (holder.itemView as SettingHeaderCell).setValue(it) }
            }
            holder.itemView.tag = position
        }


        inner class Holder(view: View) :
            RecyclerView.ViewHolder(view)
    }
}
