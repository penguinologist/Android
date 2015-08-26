package penguinologist.diyandroidchallenge;


/**
 * Created by Jeroen on 8/21/2015.
 */

/**
 * This class contains all the data shown inside of each element in the list on the projects page.
 * It contains an image, a title, and a description
 */
public class RowItem {
    /**
     * Class variables
     */
    private String imageId;
    private String title;
    private String description;
    private String projectID;
    private String projectOwner;
    private String currentUser;
    private String password;

    /**
     * The main constructor. It carries a lot of data between the activities.
     *
     * @param imageId      The ID of the image to be displayed.
     * @param title        The title of the project.
     * @param desc         The description of the project, mostly who made it.
     * @param projectID    The project ID.
     * @param projectOwner The owner of the project.
     * @param currentUser  The current user.
     * @param password     The password of the current user, used in authentication.
     * @param token        The token.
     */
    public RowItem(String imageId, String title, String desc, String projectID, String projectOwner, String currentUser, String password, String token) {
        this.imageId = imageId;
        this.title = title;
        this.description = desc;
        this.projectID = projectID;
        this.projectOwner = projectOwner;
        this.currentUser = currentUser;
    }

    /**
     * This method returns the Image ID
     *
     * @return String value of the Image ID
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * This method sets the Image ID
     *
     * @param imageId the image ID to be set
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * Retrieves the description of the project.
     *
     * @return String value of the description.
     */
    public String getDesc() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param desc Description to be set.
     */
    public void setDesc(String desc) {
        this.description = desc;
    }

    /**
     * Gets the title of the project.
     *
     * @return String value of the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the project.
     *
     * @param title String value of the title to be set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the projectID
     *
     * @return String value of the project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * sets the project ID
     *
     * @param proj String value to be set.
     */
    public void setProjectID(String proj) {
        projectID = proj;
    }

    /**
     * Retrieves the project owner's reference
     *
     * @return String value of the project owner's reference
     */
    public String getProjectOwner() {
        return projectOwner;
    }

    /**
     * Sets the project owner
     *
     * @param projectOwner Project owner to be set
     */
    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    /**
     * Returns a string containing the title and description of the project.
     *
     * @return String value of the RowItem
     */
    @Override
    public String toString() {
        return title + "\n" + description;
    }

    /**
     * Retrieves the current user.
     *
     * @return String value of the user.
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user (possibly different than the project owner).
     *
     * @param currentUser The user to be set as the current user.
     */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Retrieves the password for authentication
     *
     * @return String value of the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password Password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}