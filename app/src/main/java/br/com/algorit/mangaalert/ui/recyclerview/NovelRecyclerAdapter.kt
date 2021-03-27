package br.com.algorit.mangaalert.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.algorit.mangaalert.R
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import java.util.*

class NovelRecyclerAdapter(context: Context?) : RecyclerView.Adapter<NovelRecyclerAdapter.MyViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var novels: List<Novel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_lista, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (novels != null) {
            val novel = novels!![position]
            holder.titulo.text = novel.nome
            holder.checkBox.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> novel.isChecked = isChecked }
            holder.checkBox.isChecked = novel.isChecked
        }
    }

    fun setNovels(novels: List<Novel>) {
        this.novels = novels
        if (this.novels != null) {
            Collections.sort(this.novels) { obj: Novel, novel: Novel -> obj.compareTo(novel) }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (novels != null) novels!!.size else 0
    }

    val checkedItems: List<Novel>
        get() {
            val checkedItems: MutableList<Novel> = ArrayList()
            for (novel in novels!!) {
                if (novel.isChecked) checkedItems.add(novel)
            }
            return checkedItems
        }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.item_lista_check)
        val titulo: TextView = itemView.findViewById(R.id.item_lista_titulo)
    }

}