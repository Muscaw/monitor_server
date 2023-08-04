package dev.muscaw.monitor.image.domain;

public class ImageSerializationException extends RuntimeException {
  public ImageSerializationException(String message, Exception cause) {
    super(message, cause);
  }
}
