package com.example.tikitrendingproject.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMargin extends RecyclerView.ItemDecoration {
    private final int marginTop;
    private final int marginLeft;
    private final int marginRight;
    private final int marginBottom;

    public ItemMargin(int marginTop, int marginLeft, int marginRight, int marginBottom) {
        this.marginTop = marginTop;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = marginTop;
        outRect.left = marginLeft;
        outRect.right = marginRight;
        outRect.bottom = marginBottom;
    }
}
