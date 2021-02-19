package comm.xuanthuan.watchanime.Object;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import comm.xuanthuan.watchanime.R;


public class LoadMore3012 extends LoadMoreRecyclerView3012{

    public LoadMore3012(Context context) {
        this(context, null);
    }

    public LoadMore3012(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMore3012(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setLoadMorePresenter(new SimpleLoadMorePresenter(retryListener));
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            performLoadMore();
        }
    };


    static class SimpleLoadMorePresenter extends LoadMorePresenter {

        TextView message;
        View.OnClickListener onClickListener;

        public SimpleLoadMorePresenter(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_more, parent, false);
            message = (TextView) view.findViewById(R.id.message);
            view.setOnClickListener(onClickListener);
            return view;
        }

        @Override
        public void onLoading(View view) {
            view.setClickable(false);
            message.setText("loadmore");
        }

        @Override
        public void onError(View view, String msg) {
            view.setClickable(true);
            message.setText(msg);
        }
    }
}
