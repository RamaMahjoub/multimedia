package EditedClasses;

public class Image {
    private javafx.scene.image.Image image;
    private String url;

    public Image(String url) {
        this.url = url;
        this.image = new javafx.scene.image.Image(url);
    }

    public javafx.scene.image.Image getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public void setImage(javafx.scene.image.Image image) {
        this.image = image;
    }
}
