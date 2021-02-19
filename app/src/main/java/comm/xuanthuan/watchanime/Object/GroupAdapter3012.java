package comm.xuanthuan.watchanime.Object;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GroupAdapter3012 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    @NonNull
    final RecyclerView.Adapter[] adapters;
    final int adapterCount;
    @NonNull
    final int[] endPositions;
    // view type
    @NonNull
    final Map<Integer, Integer>[] viewTypeToGenerateViewTypeMaps; // [(viewType, generate viewType),(viewType, generate viewType)]
    @NonNull
    final Map<Integer, ViewTypeInfo> generateViewTypeToViewTypeInfoMap; // (generate viewType , ViewTypeInfo(adapter position, viewType))
    @NonNull
    final Map<Long, Long>[] itemIdToGenerateItemIdMaps;  // [(itemId, generate itemId),(itemId, generate itemId)]


    boolean dataInvalid = true;
    int resolvedAdapterIndex;
    int resolvedItemIndex;

    public GroupAdapter3012(@NonNull GroupAdapter3012.Builder builder) {
        int count = builder.adapters.size();
        if (count < 0) {
            throw new IllegalArgumentException("Must add at least one adapter");
        }
        this.adapters = builder.adapters.toArray(new RecyclerView.Adapter[builder.adapters.size()]);
        this.adapterCount = count;
        this.endPositions = new int[count];
        this.viewTypeToGenerateViewTypeMaps = new HashMap[count];
        this.itemIdToGenerateItemIdMaps = new HashMap[count];
        this.generateViewTypeToViewTypeInfoMap = new HashMap<>();
        this.dataInvalid = true;

        for (int i = 0; i < count; i++) {
            adapters[i].registerAdapterDataObserver(new MyDataObserver(i));
            viewTypeToGenerateViewTypeMaps[i] = new HashMap<>();
            itemIdToGenerateItemIdMaps[i] = new HashMap<>();
        }
    }

    public static class Builder {
        @NonNull
        List<RecyclerView.Adapter> adapters = new ArrayList<>();

        public void add(@NonNull RecyclerView.Adapter adapter) {
            if (adapter == null) {
                throw new NullPointerException();
            }
            adapters.add(adapter);
        }

        public GroupAdapter3012 build() {
            return new GroupAdapter3012(this);
        }
    }


    @Override
    public int getItemCount() {
        if (dataInvalid) {
            int lastEndPosition = 0;
            for (int i = 0; i < adapterCount; i++) {
                lastEndPosition += adapters[i].getItemCount();
                endPositions[i] = lastEndPosition;
            }
            dataInvalid = false;
        }
        return endPositions[adapterCount - 1];
    }

    @Override
    public int getItemViewType(int position) {
        resolveIndices(position);
        int resolvedAdapterIndex = this.resolvedAdapterIndex;
        int resolvedItemIndex = this.resolvedItemIndex;

        Map<Integer, Integer> viewTypeMap = viewTypeToGenerateViewTypeMaps[resolvedAdapterIndex];
        int viewType = adapters[resolvedAdapterIndex].getItemViewType(resolvedItemIndex);

        if (viewTypeMap.containsKey(viewType)) {
            return viewTypeMap.get(viewType);
        } else {
            int generateViewType = generateViewType();
            viewTypeMap.put(viewType, generateViewType);
            generateViewTypeToViewTypeInfoMap.put(generateViewType, new ViewTypeInfo(resolvedAdapterIndex, viewType));
            return generateViewType;
        }
    }

    @Override
    public long getItemId(int position) {
        resolveIndices(position);
        int resolvedAdapterIndex = this.resolvedAdapterIndex;
        int resolvedItemIndex = this.resolvedItemIndex;

        Map<Long, Long> itemIdMap = itemIdToGenerateItemIdMaps[resolvedAdapterIndex];
        long id = adapters[resolvedAdapterIndex].getItemId(resolvedItemIndex);
        if (id == RecyclerView.NO_ID) {
            return id;
        } else {
            if (itemIdMap.containsKey(id)) {
                return itemIdMap.get(id);
            } else {
                long generateItemId = generateItemId();
                itemIdMap.put(id, generateItemId);
                return generateItemId;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewTypeInfo viewTypeInfo = generateViewTypeToViewTypeInfoMap.get(viewType);
        int resolvedAdapterIndex = viewTypeInfo.adapterPosition;
        int resolvedViewType = viewTypeInfo.viewType;
        return adapters[resolvedAdapterIndex].onCreateViewHolder(parent, resolvedViewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        resolveIndices(position);
        int resolvedAdapterIndex = this.resolvedAdapterIndex;
        int resolvedItemIndex = this.resolvedItemIndex;
        adapters[resolvedAdapterIndex].onBindViewHolder(holder, resolvedItemIndex);
    }

    public int getChildAdapterStartPosition(RecyclerView.Adapter adapter) {
        for (int i = 0; i < adapters.length; i++) {
            if (adapters[i] == adapter) {
                return i == 0 ? 0 : endPositions[i - 1];
            }
        }
        throw new IllegalArgumentException("adapter is not added");
    }

    private int getChildAdapterStartPosition(int adapterIndex) {
        return adapterIndex == 0 ? 0 : endPositions[adapterIndex - 1];
    }


    /**
     * Converts the given overall adapter {@code position} into {@link #resolvedAdapterIndex}
     * and {@link #resolvedItemIndex}.
     */
    private void resolveIndices(int position) {
        int itemCount = getItemCount(); // This conveniently rebuilds endPositions if necessary.
        if (position < 0 || position >= itemCount) {
            throw new IndexOutOfBoundsException(
                    "Asked for position " + position + " while count is " + itemCount);
        }

        int arrayIndex = Arrays.binarySearch(endPositions, position);
        if (arrayIndex >= 0) {
            // position is the end position of repositories[arrayIndex], so it falls in the range
            // of the next repository that advances past it (there may be some empty repositories).
            do {
                arrayIndex++;
            }
            while (endPositions[arrayIndex] == position); // will not OOB after the initial bound check.
        } else {
            // position is before the end position of repositories[~arrayIndex], so it falls in the
            // range of the repository at ~arrayIndex.
            arrayIndex = ~arrayIndex;
        }

        resolvedAdapterIndex = arrayIndex;
        resolvedItemIndex = arrayIndex == 0 ? position : position - endPositions[arrayIndex - 1];
    }


    /**
     * for child adapter
     */
    private class MyDataObserver extends RecyclerView.AdapterDataObserver {

        final int adapterPosition;

        public MyDataObserver(int adapterPosition) {
            this.adapterPosition = adapterPosition;
        }

        @Override
        public void onChanged() {
            GroupAdapter3012.this.dataInvalid = true;
            GroupAdapter3012.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            GroupAdapter3012.this.dataInvalid = true;
            positionStart = positionStart + GroupAdapter3012.this.getChildAdapterStartPosition(adapterPosition);
            GroupAdapter3012.this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            GroupAdapter3012.this.dataInvalid = true;
            positionStart = positionStart + GroupAdapter3012.this.getChildAdapterStartPosition(adapterPosition);
            GroupAdapter3012.this.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            GroupAdapter3012.this.dataInvalid = true;
            positionStart = positionStart + GroupAdapter3012.this.getChildAdapterStartPosition(adapterPosition);
            GroupAdapter3012.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            GroupAdapter3012.this.dataInvalid = true;
            positionStart = positionStart + GroupAdapter3012.this.getChildAdapterStartPosition(adapterPosition);
            GroupAdapter3012.this.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            GroupAdapter3012.this.dataInvalid = true;
            int adapterStartPosition = GroupAdapter3012.this.getChildAdapterStartPosition(adapterPosition);
            fromPosition = fromPosition + adapterStartPosition;
            toPosition = toPosition + adapterStartPosition;
            GroupAdapter3012.this.notifyItemMoved(fromPosition, toPosition);
        }
    }

    static class ViewTypeInfo {
        final int adapterPosition;
        final int viewType;

        public ViewTypeInfo(int adapterPosition, int viewType) {
            this.adapterPosition = adapterPosition;
            this.viewType = viewType;
        }
    }

    private static final AtomicInteger sNextGeneratedType = new AtomicInteger(1);
    private static final AtomicLong sNextGeneratedId = new AtomicLong(1);

    public static int generateViewType() {
        for (; ; ) {
            final int result = sNextGeneratedType.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > Integer.MAX_VALUE) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedType.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static long generateItemId() {
        for (; ; ) {
            final long result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            long newValue = result + 1;
            if (newValue > Long.MAX_VALUE) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
