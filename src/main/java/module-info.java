module site.meowcat.jscrobbler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.net.http;
    requires com.google.gson;

    opens site.meowcat.jscrobbler to javafx.fxml, com.google.gson;
    exports site.meowcat.jscrobbler;
}