package comm.xuanthuan.watchanime.Fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import comm.xuanthuan.watchanime.Activity.Activity_Home3012
import comm.xuanthuan.watchanime.Adapter.Adapter_ViewPager0501
import comm.xuanthuan.watchanime.R
import kotlinx.android.synthetic.main.fragment__home3012.*

class Fragment_Home3012 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__home3012, container, false)
        val tablayout: TabLayout = view.findViewById(R.id.tablayout_home)
        val viewpager: ViewPager = view.findViewById(R.id.viewpager_home)
        val adapter: Adapter_ViewPager0501 =
            Adapter_ViewPager0501(activity!!.supportFragmentManager, 0)
        adapter.addFm(Fragment_RecentRelease0601(), resources.getString(R.string.RECENTRELEASE))
        adapter.addFm(Fragment_Dub0601(), resources.getString(R.string.DUB))
        adapter.addFm(Fragment_Chinese0601(), resources.getString(R.string.CHINESE))

        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)
        viewpager.offscreenPageLimit = 3
        return view
    }
}