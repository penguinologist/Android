package penguinologist.diyandroidchallenge;

import android.graphics.drawable.Drawable;

/**
 * Created by Jeroen on 8/21/2015.
 */

public class RowItem {
    private String imageId;
    private String title;
    private String description;
    private String projectID;
    private String projectOwner;

    public RowItem(String imageId, String title, String desc, String projectID, String projectOwner) {
        this.imageId = imageId;
        this.title = title;
        this.description = desc;
        this.projectID = projectID;
        this.projectOwner = projectOwner;
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

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String proj) {
        projectID = proj;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    @Override
    public String toString() {
        return title + "\n" + description;
    }
}