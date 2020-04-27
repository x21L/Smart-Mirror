module lukas.wais.smart.mirror {
    requires javafx.controls;
    requires javafx.fxml;

    opens lukas.wais.smart.mirror to javafx.fxml;
    exports lukas.wais.smart.mirror;
}