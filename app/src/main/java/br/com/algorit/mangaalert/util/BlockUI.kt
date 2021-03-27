package br.com.algorit.mangaalert.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import br.com.algorit.mangaalert.R
import java.util.*

class BlockUI(private val context: Context) {
    private var dialog: AlertDialog? = null

    fun start() {
        val alertDialog = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.loading, null)
        alertDialog.setView(view)
        dialog = alertDialog.create()
        Objects.requireNonNull(dialog!!.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun startWithCuriosity() {
        val alertDialog = AlertDialog.Builder(Objects.requireNonNull(context), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = Objects.requireNonNull(inflater).inflate(R.layout.loading_with_curiosity, null)
        alertDialog.setView(view)
        dialog = alertDialog.create()
        Objects.requireNonNull(dialog!!.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        val window = dialog!!.window
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        dialog!!.show()
    }

    fun exibeDialog(mensagem: String?) {
        val view = LayoutInflater.from(context).inflate(R.layout.mensagem, null)
        val textView = view.findViewById<TextView>(R.id.mensagem)
        textView.text = mensagem
        android.app.AlertDialog.Builder(context)
                .setView(view)
                .show()
    }

    fun stop() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }
}