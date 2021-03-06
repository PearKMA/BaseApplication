package com.baseandroid.baselibrary.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.baseandroid.baselibrary.R

abstract class BaseDialog<BD : ViewDataBinding> : DialogFragment() {
    // region Const and Fields
    protected lateinit var binding: BD
    private var restore = false
    // endregion

    // region override function
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                handleBackPress()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = cancelable()
        initViews()
    }

    override fun onStop() {
        super.onStop()
        restore = true
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

    // region abstract function
    abstract fun getLayoutId(): Int
    // endregion

    // region protected function
    protected open fun handleBackPress() {

    }

    protected open fun cancelable(): Boolean = true


    protected open fun initViews() {

    }
    // endregion
}