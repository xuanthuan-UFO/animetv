package comm.xuanthuan.watchanime.Fragment

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.R
import kotlinx.android.synthetic.main.fragment__favourite3012.*
import kotlinx.android.synthetic.main.fragment__genre3012.*
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class Fragment_Favourite3012 : Fragment() {
    var list_itemFavorite: ArrayList<Object_Anime3012> = ArrayList()
    var adapter: Adapter_itemHome3012? = null
    var rcl_favourite : LoadMoreRecyclerView3012? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__favourite3012, container, false)

            adapter = Adapter_itemHome3012(list_itemFavorite, activity)
            rcl_favourite = view.findViewById(R.id.rcl_favourite)

            val gridLayoutManager = GridLayoutManager(activity, 2)
            gridLayoutManager.scrollToPositionWithOffset(0, 20)
            // rcl_newSeason.setLayoutManager(gridLayoutManager)
            rcl_favourite?.layoutManager = gridLayoutManager
            rcl_favourite?.setHasFixedSize(true)
            rcl_favourite?.adapter = adapter
            // adapter.notifyDataSetChanged()

        loadFavourite()
        return view
    }

    fun loadFavourite(){
        list_itemFavorite.clear()
        try {
            adapter?.notifyDataSetChanged()
        } catch (e: Exception) {

        }
        try {
            val dir = File(
                Environment.getExternalStorageDirectory().toString() + "/watchanime",
                "data.txt"
            )
            if (dir.exists()) {
                var inputStream: FileInputStream? = null
                try {
                    inputStream = FileInputStream(dir)
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
                        val name = `object`.getString("name")
                        val href = `object`.getString("href")
                        val img = `object`.getString("img")
                        val release = `object`.getString("release")
                        list_itemFavorite.add(
                            Object_Anime3012(
                                name,
                                img,
                                href,
                                release
                            )
                        )
                    }
                    try {
                        Collections.reverse(list_itemFavorite)
                        adapter?.notifyDataSetChanged()
                    } catch (e: Exception) {

                    }
//                    try {
//                        val adapter = Adapter_itemHome3012(list_itemFavorite, activity)
//                        val gridLayoutManager = GridLayoutManager(activity, 2)
//                        gridLayoutManager.scrollToPositionWithOffset(0, 20)
//                        // rcl_newSeason.setLayoutManager(gridLayoutManager)
//                        rcl_favourite.layoutManager = gridLayoutManager
//                        rcl_favourite.setHasFixedSize(true)
//                        rcl_favourite.adapter = adapter
//                        // adapter.notifyDataSetChanged()
//                    } catch (e: Exception) {
//                    }

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Log.d("zzz", "loadFavourite: data null")
            }
        } catch (e: Exception) {
            Log.d("zzz", "loadFavourite:  " + e.message)
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavourite()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            loadFavourite()
        }
    }
}