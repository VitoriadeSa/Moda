package pt.ubi.di.pdm.modadebolsa.ui;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.io.File;

public class ImagemUtil {

    /** Prefixo para imagens que vêm da pasta assets, ex: "asset:tecidos/algodao.jpg". */
    public static final String PREFIXO_ASSET = "asset:";

    public static void carregar(ImageView iv, String path) {
        carregar(iv, path, 0);
    }

    /**
     * @param placeholder drawable mostrado enquanto carrega, se o caminho for vazio
     *                    ou se a imagem não existir (0 = nenhum)
     */
    public static void carregar(ImageView iv, String path, @DrawableRes int placeholder) {
        if (path == null || path.trim().isEmpty()) {
            Glide.with(iv).clear(iv);
            if (placeholder != 0) {
                iv.setImageResource(placeholder);
            } else {
                iv.setImageDrawable(null);
            }
            return;
        }

        RequestBuilder<android.graphics.drawable.Drawable> pedido;

        if (path.startsWith(PREFIXO_ASSET)) {
            Uri uri = Uri.parse("file:///android_asset/"
                    + path.substring(PREFIXO_ASSET.length()));
            pedido = Glide.with(iv).load(uri);
        } else {
            File f = new File(path);
            if (!f.isAbsolute()) {
                f = new File(iv.getContext().getFilesDir(), path);
            }
            pedido = Glide.with(iv).load(f);
        }

        if (placeholder != 0) {
            pedido = pedido.placeholder(placeholder).error(placeholder);
        }
        pedido.into(iv);
    }
}
