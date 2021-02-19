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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.GetFromAsset
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment__chinese0601.*
import kotlinx.android.synthetic.main.fragment__dub0601.*
import org.jsoup.Jsoup

class Fragment_Chinese0601 : Fragment() {
    var page = 1
    var domain: String? = null
    var domainPageHome: String? = null
    var list: ArrayList<Object_Anime3012> = ArrayList<Object_Anime3012>()
    var adapter: Adapter_itemHome3012? = null
    var rclChinese: LoadMoreRecyclerView3012? = null
    public var intcheck: Int? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment__chinese0601, container, false)

        rclChinese = view.findViewById(R.id.rcl_Chinese)
        adapter = Adapter_itemHome3012(list, activity)
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.scrollToPositionWithOffset(0, 20)
        rclChinese?.layoutManager = gridLayoutManager
        rclChinese?.adapter = adapter
        rclChinese?.setHasFixedSize(true)

        rclChinese?.setOnLoadMoreListener {
            try {
                if (intcheck != 3) {
                    if (page < 7) {
                        page++
                        try {
                            loadAnime(
                                    domainPageHome!!.split("page=")[0] + "page=" + page + domainPageHome!!.split(
                                            "page="
                                    )[1] + "3"
                            )

                        } catch (e: Exception) {

                        }
                    }
                }
            } catch (e: Exception) {
            }
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
            progress_Chinese?.visibility = View.INVISIBLE
            try {
                adapter?.notifyDataSetChanged()
                rcl_Chinese?.finishLoading()
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: recentrelease 1" + e.message)
            }
        } catch (e: Exception) {
        }

    }

    fun loadAnime(href: String) {
        progress_Chinese?.visibility = View.VISIBLE
        Log.d("zzz", "loadItem: recentrelease" + href)
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
                            .getElementsByTag("ul").get(0).getElementsByTag("li")
                        for (i in classdata) {
                            val href = i.getElementsByClass("img").get(0).getElementsByTag("a")
                                .attr("href")
                            val img =
                                i.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                                    .getElementsByTag("img").attr("src")
                            val name =
                                i.getElementsByClass("name").get(0).getElementsByTag("a").text()
                            val episode = i.getElementsByClass("episode").get(0).text()
                            //     Log.d("zzz", "onSuccess: recentrelease" + href + " " + img + " " + name + " " + episode)

                            if (href.contains("-episode")) {
                                val hrefdetail = "/category" + href.split("-episode")[0]
                                list.add(Object_Anime3012(name, img, hrefdetail, episode))
                            }

                            //  Log.d("zzz", "onSuccess: recentrelease 3" + list?.get(1)?.name)
                        }
                        //  Log.d("zzz", "onSuccess: recentrelease" + list.size)
                        progress_Chinese?.visibility = View.INVISIBLE
                        try {
                            adapter?.notifyDataSetChanged()
                            rcl_Chinese?.finishLoading()

                        } catch (e: Exception) {
                            Log.d("zzz", "onSuccess: recentrelease 1" + e.message)
                        }
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: recentrelease 2" + e.message)
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                //Log.d("zzz", "onFailure: recentrelease" + responseString)
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
                val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("Confix", Context.MODE_PRIVATE)!!
                if (sharedPreferences.contains("domain")) {
                    domain = sharedPreferences.getString("domain", "")
                    domainPageHome = sharedPreferences.getString("domainPageHome", "")
                    intcheck = sharedPreferences.getString("check", "")!!.toInt()
                    if (intcheck == 3) {
                        if (list.isEmpty()) {
                            loadItem(GetFromAsset.getFromAssets("main.txt", activity))
                        }
                    } else {
                        if (list.isEmpty()) {
                            loadAnime(domainPageHome!!.split("page=")[0] + "page=" + "1" + domainPageHome!!.split("page=")[1] + "3")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("zzz", "setUserVisibleHint: " + e.message)
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
                val document = Jsoup.parse(result)
                val classdata = document.getElementsByClass("last_episodes loaddub").get(0)
                    .getElementsByTag("ul").get(0).getElementsByTag("li")
                for (i in classdata) {
                    val href = i.getElementsByClass("img").get(0).getElementsByTag("a")
                        .attr("href")
                    val img =
                        i.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                            .getElementsByTag("img").attr("src")
                    val name =
                        i.getElementsByClass("name").get(0).getElementsByTag("a").text()
                    val episode = i.getElementsByClass("episode").get(0).text()
                    //     Log.d("zzz", "onSuccess: recentrelease" + href + " " + img + " " + name + " " + episode)

                    if (href.contains("-episode")) {
                        val hrefdetail = "/category" + href.split("-episode")[0]
                        list.add(Object_Anime3012(name, img, hrefdetail, episode))
                    }

                    //  Log.d("zzz", "onSuccess: recentrelease 3" + list?.get(1)?.name)
                }
                //  Log.d("zzz", "onSuccess: recentrelease" + list.size)
                progress_Chinese?.visibility = View.INVISIBLE
                try {
                    adapter?.notifyDataSetChanged()
                    rcl_Chinese?.finishLoading()

                } catch (e: Exception) {
                    Log.d("zzz", "onSuccess: recentrelease 1" + e.message)
                }
            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: recentrelease 2" + e.message)
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