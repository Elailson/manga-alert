package br.com.algorit.mangaalert.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import br.com.algorit.mangaalert.R
import br.com.algorit.mangaalert.retrofit.MangaService
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import br.com.algorit.mangaalert.ui.adapter.PagerAdapter
import br.com.algorit.mangaalert.util.BlockUI
import br.com.algorit.mangaalert.util.Notification
import br.com.algorit.mangaalert.util.Worker
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

class TabActivity : AppCompatActivity() {
    private var blockUI: BlockUI? = null
    private var interstitialAd: InterstitialAd? = null
    private var mangaService: MangaService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        val toolbar = findViewById<Toolbar>(R.id.activity_tab_toolbar)
        setSupportActionBar(toolbar)
        blockUI = BlockUI(this)
        blockUI!!.start()
        Notification(applicationContext)
        Worker(applicationContext)
        mangaService = MangaService()
        findMangas()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showAd()
        return false
    }

    private fun configTabLayout(mangas: List<Manga>, novels: List<Novel>) {
        val tabLayout = findViewById<TabLayout>(R.id.activity_tab_tablayout)
        tabLayout.addTab(tabLayout.newTab().setText("Mangá"))
        tabLayout.addTab(tabLayout.newTab().setText("Manhua"))
        tabLayout.addTab(tabLayout.newTab().setText("Novel"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        configViewPager(tabLayout, mangas, novels)
    }

    private fun configViewPager(tabLayout: TabLayout, mangas: List<Manga>, novels: List<Novel>) {
        val viewPager = findViewById<ViewPager>(R.id.activity_tab_pager)
        val adapter = PagerAdapter(supportFragmentManager,
            tabLayout.tabCount, applicationContext, mangas, novels)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Não necessário
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Não necessário
            }
        })
        configAdBanner()
        blockUI!!.stop()
    }

    private fun findMangas() {
        Log.i(
            TabActivity::class.java.canonicalName,
            "============================================ FIND MANGAS ============================================"
        )
        mangaService!!.findAll(object : MangaService.ResponseCallback<List<Manga>> {
            override fun success(response: List<Manga>) {
                findNovels(response)
            }

            override fun fail(erro: String) {
                Log.e(TabActivity::class.java.canonicalName, erro)
                blockUI!!.stop()
            }
        })
    }

    private fun findNovels(mangas: List<Manga>) {
        Log.i(
            TabActivity::class.java.canonicalName,
            "============================================ FIND NOVELS ============================================"
        )
        mangaService!!.findAllNovel(object : MangaService.ResponseCallback<List<Novel>> {
            override fun success(response: List<Novel>) {
                configTabLayout(mangas, response)
                blockUI!!.stop()
            }

            override fun fail(erro: String) {
                Log.e(TabActivity::class.java.canonicalName, erro)
                blockUI!!.stop()
            }
        })
    }

    private fun configAdBanner() {
        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        initAdMob()
    }

    private fun initAdMob() {
        val idTeste = "ca-app-pub-3940256099942544/1033173712"
        val idProd = "ca-app-pub-6750275666832506/4671759050"
        MobileAds.initialize(this) { initializationStatus: InitializationStatus? -> }
        InterstitialAd.load(this, idTeste,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                InterstitialAd.load(applicationContext, idTeste,
                                    AdRequest.Builder().build(),
                                    object : InterstitialAdLoadCallback() {
                                        override fun onAdLoaded(ad: InterstitialAd) {
                                            interstitialAd = ad
                                        }
                                    })
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    InterstitialAd.load(applicationContext, idTeste,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(ad: InterstitialAd) {
                                interstitialAd = ad
                            }
                        })
                }
            })
    }

    private fun showAd() {
        if (interstitialAd != null) interstitialAd!!.show(this) else initAdMob()
    }
}