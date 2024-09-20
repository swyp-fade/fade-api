package com.fade.bon.scheduler;

import com.fade.bon.entity.Bon;
import com.fade.bon.entity.HotBon;
import com.fade.bon.repository.BonRepository;
import com.fade.bon.repository.HotBonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HotBonScheduler {
    private final BonRepository bonRepository;
    private final HotBonRepository hotBonRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void hotBonBatch() {
        this.hotBonRepository.deleteAllHotBon();

        LocalDateTime startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endOfYesterday = startOfYesterday.plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Bon> extractBons = this.bonRepository.findHotBons();

        List<Bon> sortedBons = extractBons.stream()
                .sorted(Comparator.comparingLong((Bon b) ->
                                b.getBonVotes().stream()
                                        .filter(vote -> vote.getCreatedAt().isAfter(startOfYesterday) && vote.getCreatedAt().isBefore(endOfYesterday))
                                        .count()
                        ).reversed()
                        .thenComparing(Bon::getCreatedAt, Comparator.reverseOrder()))
                .toList();

        List<HotBon> hotBons = new ArrayList<>();
        for (int i = 0; i < sortedBons.size(); i++) {
            Bon bon = sortedBons.get(i);
            hotBons.add(HotBon.builder()
                    .bon(bon)
                    .rank(sortedBons.size() - i)
                    .build());
        }
        this.hotBonRepository.saveAll(hotBons);
    }
}
