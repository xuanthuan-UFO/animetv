package comm.xuanthuan.watchanime.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Adapter_ViewPager0501(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    val listfm : ArrayList<Fragment>
    val titles : ArrayList<String>
    init {
        listfm = ArrayList<Fragment>()
        titles = ArrayList<String>()
    }

    fun addFm(fm: Fragment, title: String) {
        listfm.add(fm)
        titles.add(title)
    }

    override fun getCount(): Int {
        return listfm.size
    }

    override fun getItem(position: Int): Fragment {
        return listfm[position]
    }

    override fun getPageTitle(i: Int): CharSequence? {
        return titles[i]
    }


}