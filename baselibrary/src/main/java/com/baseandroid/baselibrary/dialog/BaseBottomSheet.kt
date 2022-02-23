package com.baseandroid.baselibrary.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.baseandroid.baselibrary.R
import com.baseandroid.baselibrary.utils.screenHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<BD : ViewDataBinding> : BottomSheetDialogFragment() {
    // region Const and Fields
    protected lateinit var binding: BD
    private var restore = false
    // endregion

    // region override method
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = cancelable()
        initHeightDialog()
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
    protected open fun handleBackPress() {

    }

    protected open fun cancelable(): Boolean = true

    protected open fun initViews() {

    }

    protected open fun heightScale(): Float = 1.0f

    protected open fun getRootView(): View? = null
    // endregion

    // region private method
    private fun initHeightDialog() {
        val rootView = getRootView() ?: return

        val heightDevice = requireContext().screenHeight
        val maxHeight = heightDevice * heightScale()

        rootView.layoutParams.height = maxHeight.toInt()
        if (rootView.parent is View) {
            val behavior = BottomSheetBehavior.from(rootView.parent as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    // endregion
}
