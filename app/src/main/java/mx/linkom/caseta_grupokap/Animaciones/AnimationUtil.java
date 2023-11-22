package mx.linkom.caseta_grupokap.Animaciones;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import mx.linkom.caseta_grupokap.R;

public class AnimationUtil {
    public static void startAnimation(Context context, ConstraintLayout layout) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
        layout.startAnimation(animation);
    }
}