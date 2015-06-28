package com.threebrothers.scorestrip;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by mapinguari on 12/05/15.
 */
public class ERGO_View extends View {

    double PM3_RATIO = 0.98;
    double X_OVER_Y_RATIO = PM3_RATIO;
    double SQRT5 = 2.2360679775;
    double GRATIO = (SQRT5 - 1) / 5;

    public ERGO_View(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        int a;
        //CONSIDER WHAT HAPPENS IF WIDTH > HEIGHT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(width < height)
            a = width;
        else
            a = height;

        double Sx = width*GRATIO;
        double Sy = width*PM3_RATIO*GRATIO;

        double rt = (0.5 * (height - Sy));
        double rb = 0.5 * (height + Sy);
        double rl = 0.5 * (width - Sx);
        double rr = 0.5 * (width + Sx);

        // Here create bitmap and canvas to draw!
        //how to implement on draw!




    }
}
