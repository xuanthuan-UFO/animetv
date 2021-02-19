package comm.xuanthuan.watchanime.Object

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.ads.AudienceNetworkAds

class Multidex1101 : MultiDexApplication() {

    fun Multidex2812() {}
    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}