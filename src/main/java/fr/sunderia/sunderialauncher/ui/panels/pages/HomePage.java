package fr.sunderia.sunderialauncher.ui.panels.pages;

import fr.sunderia.sunderialauncher.SunderiaLauncher;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.sunderia.sunderialauncher.ui.panel.Panel;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class HomePage extends Panel {

    private final GridPane sideMenu = new GridPane();
    private final GridPane navContent = new GridPane();
    private final Saver saver = SunderiaLauncher.getInstance().getSaver();

    private Node activeLink;
    private Button homeBtn, settingsBtn;

    @Override
    public String getName() {
        return "Home";
    }

    @Override
    public String getStylesheetPath() {
        return "css/home.css";
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

    }
}
