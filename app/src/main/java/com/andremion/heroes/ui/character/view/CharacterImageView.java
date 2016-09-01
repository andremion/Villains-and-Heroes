package com.andremion.heroes.ui.character.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.andremion.heroes.R;

public class CharacterImageView extends ImageView {

    private final Path mPath = new Path();
    private final int mPaddingBottom;

    public CharacterImageView(Context context) {
        this(context, null, 0);
    }

    public CharacterImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharacterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaddingBottom = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
        mPath.lineTo(w, 0);
        if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
            mPath.lineTo(w, h - mPaddingBottom);
            mPath.lineTo(0, h);
        } else {
            mPath.lineTo(w, h);
            mPath.lineTo(0, h - mPaddingBottom);
        }
        mPath.lineTo(0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}
