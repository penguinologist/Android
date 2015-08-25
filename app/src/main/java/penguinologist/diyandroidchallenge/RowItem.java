package penguinologist.diyandroidchallenge;

import android.graphics.drawable.Drawable;

/**
 * Created by Jeroen on 8/21/2015.
 */

public class RowItem {
    private String imageId;
    private String title;
    private String description;

    public RowItem(String imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.description = desc;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + "\n" + description;
    }
}