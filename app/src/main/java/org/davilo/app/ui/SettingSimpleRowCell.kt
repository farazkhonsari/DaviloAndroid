package org.davilo.app.ui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.SettingSimpleRowCellBinding
import org.davilo.app.model.App

class SettingSimpleRowCell(
    context: Context
) : FrameLayout(context) {
    fun setValue(resIcon: Int, textRes: Int) {
        binding.iconImageView.setImageResource(resIcon)
        binding.textViewTitle.setText(textRes)
        binding.iconImageView.colorFilter =
            PorterDuffColorFilter(0xFFffffff.toInt(), PorterDuff.Mode.MULTIPLY)


    }

    var app: App? = null
    val binding = SettingSimpleRowCellBinding.inflate(
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