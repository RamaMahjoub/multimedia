
package interfaces;

import javafx.scene.Parent;

import java.io.IOException;

public interface Page {
    Parent getView() throws IOException;
}
