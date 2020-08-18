package org.davilo.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.LevelCellBinding
import org.davilo.app.model.Level

class LevelCell(
    context: Context
) : FrameLayout(context) {
    interface Delegate {
        fun onEnrollSelected(level: Level?)

    }

    var level: Level? = null
    val binding = LevelCellBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )
    var delegate: Delegate? = null

    init {
        addView(
            binding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    fun bindLevel(level: Level?) {
        this.level = level
        binding.level = this.level
        if (level != null) {
            if (level.is_enroll) {
                binding.enrollButton.setText("Continue")
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.enrollButton.setText("Enroll")
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
        binding.enrollButton.setOnClickListener { delegate?.onEnrollSelected(level) }


    }
}