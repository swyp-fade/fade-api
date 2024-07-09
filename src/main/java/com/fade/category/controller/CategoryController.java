package com.fade.category.controller;

import com.fade.category.dto.response.FindCategoryListResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categories")
public class CategoryController {
    @GetMapping("")
    @SecurityRequirement(name = "access-token")
    public FindCategoryListResponse findCategories() {
        return null;
    }
}
