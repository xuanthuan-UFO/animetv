package comm.xuanthuan.watchanime.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.flexbox.FlexboxLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Activity.Activity_Detail0401
import comm.xuanthuan.watchanime.Activity.Activity_Home3012
import comm.xuanthuan.watchanime.Adapter.Adapter_itemAnimeList3112
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.Object.Object_AnimeList3112
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment__anime_list3012.*
import kotlinx.android.synthetic.main.fragment__chinese0601.*
import kotlinx.android.synthetic.main.fragment__dub0601.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Fragment_AnimeList3012 : Fragment() {

    var listletter: ArrayList<String> = ArrayList()
    var list: ArrayList<Object_AnimeList3112> = ArrayList()
    var adapter: Adapter_itemAnimeList3112? = null

    var lv_animelist1: ListView? = null
    var index_letter = 0;

    var domainAnimeList: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__anime_list3012, container, false)
        lv_animelist1 = view.findViewById(R.id.lv_animelist)
        adapter = Adapter_itemAnimeList3112(list, activity, R.layout.item_animelist3112)
        lv_animelist1?.adapter = adapter

        lv_animelist1?.setOnItemClickListener { parent, view, position, id ->
            try {
                Log.d("zzz", "onCreateView: vvvvvvvv" + list.get(position).href)
                val intent = Intent(activity,Activity_Detail0401::class.java )
                intent.putExtra("dataAnimeList", list.get(position))
                activity?.startActivity(intent)
            } catch (e: Exception) {

            }

        }
        return view
    }

    fun loadLetter() {
        listletter.add(resources.getString(R.string.A))
        listletter.add(resources.getString(R.string.B))
        listletter.add(resources.getString(R.string.C))
        listletter.add(resources.getString(R.string.D))
        listletter.add(resources.getString(R.string.E))
        listletter.add(resources.getString(R.string.F))
        listletter.add(resources.getString(R.string.G))
        listletter.add(resources.getString(R.string.H))
        listletter.add(resources.getString(R.string.I))
        listletter.add(resources.getString(R.string.J))
        listletter.add(resources.getString(R.string.K))
        listletter.add(resources.getString(R.string.L))
        listletter.add(resources.getString(R.string.M))
        listletter.add(resources.getString(R.string.N))
        listletter.add(resources.getString(R.string.O))
        listletter.add(resources.getString(R.string.P))
        listletter.add(resources.getString(R.string.Q))
        listletter.add(resources.getString(R.string.R))
        listletter.add(resources.getString(R.string.S))
        listletter.add(resources.getString(R.string.T))
        listletter.add(resources.getString(R.string.U))
        listletter.add(resources.getString(R.string.V))
        listletter.add(resources.getString(R.string.W))
        listletter.add(resources.getString(R.string.X))
        listletter.add(resources.getString(R.string.Y))
        listletter.add(resources.getString(R.string.Z))

        for (j: Int in listletter.indices) {
            val linearLayout = layoutInflater.inflate(R.layout.tag_lay, flexbox_animelist, false) as LinearLayout
            val textView = linearLayout.findViewById<View>(R.id.tag_name) as TextView
            textView.setText(listletter.get(j))
            linearLayout.tag = j
            linearLayout.dividerPadding = 2
            if (j == 0) {
                index_letter = 0
                linearLayout.setBackgroundResource(R.color.clickletter)
            } else {
                linearLayout.setBackgroundResource(R.color.letter)
            }

            flexbox_animelist.addView(linearLayout, FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT))
            linearLayout.setOnClickListener {
                list?.clear()
                adapter?.notifyDataSetChanged()
                flexbox_animelist.getChildAt(index_letter).setBackgroundResource(R.color.letter)
                index_letter = linearLayout.tag as Int
                flexbox_animelist.getChildAt(index_letter).setBackgroundResource(R.color.clickletter)
                loadItem((activity as Activity_Home3012).domainAnimeList + listletter.get(linearLayout.tag as Int))
            }

        }

        loadItem((activity as Activity_Home3012).domainAnimeList.toString() + "A")
    }

    fun loadItem(href: String) {
        container_animelist?.visibility = View.VISIBLE
        val client: AsyncHttpClient = AsyncHttpClient()
        client.get(href, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                if (responseString != null) {
                    try {
                        val document: Document = Jsoup.parse(responseString)
                        var listli = document.getElementsByClass("anime_list_body").get(0)
                            .getElementsByClass("listing").get(0).getElementsByTag("li")
                        for (lis in listli) {
                            var title = lis.getElementsByTag("a").text()
                            var hrefTitle = lis.getElementsByTag("a").get(0).attr("href")
                            list?.add(Object_AnimeList3112(title, hrefTitle))
                        }
                        try {
                            adapter?.notifyDataSetChanged()
                        } catch (e: Exception) {
                        }
                        container_animelist?.visibility = View.INVISIBLE

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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            try {
                val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("Confix", Context.MODE_PRIVATE)!!
                if (sharedPreferences.contains("domain")) {
                    domainAnimeList = sharedPreferences.getString("domain", "")
                    if (list.isEmpty()) {
                        loadLetter()
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
                val document: Document = Jsoup.parse(result)
                var listli = document.getElementsByClass("anime_list_body").get(0)
                    .getElementsByClass("listing").get(0).getElementsByTag("li")
                for (lis in listli) {
                    var title = lis.getElementsByTag("a").text()
                    var hrefTitle = lis.getElementsByTag("a").get(0).attr("href")
                    list?.add(Object_AnimeList3112(title, hrefTitle))
                }
                try {
                    adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                }
                container_animelist?.visibility = View.INVISIBLE
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