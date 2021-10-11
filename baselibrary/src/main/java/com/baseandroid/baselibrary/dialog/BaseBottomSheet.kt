package com.baseandroid.baselibrary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.baseandroid.baselibrary.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<BD : ViewDataBinding> : BottomSheetDialogFragment() {
    // region Const and Fields
    protected lateinit var binding: BD
    private var restore = false
    // endregion

    // region override method
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onStop() {
        restore = true
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        if (restore) {
            restore = false
            dialog?.window?.setWindowAnimations(
                R.style.DialogAnimation_Restore
            )
        } else {
            dialog?.window?.setWindowAnimations(
                R.style.DialogAnimation
            )
        }
    }
    // endregion

    // region abstract method
    abstract fun getLayoutId(): Int
    // endregion

    // region protected method
    protected open fun initViews() {

    }
    // endregion
}
