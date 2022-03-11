package fr.sunderia.sunderialauncher.ui.panels.partials;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.sunderia.sunderialauncher.ui.panel.Panel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TopBar extends Panel {

    @Override
    public String getName() {
        return "TopBar";
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);
        GridPane topbarPane = this.layout;
        this.layout.setStyle("-fx-background-color: rgb(35, 40, 40);");
        setCanTakeAllWidth(topbarPane);

        /**
         * TopBar separator
         */
        //TopBar: Left side
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("images/icon.png"));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(25);
        setLeft(imageView);
        this.layout.getChildren().add(imageView);

        //TopBar: Center
        Label title = new Label("Sunderia");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 18f));
        title.setStyle("-fx-text-fill: white;");
        setCenterH(title);
        this.layout.getChildren().add(title);

        //TopBar: Right side
        GridPane topBarButton = new GridPane();
        topBarButton.setMinWidth(100d);
        topBarButton.setMaxWidth(100d);
        setCanTakeAllSize(topBarButton);
        setRight(topBarButton);
        this.layout.getChildren().add(topBarButton);

        //TopBar Button Configuration
        FontAwesomeIconView closeBtn = new FontAwesomeIconView(FontAwesomeIcon.TIMES);
        FontAwesomeIconView fullscreenBtn = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_UP);
        FontAwesomeIconView minimizeBtn = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MINIMIZE);
        setCanTakeAllWidth(closeBtn, fullscreenBtn, minimizeBtn);

        closeBtn.setFill(Color.RED);
        closeBtn.setOpacity(.7f);
        closeBtn.setSize("16px");
        closeBtn.setOnMouseEntered(event -> closeBtn.setOpacity(1.f));
        closeBtn.setOnMouseExited(event -> closeBtn.setOpacity(.7f));
        closeBtn.setOnMouseClicked(event -> System.exit(0));
        closeBtn.setTranslateX(70.0d);
        closeBtn.setTranslateY(4.0D);

        fullscreenBtn.setFill(Color.GREENYELLOW);
        fullscreenBtn.setOpacity(.7f);
        fullscreenBtn.setSize("14px");
        fullscreenBtn.setOnMouseEntered(event -> fullscreenBtn.setOpacity(1.f));
        fullscreenBtn.setOnMouseExited(event -> fullscreenBtn.setOpacity(.7f));
        fullscreenBtn.setOnMouseClicked(event -> this.panelManager.getStage().setMaximized(!this.panelManager.getStage().isMaximized()));
        fullscreenBtn.setTranslateX(45.0D);
        fullscreenBtn.setTranslateY(4.0D);

        minimizeBtn.setFill(Color.ROSYBROWN);
        minimizeBtn.setOpacity(.7f);
        minimizeBtn.setSize("14px");
        minimizeBtn.setOnMouseEntered(event -> minimizeBtn.setOpacity(1.f));
        minimizeBtn.setOnMouseExited(event -> minimizeBtn.setOpacity(.7f));
        minimizeBtn.setOnMouseClicked(event -> this.panelManager.getStage().setIconified(true));
        minimizeBtn.setTranslateX(20.0D);
        minimizeBtn.setTranslateY(3.0D);

        topBarButton.getChildren().addAll(closeBtn, fullscreenBtn, minimizeBtn);
    }
}
