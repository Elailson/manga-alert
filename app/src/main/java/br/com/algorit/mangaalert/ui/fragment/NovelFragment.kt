package br.com.algorit.mangaalert.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.algorit.mangaalert.R
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import br.com.algorit.mangaalert.ui.dialog.GenericDialog
import br.com.algorit.mangaalert.ui.recyclerview.ItemClickListener
import br.com.algorit.mangaalert.ui.recyclerview.NovelRecyclerAdapter
import br.com.algorit.mangaalert.viewmodel.NovelViewModel

class NovelFragment(private val myContext: Context, private val novels: List<Novel>) : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var novelRecyclerAdapter: NovelRecyclerAdapter? = null
    private var novelViewModel: NovelViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        novelViewModel = ViewModelProviders.of(this).get(NovelViewModel::class.java)
        novelViewModel!!.allNovels.observe(this, { returnedNovels: List<Novel> -> novelRecyclerAdapter!!.setNovels(returnedNovels) })
        for (novel in novels) {
            novelViewModel!!.insert(novel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_novel, container, false)
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
        recyclerView = view.findViewById(R.id.fragment_novel_recycler_view)
        novelRecyclerAdapter = NovelRecyclerAdapter(context)
        recyclerView?.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView?.setAdapter(novelRecyclerAdapter)
    }

    private fun saveChanges() {
        val listaNovel = (recyclerView!!.adapter as NovelRecyclerAdapter?)!!.checkedItems
        novelViewModel!!.uncheckAll()
        for (novel in listaNovel) {
            novelViewModel!!.updateChecked(novel)
        }
        GenericDialog(requireContext()).show("Preferências atualizadas!")
    }
}