package dev.omaremara.bugtracker.model;

public enum Status {
  OPENED("OPENED"),
  CLOSED("CLOSED");
  private String name;
  private Status(String name) { this.name = name; }

  @Override
  public String toString() { return this.name; }
}