package org.davilo.app.ui.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.EnroledModuleProgressBinding
import org.davilo.app.model.ModuleInfo

class EnrolledMouduleProgressCell(
    context: Context
) : FrameLayout(context) {
    var moduleInfo: ModuleInfo? = null
    val binding = EnroledModuleProgressBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )

    init {
        addView(
            binding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    fun bindModule(moduleInfo: ModuleInfo?) {
        this.moduleInfo = moduleInfo
        binding.moduleInfo = moduleInfo


    }
}