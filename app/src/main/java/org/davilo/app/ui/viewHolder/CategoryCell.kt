package org.davilo.app.ui.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.databinding.CategoryCellBinding
import org.davilo.app.model.Category

class CategoryCell(
    context: Context
) : FrameLayout(context) {
    var category: Category? = null
    val binding = CategoryCellBinding.inflate(
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

    fun bindCategory(category: Category?) {
        this.category = category
        binding.category = category

    }
}