package br.com.algorit.mangaalert.ui.recyclerview

import android.view.View

interface ItemClickListener {
    fun onItemClick(view: View?, position: Int)
}