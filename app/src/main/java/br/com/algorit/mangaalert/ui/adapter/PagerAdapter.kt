package br.com.algorit.mangaalert.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import br.com.algorit.mangaalert.ui.fragment.MangaFragment
import br.com.algorit.mangaalert.ui.fragment.ManhuaFragment
import br.com.algorit.mangaalert.ui.fragment.NovelFragment

class PagerAdapter(manager: FragmentManager, private val numOfTabs: Int, private val context: Context,
                   private val mangas: List<Manga>, private val novels: List<Novel>) : FragmentStatePagerAdapter(manager, numOfTabs) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MangaFragment(context, mangas)
            1 -> ManhuaFragment()
            2 -> NovelFragment(context, novels)
            else -> throw IllegalArgumentException("Informe um parâmetro válido")
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }
}