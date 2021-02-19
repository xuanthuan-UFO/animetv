package comm.xuanthuan.watchanime.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.R
import kotlinx.android.synthetic.main.fragment__recent3012.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class Fragment_Recent3012 : Fragment() {

    var list: ArrayList<Object_Anime3012> = ArrayList()
    var adapter: Adapter_itemHome3012? = null
    var rcl_recent: LoadMoreRecyclerView3012? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inf var view : View? = nulllate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment__recent3012, container, false)
        rcl_recent = view?.findViewById(R.id.rcl_recent)
        adapter = Adapter_itemHome3012(list, activity)
        rcl_recent?.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.scrollToPositionWithOffset(0, 20)
        rcl_recent?.layoutManager = gridLayoutManager
        rcl_recent?.setHasFixedSize(true)

        return view
    }

    fun load(){
        list.clear()
        try {
            adapter?.notifyDataSetChanged()
        } catch (e: Exception) {

        }
        container_recent?.visibility = View.VISIBLE
        val sharedPreferences = activity!!.getSharedPreferences("RECENT", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("dataRecent")) {
            val arrayData = sharedPreferences.getString("dataRecent", "")
            Log.d("zzz", "load: ok")
            try {
                val array = JSONArray(arrayData)
                for (i in 0 until array.length()) {
                    val `object` = array.getJSONObject(i)
                    val name = `object`.getString("name")
                    val href = `object`.getString("href")
                    val img = `object`.getString("img")
                    val release = `object`.getString("release")

                    list.add(Object_Anime3012(name, img, href, release))
                }
                container_recent?.visibility = View.INVISIBLE
                try {
                    Collections.reverse(list)
                    adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.d("zzz", "load:v" + e.message)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            container_recent?.visibility = View.INVISIBLE
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            load()
        }
    }

}