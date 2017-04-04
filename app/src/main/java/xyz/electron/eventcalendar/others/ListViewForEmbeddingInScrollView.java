package xyz.electron.eventcalendar.others;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

// https://stackoverflow.com/questions/3495890/how-can-i-put-a-listview-into-a-scrollview-without-it-collapsing/29708371#29708371

public class ListViewForEmbeddingInScrollView extends ListView {
    public ListViewForEmbeddingInScrollView(Context context) {
        super(context);
    }

    public ListViewForEmbeddingInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForEmbeddingInScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 1, MeasureSpec.AT_MOST));
    }
}