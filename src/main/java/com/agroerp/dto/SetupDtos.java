package com.agroerp.dto;

public final class SetupDtos {
    private SetupDtos() {}

    public record SubCategoryResponse(Long id, String name, Long categoryId, String categoryName, boolean active) {}
    public record SubCategoryRequest(String name, Long categoryId, boolean active) {}
}
