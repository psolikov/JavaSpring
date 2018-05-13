package ru.spbau.solikov.ftp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A user interface of frp-client that allows to traverse and download files from directories.
 * To go to the parent folder use "/.." button. When you want to download some file from current folder, hit
 * "download" button and after that select a file or hit "download" button again to continue browsing.
 */
public class UIFTP extends Application {

    private FTPClient client;
    private Path current;
    private BorderPane root;
    private Button download;
    private VBox pathsBox = new VBox();
    private Map<Button, FTPFile> map = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        client = new FTPClient(Integer.parseInt(parameters.get(0)), parameters.get(1));
        primaryStage.setTitle("FTP Client");
        pathsBox.setSpacing(5);
        root = new BorderPane();
        download = new Button("Download");
        download.setAlignment(Pos.CENTER);
        download.setOnAction(event -> setButtonsDownload(getButtons()));
        root.setTop(download);
        BorderPane.setAlignment(download, Pos.CENTER);
        Scene scene = new Scene(root, 500, 500);
        current = Paths.get(System.getProperty("user.dir"));
        setScene(current);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setButtonsBrowse(Collection<Button> buttons) {
        download.setStyle("");
        download.setOnAction(event -> setButtonsDownload(getButtons()));
        for (Button button : buttons) {
            if (map.get(button).isDirectory()) {
                button.setStyle("-fx-text-fill: #0064cc;");
                button.setOnAction(event -> {
                    reBuild(map.get(button).getName());
                });
                continue;
            }
            button.setStyle("");
            button.setOnAction(event -> {
            });
        }
    }

    private void setButtonsDownload(Collection<Button> buttons) {
        download.setStyle("-fx-background-color: rgba(43,46,53,0.19)");
        download.setOnAction(event -> setButtonsBrowse(getButtons()));

        for (Button button : buttons) {
            if (map.get(button).isDirectory()) {
                button.setOnAction(event -> {
                });
                button.setStyle("-fx-background-color: rgba(43,46,53,0.19)");
                continue;
            }
            button.setOnAction(event -> {
                try {
                    client.get(current + "/" + map.get(button).getName());
                } catch (IOException e) {
                    showAlert("Sorry, can't download the file : " + e.getMessage());
                }
            });
        }
    }

    private void setScene(Path path) {
        List<Button> collect = new ArrayList<>();
        Button upFolder = new Button("/..");
        upFolder.setOnAction(event -> {
            stepBack();
            reBuild("");
        });
        collect.add(upFolder);
        try {
            collect.addAll(client.list(path.toString()).stream().map(f ->
            {
                Button b = new Button(f.getName());
                if (f.isDirectory()) b.setStyle("-fx-text-fill: #0064cc;");
                map.put(b, f);
                return b;
            })
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            showAlert("Sorry, can't get a list of files : " + e.getMessage());
        }
        pathsBox.getChildren().addAll(collect);
        setButtonsBrowse(getButtons());
        ScrollPane scrollPane = new ScrollPane(pathsBox);
        scrollPane.setPrefSize(500, 500);
        root.setCenter(scrollPane);
    }

    private void stepBack() {
        if (current.getParent() != null) current = current.getParent();
    }

    private Collection<Button> getButtons() {
        return map.keySet();
    }

    private void reBuild(String name) {
        pathsBox.getChildren().clear();
        map.clear();
        current = Paths.get(current + "/" + name);
        setScene(current);
    }

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * To start the UI run that method.
     *
     * @param args host name and port number
     */
    public static void main(String[] args) {
        launch(args);
    }
}
