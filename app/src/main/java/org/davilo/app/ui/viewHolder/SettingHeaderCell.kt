package org.davilo.app.ui.viewHolder

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.R
import org.davilo.app.databinding.SettingHeaderCellBinding
import org.davilo.app.model.App
import javax.inject.Inject

class SettingHeaderCell @Inject constructor(
    context: Context
) : FrameLayout(context) {
    fun setValue(email: String) {
        val str = context.resources.getString(R.string.LoggedInDescription)
        val start = str.indexOf("*num1*")
        val span = SpannableString(str.replace("*num1*", email))
        span.setSpan(
            StyleSpan(Typeface.BOLD), start, start+email.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewDescription.text = span


    }

    var app: App? = null
    val binding = SettingHeaderCellBinding.inflate(
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