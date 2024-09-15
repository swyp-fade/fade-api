package com.fade.bon.service;

import com.fade.bon.entity.Bon;
import com.fade.bon.repository.BonRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BonCommonService {
    private final BonRepository bonRepository;

    public Bon findById(Long id) {
        return this.bonRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_BON));
    }
}
