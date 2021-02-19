package comm.xuanthuan.watchanime.Fragment

import android.os.AsyncTask
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Activity.Activity_Home3012
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment__episodes0501.*
import kotlinx.android.synthetic.main.fragment__genre3012.*
import org.jsoup.Jsoup

class Fragment_Genre3012 : androidx.fragment.app.Fragment() {
    var list: ArrayList<Object_Anime3012> = ArrayList<Object_Anime3012>()
    var adapter: Adapter_itemHome3012? = null
    var rclGenre: LoadMoreRecyclerView3012? = null

    var page = 1
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment__genre3012, container, false)

        rclGenre = view?.findViewById(R.id.rcl_genre)
        adapter = Adapter_itemHome3012(list, activity)
        rclGenre?.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.scrollToPositionWithOffset(0, 20)
        rclGenre?.layoutManager = gridLayoutManager
        rclGenre?.setHasFixedSize(true)
        rclGenre?.setOnLoadMoreListener {
            if ((activity as Activity_Home3012).intcheck != 3) {
                if (page < 7) {
                    page++
                    try {
                        load((activity as Activity_Home3012).domainGenre.toString() + (activity as Activity_Home3012).listGenre.get((activity as Activity_Home3012).indexgenre).toString().toLowerCase() + "?page=" +page )

                    } catch (e: Exception) {

                    }
                }
            }

        }

        Log.d("zzz", "onCreateView: aaaaaaaaaaaaaaaa")
        return view
    }

    fun loadItem(responseString:String){
        try {
            val document = Jsoup.parse(responseString)
            val element =
                    document.getElementsByClass("last_episodes").get(0).getElementsByTag("li")
            for (li in element) {
                var name = li.getElementsByClass("name").get(0).getElementsByTag("a").text()
                var href =
                        li.getElementsByClass("name").get(0).getElementsByTag("a").attr("href")
                var img = li.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                        .getElementsByTag(
                                "img"
                        ).attr("src")
                var released = li.getElementsByClass("released").text()
                try {
                    list.add(Object_Anime3012(name, img, href, released))
                } catch (e: Exception) {
                    Log.d("zzz", "onSuccess: " + e.message)
                }
            }
            try {
                adapter?.notifyDataSetChanged()
               // rclGenre?.finishLoading()
                Log.d("zzz", "onSuccess: huuuuuuu")
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: huaaaa")
            }
            container_genre?.visibility = View.INVISIBLE
        } catch (e: Exception) {
            Log.d("zzz", "onSuccess: 2" + e.message)
        }
    }

    fun load(href: String) {
        container_genre?.visibility = View.VISIBLE
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
                        val element =
                            document.getElementsByClass("last_episodes").get(0)
                                .getElementsByTag("li")
                        for (li in element) {
                            var name =
                                li.getElementsByClass("name").get(0).getElementsByTag("a").text()
                            var href =
                                li.getElementsByClass("name").get(0).getElementsByTag("a")
                                    .attr("href")
                            var img =
                                li.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                                    .getElementsByTag(
                                        "img"
                                    ).attr("src")
                            var released = li.getElementsByClass("released").text()
                            try {
                                list.add(Object_Anime3012(name, img, href, released))
                            } catch (e: Exception) {
                                Log.d("zzz", "onSuccess: " + e.message)
                            }
                        }
                        Log.d("zzz", "onSuccess: " + list.size)

                        try {
                            adapter?.notifyDataSetChanged()
                            rclGenre?.finishLoading()
                            Log.d("zzz", "onSuccess: huuuuuuu")
                        } catch (e: Exception) {
                            Log.d("zzz", "onSuccess: huaaaa")
                        }

                        container_genre?.visibility = View.INVISIBLE
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: 2" + e.message)
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.d("zzz", "onFailure: ")
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
            container_genre?.visibility = View.INVISIBLE
            try {
                val document = Jsoup.parse(result)
                val element =
                    document.getElementsByClass("last_episodes").get(0)
                        .getElementsByTag("li")
                for (li in element) {
                    var name =
                        li.getElementsByClass("name").get(0).getElementsByTag("a").text()
                    var href =
                        li.getElementsByClass("name").get(0).getElementsByTag("a")
                            .attr("href")
                    var img =
                        li.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                            .getElementsByTag(
                                "img"
                            ).attr("src")
                    var released = li.getElementsByClass("released").text()
                    try {
                        list.add(Object_Anime3012(name, img, href, released))
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: " + e.message)
                    }
                }
                Log.d("zzz", "onSuccess: " + list.size)

                try {
                    adapter?.notifyDataSetChanged()
                    rclGenre?.finishLoading()
                    Log.d("zzz", "onSuccess: huuuuuuu")
                } catch (e: Exception) {
                    Log.d("zzz", "onSuccess: huaaaa")
                }

                container_genre?.visibility = View.INVISIBLE
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: 2" + e.message)
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