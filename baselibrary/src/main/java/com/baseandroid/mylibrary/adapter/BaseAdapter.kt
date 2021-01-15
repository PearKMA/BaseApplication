package com.baseandroid.mylibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.baseandroid.mylibrary.BR
import com.baseandroid.mylibrary.model.BaseModel

class BaseAdapter<T: BaseModel>(private val inflater: LayoutInflater,
                                @LayoutRes private val resLayout: Int)
    : RecyclerView.Adapter<BaseAdapter.ViewHolderBase>() {
    // region Const and Fields
    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null
    // endregion

    // region override function
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBase {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resLayout, parent, false
        )
        return ViewHolderBase(binding)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        val item = data?.get(position)
        holder.binding.setVariable(BR.item, item)
        holder.binding.setVariable(BR.listener, listener)
        holder.binding.executePendingBindings()
    }
    // endregion

    // region ViewHolder
    class ViewHolderBase(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    // endregion

    // region listener
    interface ListItemListener
    // endregion
}