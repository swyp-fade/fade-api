package com.fade.bon.repository;

import com.fade.bon.dto.request.FindBonRequest;
import com.fade.bon.entity.HotBon;
import com.fade.global.constant.SearchType;

import java.util.List;

public interface CustomHotBonRepository {
    List<HotBon> findHotBons(Long memberId, FindBonRequest findBonRequest);
    HotBon findNextCursor(Long memberId, Long lastCursor, SearchType searchType);
}
