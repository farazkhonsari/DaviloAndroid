package org.davilo.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.AppCellBinding
import org.davilo.app.model.App

class AppCell(
    context: Context
) : FrameLayout(context) {
    var app: App? = null
    val binding = AppCellBinding.inflate(
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

    fun bindCategory(app: App?) {
        this.app = app
        binding.app = app
    }
}