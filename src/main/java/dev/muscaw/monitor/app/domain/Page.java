package dev.muscaw.monitor.app.domain;

import dev.muscaw.monitor.image.domain.Renderable;

public record Page(int currentPage, int nextPage, Renderable renderable) {}
