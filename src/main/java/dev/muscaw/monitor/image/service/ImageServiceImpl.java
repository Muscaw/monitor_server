package dev.muscaw.monitor.image.service;

import dev.muscaw.monitor.image.domain.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

  @Override
  public int getNextPage(int currentPage) {
    return currentPage + 1;
  }
}
