package comm.xuanthuan.watchanime.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import comm.xuanthuan.watchanime.Activity.Activity_Detail0401
import comm.xuanthuan.watchanime.Activity.Activity_Home3012
import comm.xuanthuan.watchanime.Activity.Activity_Search0801
import comm.xuanthuan.watchanime.Object.Object_Anime3012
import comm.xuanthuan.watchanime.R

class Adapter_itemHome3012(var list: List<Object_Anime3012>, var context: FragmentActivity?) :
        RecyclerView.Adapter<Adapter_itemHome3012.ViewHolderItemHome>() {
    private var lastPosition = -1
    val a = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemHome {
        val view =
                LayoutInflater.from(context).inflate(R.layout.item_home3012, parent, false)
        return ViewHolderItemHome(view)
    }

    override fun onBindViewHolder(holder: ViewHolderItemHome, position: Int) {
        setAnimation(holder.itemView, position)
        var objectHome = list[position]
        holder.name.text = objectHome.name
        holder.release.text = objectHome.release
        try {
            if ((context as Activity_Home3012).intcheck != 3) {
                Glide.with(context).load(objectHome.img).into(holder.img)
            } else {
                holder.img.visibility = View.GONE
            }
        } catch (e: Exception) {
        }

        try {
            if ((context as Activity_Search0801).intcheck != 3) {
                Glide.with(context).load(objectHome.img).into(holder.img)
            }
        } catch (e: Exception) {

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolderItemHome(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var name: TextView
        var release: TextView

        init {
            img = itemView.findViewById(R.id.item_img_home)
            name = itemView.findViewById(R.id.item_name_home)
            release = itemView.findViewById(R.id.item_released)

            itemView.setOnClickListener {
                val intent = Intent(context, Activity_Detail0401::class.java)
                intent.putExtra("data", list.get(adapterPosition))
                context?.startActivity(intent)
            }
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}