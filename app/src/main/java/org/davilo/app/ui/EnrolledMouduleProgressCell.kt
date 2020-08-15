package org.davilo.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.CurrentEnroledLevelBinding
import org.davilo.app.model.Enroll

class EnrolledLevelCell(
    context: Context
) : FrameLayout(context) {
    var enroll: Enroll? = null

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


    }
}