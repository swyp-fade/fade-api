package com.fade.style.service;

import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.style.entity.Style;
import com.fade.style.repository.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StyleCommonService {
    private final StyleRepository styleRepository;

    public Style findById(Integer id) {
        return this.styleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_STYLE));
    }
}
