package br.com.algorit.mangaalert.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.algorit.mangaalert.R
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.ui.dialog.GenericDialog
import br.com.algorit.mangaalert.ui.recyclerview.MangaRecyclerAdapter
import br.com.algorit.mangaalert.viewmodel.MangaViewModel

class MangaFragment(private val myContext: Context, private val mangas: List<Manga>) : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var mangaRecyclerAdapter: MangaRecyclerAdapter? = null
    private var mangaViewModel: MangaViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
        mangaViewModel!!.allMangas.observe(this, { returnedMangas: List<Manga> -> mangaRecyclerAdapter!!.setMangas(returnedMangas) })
        for (manga in mangas) {
            mangaViewModel!!.insert(manga)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manga, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        initAndConfigureRecycler(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        saveChanges()
        return super.onOptionsItemSelected(item)
    }

    private fun initAndConfigureRecycler(view: View) {
        recyclerView = view.findViewById(R.id.fragment_manga_recycler_view)
        mangaRecyclerAdapter = MangaRecyclerAdapter(context)
        recyclerView?.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        //        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
        recyclerView?.setAdapter(mangaRecyclerAdapter)
    }

    var itemTouchCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            mangas.removeAt(viewHolder.adapterPosition)
                mangaRecyclerAdapter!!.notifyDataSetChanged()
            }
        }

    private fun saveChanges() {
        val listaManga = (recyclerView!!.adapter as MangaRecyclerAdapter?)!!.checkedItems
        mangaViewModel!!.uncheckAll()
        for (manga in listaManga) {
            Log.i(
                MangaFragment::class.java.canonicalName,
                "============== SAVE CHANGES: " + manga.nome
            )
            mangaViewModel!!.updateChecked(manga)
        }
        GenericDialog(requireContext()).show("Preferências atualizadas!")
    }
}