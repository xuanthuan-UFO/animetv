package comm.xuanthuan.watchanime.Fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import comm.xuanthuan.watchanime.Activity.Activity_Detail0401
import comm.xuanthuan.watchanime.Activity.Activity_WatchTv0601
import comm.xuanthuan.watchanime.Adapter.Adapter_ItemChapter0601
import comm.xuanthuan.watchanime.Object.ExpandableHeightGridView
import comm.xuanthuan.watchanime.Object.GetFromAsset
import comm.xuanthuan.watchanime.Object.Object_AnimeList3112
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.R
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment__episodes0501.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Fragment_Episodes0501 : Fragment() {

    var list: ArrayList<Object_ItemChapter0501> = ArrayList<Object_ItemChapter0501>()
    var adapter: Adapter_ItemChapter0601? = null
    var gv_episodes1: ExpandableHeightGridView? = null

    var HackSign = arrayOf("SxEkQ7iH_YA", "DG9t9NBR-4k", "UNuJmQeMwJI", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs", "DG9t9NBR-4k", "RHkZgwl_phzA", "8LHq9rhCIAA", "akRfmEK3yak", "ld7mboHYnsg", "6sHKr-SalFs")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment__episodes0501, container, false)

        gv_episodes1 = view.findViewById(R.id.gv_episodes)
        adapter = Adapter_ItemChapter0601(list, activity!!)
        gv_episodes1?.setExpanded(true)
        gv_episodes1?.adapter = adapter

        gv_episodes1?.setOnItemClickListener { parent, view, position, id ->
            if ((activity as Activity_Detail0401).intcheck != 3) {
                val intent = Intent(activity, Activity_WatchTv0601::class.java)
                intent.putExtra("data", list.get(position))
                activity!!.startActivity(intent)
            } else {
                dialog(position)
            }
        }

        return view
    }


    fun loadDemo(responseString: String) {
        Log.d("zzz", "loadDemo: episodes")
        try {
            val document = Jsoup.parse(responseString)
            val element = document.getElementsByTag("ul").get(0).getElementsByTag("li")
            for (i in element) {
                try {
                    val nameChap = i.text()
                    val hrefchap = i.getElementsByTag("a").get(0).attr("href")
                    list.add(Object_ItemChapter0501(nameChap, hrefchap))
                } catch (e: Exception) {
                }

            }
            try {
                progress_episodes?.visibility = View.INVISIBLE
                adapter?.notifyDataSetChanged()
            } catch (e: Exception) {

            }

        } catch (e: Exception) {

        }
    }

    fun loadChap(href: String) {
        Log.d("zzz", "loadChap: " + href)
        progress_episodes?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.get(href, object : TextHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?
            ) {
                try {
                    val document = Jsoup.parse(responseString)
                    val lis = document.getElementsByTag("ul").get(0).getElementsByTag("li")
                    for (i in lis) {
                        try {
                            val hrefchap = i.getElementsByTag("a").get(0).attr("href")
                            val nameChap = i.text()
                            list.add(Object_ItemChapter0501(nameChap, hrefchap))
                        } catch (e: Exception) {
                            Log.d("zzz", "onSuccess: " + e.message)
                        }
                    }

                    try {
                        progress_episodes?.visibility = View.INVISIBLE
                        adapter?.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: " + e.message)
                    }

                } catch (e: Exception) {
                    Log.d("zzz", "onSuccess: ooooooooooo  " + e.message)
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

    private fun dialog(position: Int) {
        val dialogStart = AlertDialog.Builder(activity!!)
        dialogStart.setMessage("Open Youtube app to watch anime online ")
        dialogStart.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        dialogStart.setPositiveButton("OPEN YOUTUBE") { dialog, which ->
            try {
                watchYoutubeVideo(HackSign[position])
            } catch (e: java.lang.Exception) {
                watchYoutubeVideo("akRfmEK3yak")
            }
        }
        dialogStart.setCancelable(false)
        dialogStart.show()
    }

    fun watchYoutubeVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$id"))
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (list.isEmpty()) {
                if ((activity as Activity_Detail0401).name.toString().contains(".Hack//Sign")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_sign_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//Roots")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_roots_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//Quantum")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_quantum_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//Sign (Dub)")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_sign_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//Liminality")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_liminality_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//Roots (Dub)")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_roots_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//G.U. Trilogy")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_returner_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".Hack//G.U. Returner")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_returner_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains("Hacka Doll The Animation")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_animation_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//Legend of the Twilight")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_quantum_chap.txt", activity))
                } else if ((activity as Activity_Detail0401).name.toString().contains(".hack//The Movie: Sekai no Mukou ni")) {
                    loadDemo(GetFromAsset.getFromAssets("hack_roots_chap.txt", activity))
                } else {
                    loadChap((activity as Activity_Detail0401).domainChapter.toString() + (activity as Activity_Detail0401).idAnime.toString())
                }

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
                val lis = document.getElementsByTag("ul").get(0).getElementsByTag("li")
                for (i in lis) {
                    try {
                        val hrefchap = i.getElementsByTag("a").get(0).attr("href")
                        val nameChap = i.text()
                        list.add(Object_ItemChapter0501(nameChap, hrefchap))
                    } catch (e: Exception) {
                        Log.d("zzz", "onSuccess: " + e.message)
                    }
                }

                try {
                    progress_episodes?.visibility = View.INVISIBLE
                    adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.d("zzz", "onSuccess: " + e.message)
                }

            } catch (e: Exception) {
                Log.d("zzz", "onSuccess: ooooooooooo  " + e.message)
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