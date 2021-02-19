package comm.xuanthuan.watchanime.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import comm.xuanthuan.watchanime.Object.Object_Download1101
import comm.xuanthuan.watchanime.R
import kotlinx.android.synthetic.main.item_lv_download1101.view.*

class Adapter_DownLoad1101(val list: List<Object_Download1101>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
       return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolderDownLoad(row: View) {
        val lnDownLoad : LinearLayout
        val typeDown : TextView
        init {
            lnDownLoad = row.findViewById(R.id.download)
            typeDown = row.findViewById(R.id.txt_typeDownload)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        var viewholder: Adapter_DownLoad1101.ViewHolderDownLoad
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.item_lv_download1101, null)
            viewholder = Adapter_DownLoad1101.ViewHolderDownLoad(view)
            view.tag = viewholder
        } else {
            view = convertView
            viewholder = convertView.tag as ViewHolderDownLoad
        }

        var obItem: Object_Download1101 = list.get(position)
        viewholder.typeDown.text = obItem.type
        viewholder.lnDownLoad.setOnClickListener(View.OnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(obItem.href)
            context.startActivity(i)
        })


        return view as View
    }
}