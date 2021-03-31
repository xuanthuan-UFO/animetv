package comm.xuanthuan.watchanime.Activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.Object.VideoEnabledWebChromeClient0601
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity__watch_tv0601.*
import kotlinx.android.synthetic.main.fragment__dub0601.*
import org.jsoup.Jsoup
import java.util.*

class Activity_WatchTv0601 : AppCompatActivity() {
    var domain: String? = null
    var checkAdmob: String? = null
    var webChromeClient: VideoEnabledWebChromeClient0601? = null
    var hrefDownload: String? = null
    var hrefVideo: String? = null
    var interstitialAd: InterstitialAd? = null
    var interstitialAd1: InterstitialAd? = null
    var idFan: String? = null
    var idAdmob: String? = null
    var checkNumWatch = 0

    var interstitialAd_gg: com.google.android.gms.ads.InterstitialAd =
        com.google.android.gms.ads.InterstitialAd(this)
    var interstitialAd_gg1: com.google.android.gms.ads.InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__watch_tv0601)

        val sharedPreferences = getSharedPreferences("Confix", MODE_PRIVATE)
        if (sharedPreferences.contains("domain")) {
            domain = sharedPreferences.getString("domain", "")
            idFan = sharedPreferences.getString("idFan", "")
            checkAdmob = sharedPreferences.getString("checkAdmob", "")
            idAdmob = sharedPreferences.getString("idAdmob", "")
        }

        try {
            val intent1 = getIntent()
            var list1 = intent1.getSerializableExtra("data") as Object_ItemChapter0501
            hrefVideo = list1.href
            //    Log.d("dataa", "onCreate: " + domain + list1.href.substring(1))
        } catch (e: Exception) {
            Log.d("dataa", "onCreate: " + e.message)
        }

        val sharedPreferencesNumWatch = getSharedPreferences("NumWatch", MODE_PRIVATE)
        if (sharedPreferencesNumWatch.contains("num")) {
            try {
                checkNumWatch = sharedPreferencesNumWatch.getInt("num", 0)
                if (checkNumWatch > 15) {
                    if (checkAdmob?.toInt() == 1) {
                        addFb()
                    } else {
                        ggAdmob()
                    }
                } else {
                    try {
                        loadtv(domain + hrefVideo?.substring(2))
                    } catch (e: Exception) {
                    }
                }
                sharedPreferencesNumWatch.edit().putInt("num", checkNumWatch + 1).apply()
            } catch (e: Exception) {
            }
        } else {
            sharedPreferencesNumWatch.edit().putInt("num", checkNumWatch).apply()
            try {
                loadtv(domain + hrefVideo?.substring(2))
            } catch (e: Exception) {
            }
        }

        if (checkAdmob?.toInt() == 1) {
            addFb1()
        } else {
            ggAdmob1()
        }
        downLoad()
    }

    fun ggAdmob() {
        interstitialAd_gg.adUnitId = idAdmob
        interstitialAd_gg.loadAd(AdRequest.Builder().build())
        interstitialAd_gg.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                interstitialAd_gg.show()
                loadtv(domain + hrefVideo?.substring(2))
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                loadtv(domain + hrefVideo?.substring(2))
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

    fun ggAdmob1() {
        interstitialAd_gg1 = com.google.android.gms.ads.InterstitialAd(this)
        interstitialAd_gg1?.adUnitId = idAdmob
        interstitialAd_gg1?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("zzz", "onAdLoaded: 1111111111")

            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                Log.d("zzz", "onAdFailedToLoad: 11111111111111" + errorCode)

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
        interstitialAd_gg1?.loadAd(AdRequest.Builder().build())
    }

    fun addFb() {
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
                loadtv(domain + hrefVideo?.substring(2))
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("zzz", "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                interstitialAd?.show()
                //   loadTv()
                loadtv(domain + hrefVideo?.substring(2))
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

        interstitialAd?.loadAd(
            interstitialAd?.buildLoadAdConfig()
                ?.withAdListener(interstitialAdListener)
                ?.build()
        )
    }

    fun addFb1() {
        interstitialAd1 = InterstitialAd(this, idFan)
        val interstitialAdListener1: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e("zzz", "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e("zzz", "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad, adError: AdError) {
                Log.e("zzz", "Interstitial ad failed to load: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("zzz", "Interstitial ad is loaded and ready to be displayed!")
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
        interstitialAd1?.loadAd(
            interstitialAd1?.buildLoadAdConfig()
                ?.withAdListener(interstitialAdListener1)
                ?.build()
        )
    }

    fun downLoad() {
        container_download.setOnClickListener {
            val intentDown = Intent(this@Activity_WatchTv0601, ActivityDownLoad1101::class.java)
            intentDown.putExtra("hrefDownload", hrefDownload)
            startActivity(intentDown)
        }
    }

    fun loadtv(href: String) {
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
                        val hrefvideo =
                            document.getElementsByClass("play-video").get(0).getElementsByTag(
                                "iframe"
                            ).get(0).attr("src")

                        val nameAnime = document.getElementsByClass("anime_video_body").get(0)
                            .getElementsByTag("h1").text()
                        name_Anime_Watchtv.text = nameAnime

                        hrefDownload =
                            document.getElementsByClass("dowloads").get(0).getElementsByTag("a")
                                .get(0).attr("href")
                        val loadingVideo = layoutInflater.inflate(R.layout.view_loading_video, null)
                        webChromeClient = object : VideoEnabledWebChromeClient0601(
                            nonVideoLayout,
                            videoLayout,
                            loadingVideo,
                            container_video // See all available constructors...
                        ) {
                            // Subscribe to standard events, such as onProgressChanged()...
                            override fun onProgressChanged(view: WebView?, progress: Int) {
                                if (progress >= 80) {
                                    // progess_Tv_load.setVisibility(View.INVISIBLE);
                                    container_loadtv.setVisibility(View.INVISIBLE)
                                    //   Log.d("dataa", "onProgressChanged: " + progress)
                                }
                            }
                        }
                        container_video!!.setBackgroundColor(Color.BLACK)
                        webChromeClient?.setOnToggledFullscreen(object :
                            VideoEnabledWebChromeClient0601.ToggledFullscreenCallback {
                            override fun toggledFullscreen(fullscreen: Boolean) {
                                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                                if (fullscreen) {
                                    Log.d("zzz", "toggledFullscreen: a")
                                    requestedOrientation =
                                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                                    val attrs = window.attributes
                                    attrs.flags =
                                        attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                                    attrs.flags =
                                        attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                    window.attributes = attrs
                                    if (Build.VERSION.SDK_INT >= 14) {
                                        window.decorView.systemUiVisibility =
                                            View.SYSTEM_UI_FLAG_LOW_PROFILE
                                    }
                                } else {
                                    Log.d("zzz", "toggledFullscreen: b")
                                    requestedOrientation =
                                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    val attrs = window.attributes
                                    attrs.flags =
                                        attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                                    attrs.flags =
                                        attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
                                    window.attributes = attrs
                                    if (Build.VERSION.SDK_INT >= 14) {
                                        window.decorView.systemUiVisibility =
                                            View.SYSTEM_UI_FLAG_VISIBLE
                                    }
                                }
                            }
                        })

                        container_video?.setWebChromeClient(webChromeClient)
                        // Call private class InsideWebViewClient
                        //       webView!!.webViewClient = comm.xuanthuan.watchanime.Activity.Activity_WatchTv0601.InsideWebViewClient()
                        container_video?.webViewClient = InsideWebViewClient()
                        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site
                        container_video?.loadUrl("https:" + hrefvideo)
                    } catch (e: Exception) {
                        Log.d("dataa", "onSuccess: Exception 222222222" + e.message)
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

    inner private class InsideWebViewClient : WebViewClient() {
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.contains(domain.toString())) {
                view.loadUrl(url)
            }
            return true
        }
    }

    override fun onBackPressed() {
        try {
            interstitialAd1?.show()
        } catch (e: Exception) {
        }

        try {
            interstitialAd_gg1?.show()
        } catch (e: Exception) {

        }

        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        try {
            if (!webChromeClient?.onBackPressed()!!) {
                if (container_video.canGoBack()) {
                    container_video.goBack()
                } else {
                    // Standard back button implementation (for example this could close the app)
                    super.onBackPressed()
                }
            }
        } catch (e: Exception) {
            super.onBackPressed()
        }
    }

    inner class someTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val document = Jsoup.parse(result)
                val hrefvideo =
                    document.getElementsByClass("play-video").get(0).getElementsByTag(
                        "iframe"
                    ).get(0).attr("src")

                val nameAnime = document.getElementsByClass("anime_video_body").get(0)
                    .getElementsByTag("h1").text()
                name_Anime_Watchtv.text = nameAnime

                hrefDownload =
                    document.getElementsByClass("dowloads").get(0).getElementsByTag("a")
                        .get(0).attr("href")
                val loadingVideo = layoutInflater.inflate(R.layout.view_loading_video, null)
                webChromeClient = object : VideoEnabledWebChromeClient0601(
                    nonVideoLayout,
                    videoLayout,
                    loadingVideo,
                    container_video // See all available constructors...
                ) {
                    // Subscribe to standard events, such as onProgressChanged()...
                    override fun onProgressChanged(view: WebView?, progress: Int) {
                        if (progress >= 80) {
                            // progess_Tv_load.setVisibility(View.INVISIBLE);
                            container_loadtv.setVisibility(View.INVISIBLE)
                            //   Log.d("dataa", "onProgressChanged: " + progress)
                        }
                    }
                }
                container_video!!.setBackgroundColor(Color.BLACK)
                webChromeClient?.setOnToggledFullscreen(object :
                    VideoEnabledWebChromeClient0601.ToggledFullscreenCallback {
                    override fun toggledFullscreen(fullscreen: Boolean) {
                        // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                        if (fullscreen) {
                            requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                            val attrs = window.attributes
                            attrs.flags =
                                attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                            attrs.flags =
                                attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            window.attributes = attrs
                            if (Build.VERSION.SDK_INT >= 14) {
                                window.decorView.systemUiVisibility =
                                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            }
                        } else {
                            requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            val attrs = window.attributes
                            attrs.flags =
                                attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                            attrs.flags =
                                attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
                            window.attributes = attrs
                            if (Build.VERSION.SDK_INT >= 14) {
                                window.decorView.systemUiVisibility =
                                    View.SYSTEM_UI_FLAG_VISIBLE
                            }
                        }
                    }
                })

                container_video?.setWebChromeClient(webChromeClient)
                container_video?.webViewClient = InsideWebViewClient()
                container_video?.loadUrl("https:" + hrefvideo)
            } catch (e: Exception) {
                Log.d("dataa", "onSuccess: Exception 222222222" + e.message)
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
}