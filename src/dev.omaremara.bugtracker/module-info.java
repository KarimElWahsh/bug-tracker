module dev.omaremara.bugtracker {
  requires javafx.base;
  requires javafx.controls;
  requires javafx.graphics;
  requires javax.mail;
  requires javax.activation;
  exports dev.omaremara.bugtracker to javafx.graphics;
  requires java.sql;
}

