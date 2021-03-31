package comm.xuanthuan.watchanime.Activity

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Adapter.Adapter_ViewPager0501
import comm.xuanthuan.watchanime.Fragment.Fragment_Episodes0501
import comm.xuanthuan.watchanime.Fragment.Fragment_Summary0501
import comm.xuanthuan.watchanime.Object.*
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity__detail0401.*
import kotlinx.android.synthetic.main.activity__home3012.*
import kotlinx.android.synthetic.main.fragment__anime_list3012.*
import kotlinx.android.synthetic.main.fragment__episodes0501.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.*
import java.util.*

class Activity_Detail0401 : AppCompatActivity() {
    var name: String = ""
    var img: String = ""
    var hrefanime: String = ""
    var release: String = ""
    var typeAnime: String? = null
    var summary: String? = null
    var released: String? = null
    var status2: String? = null
    var status1: String? = null
    var idAnime: String? = null

    var genre: String = ""
    var domain: String? = null
    var domainChapter: String? = null

    val fmSummary = Fragment_Summary0501()
    val fmEpisodes = Fragment_Episodes0501()
    var objectWrite: JSONObject = JSONObject()
    var arrayWrite: JSONArray = JSONArray()

    var interstitialAd_gg: com.google.android.gms.ads.InterstitialAd =
        com.google.android.gms.ads.InterstitialAd(this)

    public var intcheck: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__detail0401)

        val sharedPreferences: SharedPreferences = getSharedPreferences("Confix", MODE_PRIVATE)
        if (sharedPreferences.contains("domain")) {
            domain = sharedPreferences.getString("domain", "")
            domainChapter = sharedPreferences.getString("domainChapter", "")
            intcheck = sharedPreferences.getString("check", "")!!.toInt()
        }

        toolbar_detail.setNavigationOnClickListener { onBackPressed() }

        var intent = intent
        try {
            intent.getSerializableExtra("data")
            if (intent != null) {
                var item = intent.getSerializableExtra("data") as Object_Anime3012
                name = item.name
                img = item.img
                hrefanime = item.href
                release = item.release
                txtDetail.text = "Name: " + name
                txt_toolbar_Detail.text = name.toString()

                saveRecent(name.toString(), img.toString(), hrefanime.toString(), release.toString())
                if (intcheck != 3) {
                    Glide.with(this@Activity_Detail0401).load(item.img).into(img_detail)
                }

                Log.d("zzz", "onCreate: loadDemo" + name)
                if (name!!.contains(".Hack//Sign")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_sign.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//Roots")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_roots.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//Quantum")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_quantum.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//Sign (Dub)")) {
                    loadDemo(GetFromAsset.getFromAssets("hacksign_dub.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//Liminality")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_liminality.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//Roots (Dub)")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_roots_dub.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//G.U. Trilogy")) {
                    loadDemo(GetFromAsset.getFromAssets("hackgu_trilogy.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".Hack//G.U. Returner")) {
                    loadDemo(GetFromAsset.getFromAssets("hackgu_returner.txt", this@Activity_Detail0401))
                } else if (name.toString().contains("Hacka Doll The Animation")) {
                    loadDemo(GetFromAsset.getFromAssets("hacka_doll_the_animation.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//Legend of the Twilight")) {
                    loadDemo(GetFromAsset.getFromAssets("hacklegend_of_the_twilight.txt", this@Activity_Detail0401))
                } else if (name.toString().contains(".hack//The Movie: Sekai no Mukou ni")) {
                    loadDemo(GetFromAsset.getFromAssets("hackthe_movie_sekai_no_mukou_ni.txt", this@Activity_Detail0401))
                } else {
                    loadAnime(domain + item.href.substring(1))
                }
            }
        } catch (e: Exception) {
            Log.d("zzz", "onCreate: log  " + e.message)
        }

        try {
            intent.getSerializableExtra("dataAnimeList")
            if (intent != null) {
                var item = intent.getSerializableExtra("dataAnimeList") as Object_AnimeList3112
                name = item.name
                hrefanime = item.href
                txtDetail.text = resources.getString(R.string.Name) + " "+ name
                txt_toolbar_Detail.text = name.toString()
                loadAnimeList(domain + item.href.substring(1))

            }
        } catch (e: Exception) {
        }

        isStoragePermissionGranted()
    }

    fun loadDemo(responseString: String) {
         val document: Document = Jsoup.parse(responseString)
        try {
            typeAnime =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                            .getElementsByTag("p")
                            .get(1).getElementsByTag("a").text()
            txtDetailType.text =  resources.getString(R.string.Type) + " " + typeAnime
        } catch (e: Exception) {
        }

        try {
            summary =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                            .getElementsByTag("p")
                            .get(2).text()
        } catch (e: Exception) {
            Log.d("zzz", "onSuccess: mmmmmmmmm" + e.message)
        }

        try {
            val itemgenrehtml =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                            .getElementsByTag("p")
                            .get(3).getElementsByTag("a")

            for (i in itemgenrehtml) {
                genre = genre.plus(", " + i.text())
            }

            txtDetailGenre.text = resources.getString(R.string.Genre) + " " + genre
        } catch (e: Exception) {
        }

        try {
            released =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                            .getElementsByTag("p")
                            .get(4).text()
            txtDetailReleased.text = released
        } catch (e: Exception) {
        }

        try {
            status2 =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                            .getElementsByTag("p")
                            .get(5).getElementsByTag("a").text()

            txtDetailStatus.text = status2
        } catch (e: Exception) {
        }

        try {
            idAnime = document.getElementsByClass("anime_info_episodes_next").get(0).getElementsByTag("input ").get(0).attr("value")
        } catch (e: Exception) {
        }

        container_detail.visibility = View.INVISIBLE
        addFragment()
    }

    fun loadAnime(href: String) {
        Log.d("zzz", "loadAnime: ??" + href)
        container_detail.visibility = View.VISIBLE
        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.get(href, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                container_detail.visibility = View.INVISIBLE
                if (responseString != null) {
                    val document: Document = Jsoup.parse(responseString)
                    try {
                        typeAnime =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(1).getElementsByTag("a").text()
                        txtDetailType.text = resources.getString(R.string.Type) + " "  + typeAnime
                    } catch (e: Exception) {
                    }

                    try {
                        summary =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(2).text()
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: mmmmmmmmm" + e.message)
                    }


                    try {
                        val itemgenrehtml =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(3).getElementsByTag("a")

                        for (i in itemgenrehtml) {
                            genre = genre.plus(", " + i.text())
                        }

                        txtDetailGenre.text = resources.getString(R.string.Genre) + " "  + genre
                    } catch (e: Exception) {
                    }


                    try {
                        released =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(4).text()
                        txtDetailReleased.text = released
                    } catch (e: Exception) {
                    }


                    try {
                        status2 =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(5).getElementsByTag("a").text()

                        txtDetailStatus.text = status2
                    } catch (e: Exception) {
                    }

                    try {
                        idAnime = document.getElementsByClass("anime_info_episodes_next").get(0)
                            .getElementsByTag("input ").get(0).attr("value")
                    } catch (e: Exception) {

                    }

                    addFragment()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                try {
                    val someTask = someTask()
                    someTask.execute(href)
                } catch (e: Exception) {
                }
            }
        })


    }

    fun loadAnimeList(href: String) {
        Log.d("zzz", "loadAnime: " + href)
        container_detail.visibility = View.VISIBLE
        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.get(href, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                container_detail.visibility = View.INVISIBLE
                if (responseString != null) {
                    val document: Document = Jsoup.parse(responseString)
                    try {
                        img = document.getElementsByClass("anime_info_body_bg").get(0)
                            .getElementsByTag("img").get(0).attr("src")
                        Glide.with(this@Activity_Detail0401).load(img).into(img_detail)
                    } catch (e: Exception) {
                    }

                    try {
                        typeAnime =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(1).getElementsByTag("a").text()
                        txtDetailType.text = resources.getString(R.string.Type) + " " + typeAnime
                    } catch (e: Exception) {
                    }

                    try {
                        summary =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(2).text()
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: mmmmmmmmm" + e.message)
                    }


                    try {
                        val itemgenrehtml =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(3).getElementsByTag("a")

                        for (i in itemgenrehtml) {
                            genre = genre.plus(", " + i.text())
                        }

                        txtDetailGenre.text = resources.getString(R.string.Genre) + " "  + genre
                    } catch (e: Exception) {
                    }


                    try {
                        released =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(4).text()
                        txtDetailReleased.text = released
                    } catch (e: Exception) {
                    }

                    isStoragePermissionGranted()

                    try {
                        status2 =
                            document.getElementsByClass("anime_info_body_bg").get(0)
                                .getElementsByTag("p")
                                .get(5).getElementsByTag("a").text()

                        txtDetailStatus.text = status2
                    } catch (e: Exception) {
                    }

                    try {
                        idAnime = document.getElementsByClass("anime_info_episodes_next").get(0)
                            .getElementsByTag("input ").get(0).attr("value")
                    } catch (e: Exception) {

                    }

                    saveRecent(
                        name.toString(),
                        img.toString(),
                        hrefanime.toString(),
                        released.toString()
                    )
                    addFragment()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                try {
                    val someTask = someTasklist()
                    someTask.execute(href)
                } catch (e: Exception) {

                }
            }
        })


    }

    fun setupFavourite(name: String, href: String, img: String, release: String) {
        val dir =
                File(Environment.getExternalStorageDirectory().toString() + "/watchanime", "data.txt")
        if (dir.exists()) {
            try {
                val inputStream = FileInputStream(dir)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var line: String? = ""
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                val array = JSONArray(stringBuilder.toString())
                for (i in 0 until array.length()) {
                    val `object` = array.getJSONObject(i)
                    val name1 = `object`.getString("name")
                    if (name1 == name) {
                        img_Favourite.visibility = View.VISIBLE
                        img_Notfavourite.visibility = View.INVISIBLE
                        break
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        img_Notfavourite.setOnClickListener {
            img_Notfavourite.visibility = View.INVISIBLE
            img_Favourite.visibility = View.VISIBLE
            try {
                objectWrite.put("name", name)
                objectWrite.put("href", href)
                objectWrite.put("img", img)
                objectWrite.put("release", release)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            try {
                val dir = File(
                        Environment.getExternalStorageDirectory().toString() + "/watchanime",
                        "data.txt"
                )
                if (dir.exists()) {
                    try {
                        val inputStream = FileInputStream(dir)
                        val inputStreamReader = InputStreamReader(inputStream)
                        val bufferedReader = BufferedReader(inputStreamReader)
                        val stringBuilder = java.lang.StringBuilder()
                        var line: String? = ""
                        while (bufferedReader.readLine().also { line = it } != null) {
                            stringBuilder.append(line)
                        }
                        arrayWrite = JSONArray(stringBuilder.toString())
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: java.lang.Exception) {
            }

            arrayWrite.put(objectWrite)
            writeData(arrayWrite.toString() + "")
        }

        img_Favourite.setOnClickListener {
            img_Notfavourite.visibility = View.VISIBLE
            img_Favourite.visibility = View.INVISIBLE

            val dir = File(
                    Environment.getExternalStorageDirectory().toString() + "/watchanime",
                    "data.txt"
            )
            if (dir.exists()) {
                //dir.deleteOnExit();
                Log.d("zzz", "onClick: delete")
                try {
                    val fileInputStream = FileInputStream(dir)
                    val isr = InputStreamReader(fileInputStream)
                    val bufferedReader = BufferedReader(isr)
                    var line: String? = ""
                    val builder = java.lang.StringBuilder()
                    Log.d("zzz", "onClick: mkkkkkkkkkk")
                    while (bufferedReader.readLine().also { line = it } != null) {
                        builder.append(line)
                    }
                    try {
                        val array = JSONArray(builder.toString())
                        //  JSONObject object
                        Log.d("zzz", "onClick:  okkkk")
                        for (i in 0 until array.length()) {
                            val `object` = array.getJSONObject(i)
                            val nameFavourite = `object`.getString("name")
                            val img = `object`.getString("img")
                            val href = `object`.getString("href")
                            val release = `object`.getString("release")
                            if (nameFavourite.equals(name)) {
                                array.remove(i)
                                //deleteStore(array);
                                writeData(array.toString() + "")
                            }
                            Log.d("zzz", "onClick: name: $name")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d("zzz", "onClick: " + e.message)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Log.d("zzz", "onClick:  FileNotFoundException" + e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("zzz", "onClick:  IOException" + e.message)
                }
            }
        }
    }

    private fun writeData(data: String) {
        try {
            val folder = File(Environment.getExternalStorageDirectory().toString() + "/watchanime")
            var `var` = false
            if (!folder.exists()) `var` = folder.mkdir()
            val file = File(folder, "data.txt")
            val fos = FileOutputStream(file)
            fos.write(data.toByteArray())
            fos.close()
            Log.d("zzz", "writeData: ,aaaaaaaaa")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("zzz", "writeData: ,mmmmmmmmmmmmm" + e.message)
        }
    }

    fun addFragment() {
        val adapterViewPager = Adapter_ViewPager0501(supportFragmentManager, 0)
        adapterViewPager.addFm(fmSummary, resources.getString(R.string.Summary))
        adapterViewPager.addFm(fmEpisodes, resources.getString(R.string.Episode))
        viewPager_detail.adapter = adapterViewPager
        radioGroup_detail.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_Summary -> {
                    viewPager_detail.setCurrentItem(1)
                  //  Log.d("zzz", "onCreate: 2")
                }
                R.id.btn_Episodes -> {
                    viewPager_detail.setCurrentItem(0)
                  //  Log.d("zzz", "onCreate: 1")
                }

            }
        }

        viewPager_detail.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                radioGroup_detail.check(radioGroup_detail.getChildAt(position).getId())
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        viewPager_detail.offscreenPageLimit = 2
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("zzz", "Permission is granted")
                setupFavourite(name.toString(), hrefanime.toString(), img.toString(), release.toString())
                true
            } else {
                Log.v("zzz", "Permission is revoked")
                ActivityCompat.requestPermissions(
                        this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("zzz", "Permission: " + permissions[0] + "was " + grantResults[0])
            setupFavourite(name.toString(), hrefanime.toString(), img.toString(), release.toString())
        }
    }

    fun saveRecent(name: String, img: String, href: String, release: String) {
        val sharedPreferencesRecent: SharedPreferences = this@Activity_Detail0401.getSharedPreferences("RECENT", MODE_PRIVATE)
        val `object` = JSONObject()
        try {
            `object`.put("name", name)
            `object`.put("img", img)
            `object`.put("release", release)
            `object`.put("href", href)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        var arrayRecent = JSONArray()

        try {
            val arrayData = sharedPreferencesRecent.getString("dataRecent", "")
            arrayRecent = JSONArray(arrayData)
            for (i in 0 until arrayRecent.length()) {
                val object1: JSONObject = arrayRecent.getJSONObject(i)
                val checkName = object1.getString("name")
                if (name == checkName) {
                    arrayRecent.remove(i)
                }
            }
            Log.d("zzz", "saveRecent: ok")
        } catch (e:Exception) {
            Log.d("zzz", "saveRecent: " +e.message)
        }

        arrayRecent.put(`object`)
        sharedPreferencesRecent.edit().putString("dataRecent", arrayRecent.toString() + "").apply()

    }

    inner class someTasklist() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("zzz", "onPostExecute: " + result)

            container_detail.visibility = View.INVISIBLE
            val document: Document = Jsoup.parse(result)
            try {
                img = document.getElementsByClass("anime_info_body_bg").get(0)
                    .getElementsByTag("img").get(0).attr("src")
                Glide.with(this@Activity_Detail0401).load(img).into(img_detail)
            } catch (e: Exception) {
                Log.d("zzzimg", "onPostExecute: ttttttt" + img)
            }

            try {
                typeAnime =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(1).getElementsByTag("a").text()
                txtDetailType.text = resources.getString(R.string.Type) + " " + typeAnime
            } catch (e: Exception) {
            }

            try {
                summary =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(2).text()
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: mmmmmmmmm" + e.message)
            }


            try {
                val itemgenrehtml =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(3).getElementsByTag("a")

                for (i in itemgenrehtml) {
                    genre = genre.plus(", " + i.text())
                }

                txtDetailGenre.text = resources.getString(R.string.Genre) + " " + genre
            } catch (e: Exception) {
            }


            try {
                released =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(4).text()
                txtDetailReleased.text = released
            } catch (e: Exception) {
            }

            isStoragePermissionGranted()

            try {
                status1 = document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(5).getElementsByTag("a").text()

                txtDetailStatus.text = status1
            } catch (e: Exception) {
            }

            try {
                idAnime = document.getElementsByClass("anime_info_episodes_next").get(0)
                    .getElementsByTag("input ").get(0).attr("value")
            } catch (e: Exception) {

            }
            saveRecent(
                name.toString(),
                img.toString(),
                hrefanime.toString(),
                released.toString()
            )
            addFragment()

        }

        override fun doInBackground(vararg params: String?): String? {
            var result = ""
            try {
                val doc = Jsoup.connect(params[0].toString()).get()
                Log.d("zzz", "doInBackground: aâ")
                result = doc.toString()
            } catch (e: Exception) {
                Log.d("zzz", "doInBackground: zzzzzzzzz")
            }

            return result
        }
    }
    inner class someTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("zzz", "onPostExecute: " + result)

            container_detail.visibility = View.INVISIBLE
            val document: Document = Jsoup.parse(result)
            try {
                typeAnime =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(1).getElementsByTag("a").text()
                txtDetailType.text = resources.getString(R.string.Type) + " " + typeAnime
            } catch (e: Exception) {
            }

            try {
                summary =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(2).text()
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: mmmmmmmmm" + e.message)
            }


            try {
                val itemgenrehtml =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(3).getElementsByTag("a")

                for (i in itemgenrehtml) {
                    genre = genre.plus(", " + i.text())
                }

                txtDetailGenre.text = resources.getString(R.string.Genre) + " " + genre
            } catch (e: Exception) {
            }


            try {
                released =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(4).text()
                txtDetailReleased.text = released
            } catch (e: Exception) {
            }


            try {
                status2 =
                    document.getElementsByClass("anime_info_body_bg").get(0)
                        .getElementsByTag("p")
                        .get(5).getElementsByTag("a").text()

                txtDetailStatus.text = status2
            } catch (e: Exception) {
            }

            try {
                idAnime = document.getElementsByClass("anime_info_episodes_next").get(0)
                    .getElementsByTag("input ").get(0).attr("value")
            } catch (e: Exception) {

            }

            addFragment()
        }

        override fun doInBackground(vararg params: String?): String? {
            var result = ""
            try {
                val doc = Jsoup.connect(params[0].toString()).get()
                Log.d("zzz", "doInBackground: aâ")
                result = doc.toString()
            } catch (e: Exception) {
                Log.d("zzz", "doInBackground: zzzzzzzzz")
            }

            return result
        }
    }
}