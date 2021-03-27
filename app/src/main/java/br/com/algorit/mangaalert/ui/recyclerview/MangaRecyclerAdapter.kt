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
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import java.util.*

class MangaRecyclerAdapter(context: Context?) :
    RecyclerView.Adapter<MangaRecyclerAdapter.MyViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var listaManga: List<Manga>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = layoutInflater.inflate(R.layout.item_lista, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listaManga != null) {
            val manga = listaManga!![position]
            holder.titulo.text = manga.nome
            holder.checkBox.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                manga.isChecked = isChecked
            }
            holder.checkBox.isChecked = manga.isChecked
        }
    }

    fun setMangas(mangas: List<Manga>) {
        listaManga = mangas
        if (listaManga != null) {
            Collections.sort(listaManga) { obj: Manga, manga: Manga -> obj.compareTo(manga) }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (listaManga != null) listaManga!!.size else 0
    }

    val checkedItems: List<Manga>
        get() {
            val checkedItems: MutableList<Manga> = ArrayList()
            for (manga in listaManga!!) {
                if (manga.isChecked) checkedItems.add(manga)
            }
            return checkedItems
        }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.item_lista_check)
        val titulo: TextView = itemView.findViewById(R.id.item_lista_titulo)
    }

}