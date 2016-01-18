package eu.kanade.tachiyomi.ui.recent;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.kanade.tachiyomi.R;
import eu.kanade.tachiyomi.data.database.models.MangaChapter;

public class RecentChaptersAdapter extends FlexibleAdapter<RecyclerView.ViewHolder, Object> {

    private RecentChaptersFragment fragment;

    private static final int CHAPTER = 0;
    private static final int SECTION = 1;

    public RecentChaptersAdapter(RecentChaptersFragment fragment) {
        this.fragment = fragment;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        Object item = getItem(position);
        if (item instanceof MangaChapter)
            return ((MangaChapter) item).chapter.id;
        else
            return item.hashCode();
    }

    public void setItems(List<Object> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public void updateDataSet(String param) {

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof MangaChapter ? CHAPTER : SECTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case CHAPTER:
                v = inflater.inflate(R.layout.item_recent_chapter, parent, false);
                return new RecentChaptersHolder(v, this, fragment);
            case SECTION:
                v = inflater.inflate(R.layout.item_recent_chapter_section, parent, false);
                return new SectionViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CHAPTER:
                final MangaChapter chapter = (MangaChapter) getItem(position);
                ((RecentChaptersHolder) holder).onSetValues(chapter);
                break;
            case SECTION:
                final Date date = (Date) getItem(position);
                ((SectionViewHolder) holder).onSetValues(date);
        }

        //When user scrolls this bind the correct selection status
        holder.itemView.setActivated(isSelected(position));
    }

    public RecentChaptersFragment getFragment() {
        return fragment;
    }

    private static class SectionViewHolder extends RecyclerView.ViewHolder {

        private TextView view;

        public SectionViewHolder(View view) {
            super(view);
            this.view = (TextView) view;
        }

        public void onSetValues(Date date) {
            String s = DateFormat.getDateFormat(view.getContext()).format(date);
            view.setText(s);
        }
    }
}