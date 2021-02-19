package comm.xuanthuan.watchanime.Activity

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Adapter.Adapter_itemHome3012
import comm.xuanthuan.watchanime.Object.LoadMoreRecyclerView3012
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity__search0801.*
import kotlinx.android.synthetic.main.fragment__episodes0501.*
import org.jsoup.Jsoup


class Activity_Search0801 : AppCompatActivity() {
    var domainSearch: String? = null
    var list: ArrayList<Object_Anime3012> = ArrayList()
    var adapter: Adapter_itemHome3012? = null
    var i = 2
    // var rcl_search: LoadMoreRecyclerView3012? = null

    var keySearch = ""
    public var intcheck: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__search0801)

        toolbar_search.setNavigationOnClickListener { onBackPressed() }
        val sharedPreferences = getSharedPreferences("Confix", MODE_PRIVATE)
        if (sharedPreferences.contains("domain")) {
            domainSearch = sharedPreferences.getString("domainSearch", "")
            intcheck = sharedPreferences.getString("check", "")!!.toInt()
        }
        txt_search.setOnClickListener {
            keySearch = edt_search.text.toString()
            i=2
            try {
                list.clear()
                adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
            }

            load(domainSearch.toString() + keySearch)
            closeKey()
        }

        edt_search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                try {
                    list.clear()
                    adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                }
                i=2
                keySearch = edt_search.text.toString()
                load(domainSearch.toString() + keySearch)
                closeKey()
                return@OnEditorActionListener true
            }
            false
        })

        // rcl_search = findViewById(R.id.rcl_search)
        adapter = Adapter_itemHome3012(list, this@Activity_Search0801)
        rcl_search.adapter = adapter
        val gridLayoutManager = GridLayoutManager(this@Activity_Search0801, 2)
        gridLayoutManager.scrollToPositionWithOffset(0, 20)
        rcl_search.layoutManager = gridLayoutManager
        rcl_search.setHasFixedSize(true)
        rcl_search.setOnLoadMoreListener(object : LoadMoreRecyclerView3012.OnLoadMoreListener {
            override fun onLoadMore() {
                Log.d("zzz", "onLoadMore: aaaaa")
                if (i < 4) {
                    try {
                        load(domainSearch.toString() + keySearch + "&page=" + i)
                    } catch (e: Exception) {
                    }
                    i += 1
                }
            }
        })

    }

    fun closeKey(){
        //context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun load(href: String) {
        progress_search.visibility = View.VISIBLE
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
                        val item = document.getElementsByClass("last_episodes").get(0)
                            .getElementsByTag("ul").get(0).getElementsByTag("li")
                        for (i in item) {
                            var name =
                                i.getElementsByClass("name").get(0).getElementsByTag("a").text()
                            var href =
                                i.getElementsByClass("name").get(0).getElementsByTag("a")
                                    .attr("href")
                            var img =
                                i.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                                    .getElementsByTag(
                                        "img"
                                    ).attr("src")
                            var released = i.getElementsByClass("released").text()

                            list.add(Object_Anime3012(name, img, href, released))
                            //    Log.d("zzz", "onSuccess: " + name + href + img + released)
                        }

                        progress_search.visibility = View.INVISIBLE
                        try {
                            adapter?.notifyDataSetChanged()
                            rcl_search.finishLoading()
                        } catch (e: Exception) {

                        }

                    } catch (e: Exception) {
                    }
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
                val item = document.getElementsByClass("last_episodes").get(0)
                    .getElementsByTag("ul").get(0).getElementsByTag("li")
                for (i in item) {
                    var name =
                        i.getElementsByClass("name").get(0).getElementsByTag("a").text()
                    var href =
                        i.getElementsByClass("name").get(0).getElementsByTag("a")
                            .attr("href")
                    var img =
                        i.getElementsByClass("img").get(0).getElementsByTag("a").get(0)
                            .getElementsByTag(
                                "img"
                            ).attr("src")
                    var released = i.getElementsByClass("released").text()

                    list.add(Object_Anime3012(name, img, href, released))
                    //    Log.d("zzz", "onSuccess: " + name + href + img + released)
                }

                progress_search.visibility = View.INVISIBLE
                try {
                    adapter?.notifyDataSetChanged()
                    rcl_search.finishLoading()
                } catch (e: Exception) {

                }

            } catch (e: Exception) {
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