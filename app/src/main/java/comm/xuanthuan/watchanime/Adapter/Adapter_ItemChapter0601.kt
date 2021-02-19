package comm.xuanthuan.watchanime.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import comm.xuanthuan.watchanime.Object.Object_AnimeList3112
import comm.xuanthuan.watchanime.Object.Object_Genre0601
import comm.xuanthuan.watchanime.Object.Object_ItemChapter0501
import comm.xuanthuan.watchanime.R

class Adapter_ItemChapter0601 (val list:List<Object_ItemChapter0501>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    class ViewHolderItemChap(row : View){
        val txtnameChap: TextView = row.findViewById(R.id.txt_namechap_esopi)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        var viewHolderItemChap : ViewHolderItemChap
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.item_chapter0601, null)
            viewHolderItemChap = ViewHolderItemChap(view)
            view.tag = viewHolderItemChap
        } else {
            view = convertView
            viewHolderItemChap = convertView.tag as ViewHolderItemChap
        }

        var obItem: Object_ItemChapter0501 = list.get(position)
        viewHolderItemChap.txtnameChap.text = obItem.name

        return view as View
    }
}