package rdsol.whereat.customeviews;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHorizontalItemSeparator  extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public RecyclerViewHorizontalItemSeparator(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets( Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        boolean isLast = position == state.getItemCount()-1;
        outRect.right = verticalSpaceHeight;
        if(isLast){
            outRect.right = verticalSpaceHeight + 160;
            outRect.left = 0; //don't forget about recycling...
        }


    }

}
