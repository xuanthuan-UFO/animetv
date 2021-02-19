package comm.xuanthuan.watchanime.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import comm.xuanthuan.watchanime.Object.Object_AnimeList3112
import comm.xuanthuan.watchanime.Object.Object_Genre0601
import comm.xuanthuan.watchanime.R

class Adapter_Genre_Home0601 (val list:List<String>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    class ViewHolderGenreHome(row : View){
        val txtGenre : TextView = row.findViewById(R.id.txt_genre_home)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        var viewHolderGenreHome : ViewHolderGenreHome
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.item_genre_home, null)
            viewHolderGenreHome = ViewHolderGenreHome(view)
            view.tag = viewHolderGenreHome
        } else {
            view = convertView
            viewHolderGenreHome = convertView.tag as ViewHolderGenreHome
        }
        viewHolderGenreHome.txtGenre.text = list.get(position)

        return view as View
    }
}