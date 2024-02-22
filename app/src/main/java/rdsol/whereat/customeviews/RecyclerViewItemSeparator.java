package rdsol.whereat.customeviews;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemSeparator extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public RecyclerViewItemSeparator(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        boolean isLast = position == state.getItemCount()-1;
        outRect.bottom = verticalSpaceHeight;
        if(isLast){
            outRect.bottom = verticalSpaceHeight + 160;
            outRect.top = 0; //don't forget about recycling...
        }


    }

}
