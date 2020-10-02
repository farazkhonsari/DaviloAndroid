package org.davilo.app.ui.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.CurrentEnroledLevelBinding

import org.davilo.app.model.Enroll
import org.davilo.app.model.ModuleInfo

interface Delegate {
    fun onModuleDidPressed(moduleInfo: ModuleInfo?)

}

class EnrolledLevelCell(
    context: Context
) : FrameLayout(context) {
    var delegate: Delegate? = null
    var enroll: Enroll? = null
    var mouduleProgressCellArray: ArrayList<EnrolledMouduleProgressCell> = ArrayList()
    val binding: CurrentEnroledLevelBinding = CurrentEnroledLevelBinding.inflate(
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

    fun setEnrollObject(enroll: Enroll?) {
        binding.enroll = enroll
        this.enroll = enroll

        if (enroll != null) {
            var i = 0
            for (moduleInfo in enroll.modules_info) {
                var cell: EnrolledMouduleProgressCell
                if (i < mouduleProgressCellArray.size) {
                    cell = mouduleProgressCellArray[i]
                } else {
                    cell =
                        EnrolledMouduleProgressCell(
                            context
                        )
                    mouduleProgressCellArray.add(cell)
                    binding.linearLayout.addView(
                        cell,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    cell.setOnClickListener { delegate?.onModuleDidPressed(cell.moduleInfo) }
                }
                cell.bindModule(moduleInfo)
                i++
            }
        }
    }
}