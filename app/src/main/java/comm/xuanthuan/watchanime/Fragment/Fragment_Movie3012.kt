package comm.xuanthuan.watchanime.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Activity.Activity_Home3012
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.GetFromAsset
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment__episodes0501.*
import kotlinx.android.synthetic.main.fragment__movie3012.*
import kotlinx.android.synthetic.main.fragment__new_season3012.*
import org.jsoup.Jsoup

class Fragment_Movie3012 : Fragment() {

    var list: ArrayList<Object_Anime3012> = ArrayList()
    var adapter: Adapter_itemHome3012? = null
    var rcl_movie: LoadMoreRecyclerView3012? = null
    var i = 1

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__movie3012, container, false)

        rcl_movie = view?.findViewById(R.id.rcl_movie)
        adapter = Adapter_itemHome3012(list, activity)
        rcl_movie?.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.scrollToPositionWithOffset(0, 20)
        rcl_movie?.layoutManager = gridLayoutManager
        rcl_movie?.setHasFixedSize(true)
        rcl_movie?.setOnLoadMoreListener(object : LoadMoreRecyclerView3012.OnLoadMoreListener {
            override fun onLoadMore() {
                if ((activity as Activity_Home3012).intcheck != 3) {
                    if (i < 5) {
                        i++
                        try {
                            loadAnime((activity as Activity_Home3012).domainMovie.toString() + "?page=" + i)
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        })

        return view
    }

    fun loadItem(responseString: String) {
        try {
            var document = Jsoup.parse(responseString)
            var element =
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
                list.add(Object_Anime3012(name, img, href, released))
            }

            if ((activity as Activity_Home3012).intcheck == 3) {
                try {
                    list.addAll(list)
                } catch (e: Exception) {

                }
            }

            try {
                adapter?.notifyDataSetChanged()
                rcl_movie?.finishLoading()
            } catch (e: Exception) {
            }
            container_movie?.visibility = View.INVISIBLE

        } catch (e: Exception) {
        }
    }

    private fun loadAnime(href: String) {
        container_movie?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.get(href,
            object : TextHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?
                ) {
                    try {
                        var document = Jsoup.parse(responseString)
                        var element =
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
                            list.add(Object_Anime3012(name, img, href, released))
                        }
                        try {
                            adapter?.notifyDataSetChanged()
                            rcl_movie?.finishLoading()
                        } catch (e: Exception) {
                        }
                        container_movie?.visibility = View.INVISIBLE
                    } catch (e: Exception) {

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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            try {
                if ((activity as Activity_Home3012).intcheck != 3) {
                    if (list.isEmpty()) {
                        loadAnime((activity as Activity_Home3012).domainMovie.toString())
                    }
                } else {
                    if (list.isEmpty()) {
                        loadItem(GetFromAsset.getFromAssets("main.txt", activity))
                    }
                }
            } catch (e: Exception) {

            }
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

            try {
                var document = Jsoup.parse(result)
                var element =
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
                    list.add(Object_Anime3012(name, img, href, released))
                }
                try {
                    adapter?.notifyDataSetChanged()
                    rcl_movie?.finishLoading()
                } catch (e: Exception) {
                }
                container_movie?.visibility = View.INVISIBLE
            } catch (e: Exception) {
                container_movie?.visibility = View.INVISIBLE
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