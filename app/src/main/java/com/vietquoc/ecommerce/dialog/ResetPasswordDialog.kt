package com.vietquoc.ecommerce.dialog

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vietquoc.ecommerce.R

fun Fragment.setupBottomSetDialog(onSendClick: (String) -> Unit) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)

    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edittextEmail = view.findViewById<EditText>(R.id.editTextResetPassword)
    val buttonCancel = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.buttonCancelResetPassword)
    val buttonSend = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.buttonSendResetPassword)

    buttonSend.setOnClickListener {
        val email = edittextEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }
}

