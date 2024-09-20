package com.fade.bon.repository;

import com.fade.bon.dto.request.FindBonRequest;
import com.fade.bon.entity.Bon;
import com.fade.global.constant.SearchType;

import java.util.List;

public interface CustomBonRepository {
    List<Bon> findBons(Long memberId, FindBonRequest findBonRequest);
    List<Bon> findHotBons();
    Bon findNextCursor(Long memberId, Long lastCursor, SearchType searchType);

}
