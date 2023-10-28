package dev.muscaw.monitor.app.domain;

public record Pager(PageNumber currentPage, int totalNumberOfPages) {
  public PageNumber nextPage() {
    return new PageNumber((currentPage.page() + 1) % totalNumberOfPages());
  }

  public PageNumber previousPage() {
    return new PageNumber((currentPage().page() - 1) % totalNumberOfPages());
  }
}
