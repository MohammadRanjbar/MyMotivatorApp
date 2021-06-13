package com.example.mymotivator.ui.mainFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.mymotivator.R

class LoadingDialog: DialogFragment(R.layout.loading_dialog_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}