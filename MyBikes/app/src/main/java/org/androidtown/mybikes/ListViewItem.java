package org.androidtown.mybikes;

import android.graphics.drawable.Drawable;

/**
 * Created by HyeonSeo on 2016-10-19.
 */

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
}
