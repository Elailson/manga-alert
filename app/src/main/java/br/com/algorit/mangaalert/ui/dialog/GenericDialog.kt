package br.com.algorit.mangaalert.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import br.com.algorit.mangaalert.R

class GenericDialog(private val context: Context) {

    fun show(mensagem: String?) {
        val view = LayoutInflater.from(context).inflate(R.layout.mensagem, null)
        val textView = view.findViewById<TextView>(R.id.mensagem)
        textView.text = mensagem
        AlertDialog.Builder(context)
                .setView(view)
                .show()
    }
}