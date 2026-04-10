package site.meowcat.jscrobbler;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import site.meowcat.jscrobbler.service.AuthService;

public class Main extends Application {

    private Label statusLabel;
    private Label trackLabel;
    private Label artistLabel;

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(160);
        sidebar.setStyle("-fx-background-color: #0f0f0f;");

        Label logo = new Label("JScrobbler");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button nowPlayingBtn = createSidebarButton("Now Playing");
        Button profileBtn = createSidebarButton("Profile");
        Button settingsBtn = createSidebarButton("Settings");
        Button connectBtn = createSidebarButton("Connect to Last.fm");
        Button confirmBtn = createSidebarButton("I've authorized");

        confirmBtn.setDisable(true);

        sidebar.getChildren().addAll(logo, nowPlayingBtn, profileBtn, settingsBtn, connectBtn, confirmBtn);


        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #181818;");

        VBox nowPlayingView = createNowPlayingView();
        VBox profileView = new VBox(new Label("Profile page (coming soon)"));
        VBox settingsView = new VBox(new Label("Settings page (coming soon)"));

        contentArea.getChildren().addAll(nowPlayingView, profileView, settingsView);

        // Hide all except default
        profileView.setVisible(false);
        settingsView.setVisible(false);

        nowPlayingBtn.setOnAction(e -> {
            nowPlayingView.setVisible(true);
            profileView.setVisible(false);
            settingsView.setVisible(false);
        });

        profileBtn.setOnAction(e -> {
            nowPlayingView.setVisible(false);
            settingsView.setVisible(false);
            profileView.setVisible(true);
        });

        settingsBtn.setOnAction(e -> {
            nowPlayingView.setVisible(false);
            settingsView.setVisible(true);
            profileView.setVisible(false);
        });

        connectBtn.setOnAction(e -> {
            try {
                AuthService.openAuthPage();
                confirmBtn.setDisable(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        confirmBtn.setOnAction(e -> {
            try {
                String sessionKey = AuthService.finishAuth();
                System.out.println("Session Key: " + sessionKey);
                confirmBtn.setDisable(true);
                connectBtn.setText("Connected");
                connectBtn.setDisable(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to authorize: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("JScrobbler");
        stage.setScene(scene);
        stage.show();
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 14px;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white;"));

        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc;"));

        return btn;
    }

    private VBox createNowPlayingView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        Label title = new Label("Now Playing");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        Label track = new Label("Track: -");
        Label artist = new Label("Artist: -");
        track.setStyle("-fx-text-fill: #dddddddd;");
        artist.setStyle("-fx-text-fill: #dddddddd;");

        box.getChildren().addAll(title, track, artist);
        return box;
    }

    private void connectToLastFm() {
        statusLabel.setText("Status: Connected ✅");
        trackLabel.setText("Track: Test Track");
        artistLabel.setText("Artist: Test Artist");
    }

    public static void main(String[] args) {
        launch();
    }
}