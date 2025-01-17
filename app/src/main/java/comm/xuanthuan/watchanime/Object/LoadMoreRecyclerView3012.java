package comm.xuanthuan.watchanime.Object;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreRecyclerView3012 extends RecyclerView {

    private Adapter contentAdapter;
    private FooterAdapter footerAdapter;
    private LoadMorePresenter loadMorePresenter;
    private boolean isLoading = false;
    private boolean isLoadError = false;
    private boolean isFooterInLayout = false;
    private boolean isLoadMoreEnable = true;
    private boolean isAttachedToWindow = false;
    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreRecyclerView3012(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView3012(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView3012(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(onScrollListener);
    }


    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isReadyForLoadMore()) {
                performLoadMore();
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    @Override
    public void setAdapter(@NonNull Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter is null!");
        }
        if (contentAdapter != null) {
            throw new IllegalArgumentException("can't change adapter!");
        }
        contentAdapter = adapter;
        footerAdapter = new FooterAdapter();
        GroupAdapter3012.Builder builder = new GroupAdapter3012.Builder();
        builder.add(contentAdapter);
        builder.add(footerAdapter);
        GroupAdapter3012 groupAdapter = builder.build();
        groupAdapter.registerAdapterDataObserver(new MyDataObserver());
        super.setAdapter(groupAdapter);
        notifyFooterViewStateChanged();
    }

    /**
     * use getContentAdapter instead
     *
     * @return
     */
    @Deprecated
    @Override
    public Adapter getAdapter() {
        return super.getAdapter();
    }

    public Adapter getContentAdapter() {
        return contentAdapter;
    }


    public void setLoadMoreEnable(boolean enable) {
        isLoadMoreEnable = enable;
        if (isLoadMoreEnable == false) {
            isLoadError = false;
            isLoading = false;
        }
        notifyFooterViewStateChanged();
    }

    public void setLoadMorePresenter(@NonNull LoadMorePresenter loadMorePresenter) {
        if (loadMorePresenter == null) {
            throw new IllegalArgumentException("loadMorePresenter is null!");
        }
        if (this.loadMorePresenter != null) {
            throw new IllegalArgumentException("can't change loadMorePresenter!");
        }
        this.loadMorePresenter = loadMorePresenter;
        notifyFooterViewStateChanged();
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void finishLoading() {
        this.isLoading = false;
        if (isReadyForLoadMore()) {
            performLoadMore();
        }
    }

    public void finishLoadingWithError(int resId) {
        this.finishLoadingWithError(getResources().getString(resId));
    }

    public void finishLoadingWithError(String msg) {
        isLoadError = true;
        isLoading = false;
        if (loadMorePresenter != null) {
            loadMorePresenter.onErrorInternal(msg);
        }
    }

    public void performLoadMore() {
        isLoadError = false;
        isLoading = true;
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
        if (loadMorePresenter != null) {
            loadMorePresenter.onLoadingInternal();
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    private void notifyFooterViewStateChanged() {
        if (isLoadMoreEnable == true) {
            if (isFooterInLayout == false) {
                if (footerAdapter != null && loadMorePresenter != null) {
                    footerAdapter.setLoadMorePresenter(loadMorePresenter);
                    getAdapter().notifyDataSetChanged();
//					footerAdapter.notifyItemInserted(0);
                    isFooterInLayout = true;
                }
            }
        } else {
            if (isFooterInLayout == true) {
                if (footerAdapter != null && loadMorePresenter != null) {
                    footerAdapter.setLoadMorePresenter(null);
                    footerAdapter.notifyItemRemoved(0);
                    isFooterInLayout = false;
                }
            }
        }
    }

    protected boolean isReadyForLoadMore() {
        if (getAdapter() == null ||
                isLoading || !isLoadMoreEnable || isLoadError || !isAttachedToWindow) {
            return false;
        } else {
            int lastVisiblePosition = getChildAdapterPosition(getChildAt(getChildCount() - 1)); // Because we have footer, so getChildCount() > 0.
            if (lastVisiblePosition == getAdapter().getItemCount() - 1) {
                return getChildAt(getChildCount() - 1).getTop() < getHeight();
            } else {
                return false;
            }
        }
    }


    /**
     * for child adapter
     */
    private class MyDataObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            if (isReadyForLoadMore()) {
                performLoadMore();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (isReadyForLoadMore()) {
                performLoadMore();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (isReadyForLoadMore()) {
                performLoadMore();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            // do nothing
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (isReadyForLoadMore()) {
                performLoadMore();
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing
        }
    }

    static class FooterAdapter extends Adapter<ViewHolder> {

        private LoadMorePresenter loadMorePresenter;

        public FooterAdapter() {
            this(null);
        }

        public FooterAdapter(@NonNull LoadMorePresenter loadMorePresenter) {
            this.loadMorePresenter = loadMorePresenter;
        }

        public void setLoadMorePresenter(LoadMorePresenter loadMorePresenter) {
            this.loadMorePresenter = loadMorePresenter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StaticViewHolder(loadMorePresenter.onCreateViewInternal(parent));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // do nothing
        }

        @Override
        public int getItemCount() {
            return loadMorePresenter == null ? 0 : 1;
        }


        static class StaticViewHolder extends ViewHolder {
            public StaticViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    public static abstract class LoadMorePresenter {

        private View view;

        View onCreateViewInternal(ViewGroup parent) {
            if (view == null) {
                view = onCreateView(parent);
                // When where is no data, we hide the view.
                view.setVisibility(View.GONE);
            }
            return view;
        }

        void onLoadingInternal() {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
            onLoading(view);
        }

        void onErrorInternal(String msg) {
            onError(view, msg);
        }

        public abstract View onCreateView(ViewGroup parent);

        public abstract void onLoading(View view);

        public abstract void onError(View view, String msg);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
