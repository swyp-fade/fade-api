package com.fade.category.service;

import com.fade.category.entity.Category;
import com.fade.category.repository.CategoryRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryCommonService {
    private final CategoryRepository categoryRepository;

    public Category findById(Integer id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY));
    }
}
