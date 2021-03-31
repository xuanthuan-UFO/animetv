package comm.xuanthuan.watchanime.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.GetFromAsset
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_AnimeList3112
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment__recent_release0601.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.HttpRetryException

class Fragment_RecentRelease0601 : Fragment() {
    var page = 1
    var domain: String? = null
    var domainPageHome: String? = null
    var list: ArrayList<Object_Anime3012> = ArrayList<Object_Anime3012>()
    var adapter: Adapter_itemHome3012? = null
    var rclRecentRelease: LoadMoreRecyclerView3012? = null
    public var intcheck: Int? = null
    var progress_recentrelease : ProgressBar? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment__recent_release0601, container, false)
        rclRecentRelease = view.findViewById(R.id.rcl_recentRelease)
        progress_recentrelease = view.findViewById(R.id.progress_recentrelease)
        adapter = Adapter_itemHome3012(list, activity)
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.scrollToPositionWithOffset(0, 20)
        rclRecentRelease?.layoutManager = gridLayoutManager
        rclRecentRelease?.adapter = adapter
        rclRecentRelease?.setHasFixedSize(true)
        rclRecentRelease?.setOnLoadMoreListener {
            try {
                if (intcheck != 3) {
                    if (page < 7) {
                        page++
                        try {
                            loadAnime(
                                    domainPageHome!!.split("page=")[0] + "page=" + page + domainPageHome!!.split(
                                            "page="
                                    )[1] + "1"
                            )

                        } catch (e: Exception) {

                        }
                    }
                }
            } catch (e: Exception) {
            }

        }

        try {
            val sharedPreferences: SharedPreferences = activity?.getSharedPreferences(
                    "Confix",
                    Context.MODE_PRIVATE
            )!!
            if (sharedPreferences.contains("domain")) {
                domain = sharedPreferences.getString("domain", "")
                domainPageHome = sharedPreferences.getString("domainPageHome", "")
                intcheck = sharedPreferences.getString("check", "")!!.toInt()
                Log.d("zzz", "onCreateView: recentrelease" + domain)
                if (intcheck == 3) {
                    progress_recentrelease?.visibility = View.INVISIBLE
                    loadItem(GetFromAsset.getFromAssets("main.txt", activity))
                } else {
                    loadAnime(domain.toString())
                }
            }
        } catch (e: Exception) {
            Log.d("zzz", "onCreateView: recentrelease" + e.message)
        }


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
            if (intcheck == 3) {
                try {
                    list.addAll(list)
                } catch (e: Exception) {

                }

            }
            try {
                progress_recentrelease?.visibility = View.INVISIBLE
                adapter?.notifyDataSetChanged()
                rclRecentRelease?.finishLoading()
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: recentrelease 1" + e.message)
            }

        } catch (e: Exception) {
        }
    }

    fun loadAnime(href: String) {
        progress_recentrelease?.visibility = View.VISIBLE
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
                        val classdata = document.getElementsByClass("last_episodes loaddub").get(0)
                                .getElementsByTag(
                                        "ul"
                                ).get(0).getElementsByTag("li")

                        for (i in classdata) {
                            val href =
                                    i.getElementsByClass("img").get(0).getElementsByTag("a")
                                            .attr("href")
                            val img =
                                    i.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                                            .getElementsByTag(
                                                    "img"
                                            ).attr("src")
                            val name =
                                    i.getElementsByClass("name").get(0).getElementsByTag("a").text()
                            val episode = i.getElementsByClass("episode").get(0).text()

                            if (href.contains("-episode")) {
                                val hrefdetail = "/category" + href.split("-episode")[0]
                                list.add(Object_Anime3012(name, img, hrefdetail, episode))
                            }
                        }
                        progress_recentrelease?.visibility = View.INVISIBLE
                        try {
                            adapter?.notifyDataSetChanged()
                            rclRecentRelease?.finishLoading()

                        } catch (e: Exception) {
                            //   Log.d("zzz", "onSuccess: recentrelease 1" + e.message)
                        }
                    } catch (e: Exception) {
                        //  Log.d("zzz", "onSuccess: recentrelease 2" + e.message)
                    }
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
            ) {
                Log.d("zzz", "onFailure: recentrelease" + throwable.toString())
                try {
                    val task = someTask()
                    task.execute(href)
                } catch (e: Exception) {
                    Log.d("zzz", "onFailure: " + e.message)
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
                val classdata = document.getElementsByClass("last_episodes loaddub").get(0)
                        .getElementsByTag(
                                "ul"
                        ).get(0).getElementsByTag("li")

                for (i in classdata) {
                    val href =
                            i.getElementsByClass("img").get(0).getElementsByTag("a").attr("href")
                    val img = i.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                            .getElementsByTag(
                                    "img"
                            ).attr("src")
                    val name = i.getElementsByClass("name").get(0).getElementsByTag("a").text()
                    val episode = i.getElementsByClass("episode").get(0).text()

                    if (href.contains("-episode")) {
                        val hrefdetail = "/category" + href.split("-episode")[0]
                        list.add(Object_Anime3012(name, img, hrefdetail, episode))
                    }
                }
                progress_recentrelease?.visibility = View.INVISIBLE
                try {
                    adapter?.notifyDataSetChanged()
                    rclRecentRelease?.finishLoading()

                } catch (e: Exception) {
                    //   Log.d("zzz", "onSuccess: recentrelease 1" + e.message)
                }
            } catch (e: Exception) {
                //  Log.d("zzz", "onSuccess: recentrelease 2" + e.message)
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