package comm.xuanthuan.watchanime.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import comm.xuanthuan.watchanime.Activity.Activity_Detail0401
import comm.xuanthuan.watchanime.R
import kotlinx.android.synthetic.main.fragment__summary0501.*
import kotlinx.android.synthetic.main.fragment__summary0501.view.*

class Fragment_Summary0501 : Fragment() {

    var txtDetailSummary: TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__summary0501, container, false)
        txtDetailSummary = view.findViewById(R.id.txtDetailSummary)
        try {
            txtDetailSummary?.text = (activity as Activity_Detail0401).summary
        } catch (e: Exception) {

        }
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            try {
                txtDetailSummary?.text = (activity as Activity_Detail0401).summary
            } catch (e: Exception) {
            }
        }
    }
}