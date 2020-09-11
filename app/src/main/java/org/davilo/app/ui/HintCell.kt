package org.davilo.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.HintCellBinding
import org.davilo.app.model.Level

class HintCell(
    context: Context
) : FrameLayout(context) {
    interface Delegate {
        fun onEnrollSelected(level: Level?)

    }
    val binding = HintCellBinding.inflate(
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


}