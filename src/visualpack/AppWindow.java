package visualpack;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppWindow {

    private static final Image APP_ICON = new Image(AppWindow.class.getResourceAsStream("/visuals/top.png"));

    public static Stage createWindow(String title) 
    {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(APP_ICON);

        return stage;
    }
}
