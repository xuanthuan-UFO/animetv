package comm.xuanthuan.watchanime.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Adapter.Adapter_DownLoad1101
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_Download1101
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_down_load1101.*
import kotlinx.android.synthetic.main.fragment__dub0601.*
import org.jsoup.Jsoup

class ActivityDownLoad1101 : AppCompatActivity() {
    var list_download : ArrayList<Object_Download1101> = ArrayList()
    var adapter: Adapter_DownLoad1101? = null
    var interstitialAd: InterstitialAd? = null
    var idFan :String? = null
    var hrefDownLoad : String? = null
    var idAdmob: String? = null
    var checkAdmob: String? = null

    var interstitialAd_gg: com.google.android.gms.ads.InterstitialAd =
        com.google.android.gms.ads.InterstitialAd(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down_load1101)

        toolbar_download.setNavigationOnClickListener {
            onBackPressed()
        }
        val sharedPreferences = getSharedPreferences("Confix", MODE_PRIVATE)
        if (sharedPreferences.contains("domain")) {
            idFan = sharedPreferences.getString("idFan", "")
            idAdmob = sharedPreferences.getString("idAdmob", "")
            checkAdmob = sharedPreferences.getString("checkAdmob", "")
        }

        adapter = Adapter_DownLoad1101(list_download, this@ActivityDownLoad1101)
        lv_Download.adapter = adapter

        val intent = intent
        try {
            hrefDownLoad = intent.getStringExtra("hrefDownload")
            load(hrefDownLoad.toString())
            Log.d("zzz", "onCreate: " + hrefDownLoad)
        } catch (e: Exception) {

        }

        if (checkAdmob?.toInt() == 1) {
            addFb()
        } else {
            ggAdmob()
        }


    }

    fun ggAdmob() {
        interstitialAd_gg.adUnitId = idAdmob
        interstitialAd_gg.loadAd(AdRequest.Builder().build())
        interstitialAd_gg.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                interstitialAd_gg.show()
                load(hrefDownLoad.toString())
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                load(hrefDownLoad.toString())
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        }
    }

    fun addFb() {
        AudienceNetworkAds.initialize(this)
        interstitialAd = InterstitialAd(this, idFan)
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e("zzz", "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e("zzz", "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.e("zzz", "Interstitial ad failed to load: " + adError.errorMessage)

//                unityListerrner = new ActivityDownLoad2611.UnityAdsListener();
//                UnityAds.initialize(ActivityDownLoad2611.this, idUnity, unityListerrner, testMode);
                load(hrefDownLoad.toString())
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("zzz", "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                interstitialAd?.show()
                load(hrefDownLoad.toString())
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d("zzz", "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d("zzz", "Interstitial ad impression logged!")
            }
        }

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd?.loadAd(
                interstitialAd?.buildLoadAdConfig()
                        ?.withAdListener(interstitialAdListener)
                        ?.build())
    }

    fun load(href: String) {
        container_download_progess.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.get(href, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                if (responseString != null) {
                    try {
                        val document = Jsoup.parse(responseString)
                        val fileName = document.getElementById("title").text()
                        val duration = document.getElementById("duration").text()
                        fileName_down.text = resources.getString(R.string.FileName) + fileName
                        duration_down.text = resources.getString(R.string.Duration) + duration
                        val elements = document.getElementsByClass("sumer_l")[0].getElementsByTag("li")
                        for (element in elements) {
                            val size = element.getElementsByTag("label").text()
                            if (size == "Size:") {
                                val sizeMb = element.getElementById("filesize").text()
                                size_down.text = resources.getString(R.string.Size) + sizeMb
                            } else if (size == "Res:") {
                                val sizeRes = element.getElementById("filesize").text()
                                res_down.text = resources.getString(R.string.Res) + sizeRes
                            }
                        }

                        val listElementDown = document.getElementsByClass("dowload")
                        for (element in listElementDown) {
                            val nameType = element.getElementsByTag("a")[0].text()
                            val hrefdown = element.getElementsByTag("a")[0].attr("href")
                            list_download.add(Object_Download1101(nameType, hrefdown))
                        }

                        try {
                            container_download_progess.visibility = View.INVISIBLE
                            adapter?.notifyDataSetChanged()
                        } catch (e: Exception) {

                        }
                    } catch (e: Exception) {

                    }

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

    inner class someTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("zzz", "onPostExecute: " + result)

            try {
                val document = Jsoup.parse(result)
                val fileName = document.getElementById("title").text()
                val duration = document.getElementById("duration").text()
                fileName_down.text = resources.getString(R.string.FileName) + fileName
                duration_down.text = resources.getString(R.string.Duration) + duration
                val elements = document.getElementsByClass("sumer_l")[0].getElementsByTag("li")
                for (element in elements) {
                    val size = element.getElementsByTag("label").text()
                    if (size == "Size:") {
                        val sizeMb = element.getElementById("filesize").text()
                        size_down.text = resources.getString(R.string.Size) + sizeMb
                    } else if (size == "Res:") {
                        val sizeRes = element.getElementById("filesize").text()
                        res_down.text =resources.getString(R.string.Res)+ sizeRes
                    }
                }

                val listElementDown = document.getElementsByClass("dowload")
                for (element in listElementDown) {
                    val nameType = element.getElementsByTag("a")[0].text()
                    val hrefdown = element.getElementsByTag("a")[0].attr("href")
                    list_download.add(Object_Download1101(nameType, hrefdown))
                }

                try {
                    container_download_progess.visibility = View.INVISIBLE
                    adapter?.notifyDataSetChanged()
                } catch (e: Exception) {

                }
            } catch (e: Exception) {

            }
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

    override fun onBackPressed() {
        super.onBackPressed()
    }
}