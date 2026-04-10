module site.meowcat.jscrobbler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;

    opens site.meowcat.jscrobbler to javafx.fxml;
    exports site.meowcat.jscrobbler;
}