package comm.xuanthuan.watchanime.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import comm.xuanthuan.watchanime.Object.Object_AnimeList3112
import comm.xuanthuan.watchanime.R

class Adapter_itemAnimeList3112(var list: ArrayList<Object_AnimeList3112>, var context: FragmentActivity?, var layout: Int) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolderAnimeList (row : View){
        var name : TextView = row.findViewById(R.id.item_txt_animelist)

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View?
        var viewHolderAnimeList: ViewHolderAnimeList
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.item_animelist3112, null)
            viewHolderAnimeList = ViewHolderAnimeList(view)
            view.tag = viewHolderAnimeList
        } else {
            view = convertView
            viewHolderAnimeList = convertView.tag as ViewHolderAnimeList
        }

        var obItem: Object_AnimeList3112 = list!!.get(position)
        viewHolderAnimeList.name.text = obItem.name

        return view as View

    }

}