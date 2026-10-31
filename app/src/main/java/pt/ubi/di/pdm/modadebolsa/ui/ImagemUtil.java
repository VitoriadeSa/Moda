package pt.ubi.di.pdm.modadebolsa.ui;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImagemUtil {

    public static void carregar(ImageView iv, String path) {
        if (path == null || path.trim().isEmpty()) {
            Glide.with(iv).clear(iv);
            iv.setImageDrawable(null);
            return;
        }

        File f = new File(path);
        if (!f.isAbsolute()) {
            f = new File(iv.getContext().getFilesDir(), path);
        }

        Glide.with(iv).load(f).into(iv);
    }
}