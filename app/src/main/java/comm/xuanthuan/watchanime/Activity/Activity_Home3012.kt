package comm.xuanthuan.watchanime.Activity

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import comm.xuanthuan.watchanime.Adapter.Adapter_Genre_Home0601
import comm.xuanthuan.watchanime.Adapter.Adapter_ViewPager0501
import comm.xuanthuan.watchanime.Fragment.*
import comm.xuanthuan.watchanime.Object.GetFromAsset
import comm.xuanthuan.watchanime.R
import kotlinx.android.synthetic.main.activity__home3012.*

class Activity_Home3012 : AppCompatActivity() {
    public var domain: String? = null
    public var intcheck: Int? = null
    public var domainNewSeason: String? = null
    public var domainMovie: String? = null
    public var domainPopular: String? = null
    public var domainAnimeList: String? = null
    public var domainGenre: String? = null
    public var namePackage: String? = null
    public var imgnamePackage: String? = null
    var indexgenre = 0

    var listGenre: ArrayList<String> = ArrayList()
    var fmgenre = Fragment_Genre3012()

    var adapter: Adapter_Genre_Home0601? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__home3012)
        val sharedPreferences: SharedPreferences = getSharedPreferences("Confix", MODE_PRIVATE)
        if (sharedPreferences.contains("domain")) {
            domain = sharedPreferences.getString("domain", "")
            domainNewSeason = sharedPreferences.getString("domainNewSeason", "")
            domainMovie = sharedPreferences.getString("domainMovie", "")
            domainPopular = sharedPreferences.getString("domainPopular", "")
            domainAnimeList = sharedPreferences.getString("domainAnimeList", "")
            domainGenre = sharedPreferences.getString("domainGenre", "")
            namePackage = sharedPreferences.getString("namePackage", "")
            imgnamePackage = sharedPreferences.getString("imgnamePackage", "")
            intcheck = sharedPreferences.getString("check", "")!!.toInt()
        }

        if (intcheck == 3) {
            ln_search.visibility = View.GONE
            menu_anime_list.visibility = View.GONE
        } else {
            ln_search.visibility = View.VISIBLE
        }

        if (namePackage.equals("a")) {
            img_admod_myapp.visibility = View.GONE
        } else {
            try {
                Glide.with(this@Activity_Home3012).load(imgnamePackage).centerCrop().into(img_admod_myapp)
                img_admod_myapp.visibility = View.VISIBLE

                img_admod_myapp.setOnClickListener {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$namePackage")))
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$namePackage")))
                    }
                }

            } catch (e: Exception) {
            }
        }

        val sharedPreferencesCheckNum = getSharedPreferences("NumWatch", MODE_PRIVATE)
        if (sharedPreferencesCheckNum.contains("num")) {
            var checkNum = sharedPreferencesCheckNum.getInt("num", 0)
            if (checkNum > 5) {
                val sharedPreferencesRating = getSharedPreferences("Rating", MODE_PRIVATE)
                if (!sharedPreferencesRating.contains("rating")) {
                    val dialog = Dialog(this@Activity_Home3012)
                    dialog.setContentView(R.layout.dialog_rating)
                    val btnOK: TextView = dialog.findViewById(R.id.btn_ok_dialog_rating)
                    val btnCancel: TextView = dialog.findViewById(R.id.btn_cancel_dialog_rating)
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnOK.setOnClickListener {
                        sharedPreferencesRating.edit().putString("rating", "rate").apply()
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=comm.xuanthuan.watchanime")))
                        } catch (anfe: ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=comm.xuanthuan.watchanime")))
                        }
                        dialog.dismiss()
                    }
                    dialog.setCancelable(false)
                    dialog.show()

                }
            }
        } else {

        }


        val adapterViewpager: Adapter_ViewPager0501 =
            Adapter_ViewPager0501(supportFragmentManager, 0)
        adapterViewpager.addFm(Fragment_Home3012(), "Home")
        adapterViewpager.addFm(Fragment_Favourite3012(), "Favourite")
        adapterViewpager.addFm(Fragment_Recent3012(), "Recent")
        adapterViewpager.addFm(Fragment_AnimeList3012(), "Anime List")
        adapterViewpager.addFm(Fragment_NewSeason3012(), "New Season")
        adapterViewpager.addFm(Fragment_Movie3012(), "Movie")
        adapterViewpager.addFm(Fragment_Popular3012(), "Popular")
        adapterViewpager.addFm(fmgenre, "Genre")
        viewpager_activity_home.adapter = adapterViewpager

        viewpager_activity_home.setCurrentItem(0)
        viewpager_activity_home.offscreenPageLimit = 7
        viewpager_activity_home.setPagingEnabled(false)

        txt.text = "Home"
        init()
        loadGenre()

    }

    fun init() {
        val drawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_baseline_menu_24,
                this@Activity_Home3012.getTheme()
        )
        val toggle = ActionBarDrawerToggle(
                this,
                drawerlayout,
                toolbar_Home,
                R.string.open,
                R.string.close
        )
        toggle.setHomeAsUpIndicator(drawable)
        toggle.isDrawerIndicatorEnabled = false
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawerlayout.isDrawerVisible(GravityCompat.START)) {
                drawerlayout?.closeDrawer(GravityCompat.START)
            } else {
                drawerlayout?.openDrawer(GravityCompat.START)
            }
        }

        ln_search.setOnClickListener {
            val intentsearch = Intent(this@Activity_Home3012, Activity_Search0801::class.java)
            startActivity(intentsearch)
        }

        menu_home.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(0)
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = "Home"
            }
        })

        menu_favourite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(1)
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = "Favourite"
            }
        })

        menu_recent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(2)
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = "Recent"
            }
        })

        menu_anime_list.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(3)
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = "Anime List"
            }
        })
        menu_new_season.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(4)
                txt.text = "New Season"
                drawerlayout.closeDrawer(GravityCompat.START)
            }
        })
        menu_movie.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(5)
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = "Movie"
            }
        })

        menu_popular.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("zzz", "onClick: ")
                viewpager_activity_home.setCurrentItem(6)
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = "Popular"
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun loadGenre() {
        listGenre.add(resources.getString(R.string.Action))
        listGenre.add(resources.getString(R.string.Adventure))
        listGenre.add(resources.getString(R.string.Cars))
        listGenre.add(resources.getString(R.string.Comedy))
        listGenre.add(resources.getString(R.string.Dementia))
        listGenre.add(resources.getString(R.string.Demons))
        listGenre.add(resources.getString(R.string.Drama))
        listGenre.add(resources.getString(R.string.Dub))
        listGenre.add(resources.getString(R.string.Ecchi))
        listGenre.add(resources.getString(R.string.Fantasy))
        listGenre.add(resources.getString(R.string.Game))
        listGenre.add(resources.getString(R.string.Harem))
        listGenre.add(resources.getString(R.string.Hentai))
        listGenre.add(resources.getString(R.string.Historical))
        listGenre.add(resources.getString(R.string.Horror))
        listGenre.add(resources.getString(R.string.Kids))
        listGenre.add(resources.getString(R.string.Magic))
        listGenre.add(resources.getString(R.string.MartialArts))
        listGenre.add(resources.getString(R.string.Mecha))
        listGenre.add(resources.getString(R.string.Military))
        listGenre.add(resources.getString(R.string.Music))
        listGenre.add(resources.getString(R.string.Mystery))
        listGenre.add(resources.getString(R.string.Parody))
        listGenre.add(resources.getString(R.string.Police))
        listGenre.add(resources.getString(R.string.Psychological))
        listGenre.add(resources.getString(R.string.Romance))
        listGenre.add(resources.getString(R.string.Samurai))
        listGenre.add(resources.getString(R.string.School))
        listGenre.add(resources.getString(R.string.SciFi))
        listGenre.add(resources.getString(R.string.Seinen))
        listGenre.add(resources.getString(R.string.Shoujo))
        listGenre.add(resources.getString(R.string.ShoujoAi))
        listGenre.add(resources.getString(R.string.Shounen))
        listGenre.add(resources.getString(R.string.ShounenAi))
        listGenre.add(resources.getString(R.string.SliceofLife))
        listGenre.add(resources.getString(R.string.Space))
        listGenre.add(resources.getString(R.string.Sports))
        listGenre.add(resources.getString(R.string.SuperPower))
        listGenre.add(resources.getString(R.string.Supernatural))
        listGenre.add(resources.getString(R.string.Thriller))
        listGenre.add(resources.getString(R.string.Vampire))
        listGenre.add(resources.getString(R.string.Yaoi))
        listGenre.add(resources.getString(R.string.Yuri))

        adapter = Adapter_Genre_Home0601(listGenre, this@Activity_Home3012)
        gridView_Genre_home.setExpanded(true)
        gridView_Genre_home.adapter = adapter

        gridView_Genre_home.setOnItemClickListener { parent, view, position, id ->
            if (intcheck == 3) {
                viewpager_activity_home.setCurrentItem(7)
                try {
                    fmgenre.list.clear()
                    fmgenre.adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                }
                fmgenre.loadItem(GetFromAsset.getFromAssets("main.txt", this@Activity_Home3012))
                txt.text = listGenre.get(position).toString()
                drawerlayout.closeDrawer(GravityCompat.START)
            } else {
                viewpager_activity_home.setCurrentItem(7)
                indexgenre = position
                fmgenre.page = 1
                try {
                    fmgenre.list.clear()
                    fmgenre.adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                }
                fmgenre.load(domainGenre.toString() + listGenre.get(position).toString().toLowerCase())
                drawerlayout.closeDrawer(GravityCompat.START)
                txt.text = listGenre.get(position).toString()
            }

        }
    }
}