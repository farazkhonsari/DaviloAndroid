package org.davilo.app.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.davilo.app.R
import org.davilo.app.databinding.FragmentListBinding
import org.davilo.app.model.Enroll
import org.davilo.app.model.ModuleInfo
import org.davilo.app.model.ObjectType
import org.davilo.app.ui.Delegate
import org.davilo.app.ui.EnrolledLevelCell

class ListFragment : Fragment() {

    private lateinit var objectId: String
    private lateinit var binding: FragmentListBinding
    private lateinit var objectType: ObjectType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }
    private fun loadArguments() {
        arguments?.getString("object_type")?.let {
            objectType = ObjectType.valueOf(it)
        }
        arguments?.getString("object_id")?.let {
            objectId = it
        }
    }


    inner class Adapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var enroll: Enroll? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: EnrolledLevelCell? =
                this@ListFragment.context?.let { EnrolledLevelCell(context = it) }
            view?.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view?.delegate = object : Delegate {
                override fun onModuleDidPressed(moduleInfo: ModuleInfo?) {
                    println(moduleInfo?.module)
                    val bundle = bundleOf(
                        "object_type" to ObjectType.Category,
                        "object_id" to moduleInfo?.id
                    )
                    Navigation.findNavController(view!!)
                        .navigate(R.id.action_home_to_details, bundle)
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
