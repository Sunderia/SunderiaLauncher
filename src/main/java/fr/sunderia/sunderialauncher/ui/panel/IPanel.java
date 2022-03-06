package fr.sunderia.sunderialauncher.ui.panel;

import fr.sunderia.sunderialauncher.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
    void init(PanelManager manager);
    GridPane getLayout();
    void onShow();
    String getName();
    String getStylesheetPath();
}
