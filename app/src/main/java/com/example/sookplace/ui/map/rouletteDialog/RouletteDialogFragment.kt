package com.example.sookplace.ui.map.rouletteDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.sookplace.R

class RouletteDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // XML 레이아웃(dialog_roulette_options.xml) inflate
        val view: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_roulette_options, null)

        // AlertDialog로 감싸서 반환
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}