package org.davilo.app.ui.adapter.myBindingAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class MyBindingAdapter<M, B : ViewDataBinding>(
    private val context: Context?,
    private val items: List<M>?,
    @LayoutRes val layout: Int,
    val onBindViewHolder: (
        item: M,
        position: Int,
        binder: B
    ) -> Unit
) : RecyclerView.Adapter<BinderViewHolderParent<M, B>>() {

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val o = items?.get(position)
        return (o as? MultiViewType)?.itemViewType ?: 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BinderViewHolderParent<M, B> {
        val binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(context),
            layout,
            parent,
            false
        )
        return BinderViewHolderParent(binding, onBindViewHolder)
    }

    override fun onBindViewHolder(holder: BinderViewHolderParent<M, B>, position: Int) {
        items?.get(position)?.let { item ->
            holder.onBindViewHolder.invoke(item, position, holder.binder)
        }
    }

}
