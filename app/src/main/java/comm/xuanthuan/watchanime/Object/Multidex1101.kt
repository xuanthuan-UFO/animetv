package comm.xuanthuan.watchanime.Object

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds


class Multidex1101 : MultiDexApplication() {

    fun Multidex2812() {}
    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(this, "ca-app-pub-5895323412368469~5380865137")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}