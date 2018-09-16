package org.ribcakes.beatScraper.details;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.ribcakes.beatScraper.details.model.DetailPage;
import org.ribcakes.beatScraper.details.model.SongDetail;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DetailIterator implements Iterator<SongDetail> {

    @NonNull
    private final DetailService detailService;

    private final LocalDateTime newestDate;

    private final Queue<SongDetail> detailCache = new ArrayDeque<>();
    private final List<SongDetail> nextPage = new ArrayList<>();

    private int offset = 0;

    public DetailIterator(@NonNull final DetailService detailService, @Nullable final LocalDateTime newestDate) {
        this.detailService = detailService;
        this.newestDate = newestDate;
        this.loadNextPage();
    }

    @Override
    public boolean hasNext() {
        boolean hasMoreOnCurrentPage = Objects.nonNull(this.detailCache.peek());
        boolean hasNextPage = CollectionUtils.isNotEmpty(this.nextPage);

        return hasMoreOnCurrentPage || hasNextPage;
    }

    @Override
    public SongDetail next() {
        if (Objects.isNull(this.detailCache.peek())) {
            this.loadNextPage();
        }
        if (Objects.isNull(this.detailCache.peek())) {
            throw new NoSuchElementException();
        }
        SongDetail nextDetail = this.detailCache.poll();

        return nextDetail;
    }

    @SneakyThrows
    private void loadNextPage() {
        this.nextPage.forEach(this.detailCache::offer);
        this.nextPage.clear();

        List<SongDetail> details = this.detailService.getPage(this.offset)
                                                     .map(DetailPage::getSongs)
                                                     .orElse(Collections.emptyList());
        if (CollectionUtils.isEmpty(details)) {
            return;
        }

        List<SongDetail> filteredDetails
                = details.stream()
                         .filter(detail -> {
                             LocalDateTime createdAt = detail.getCreatedAt().getDate();
                             Boolean shouldDownload = Optional.ofNullable(this.newestDate)
                                                              .map(newest -> newest.isBefore(createdAt))
                                                              .orElse(Boolean.TRUE);
                             return shouldDownload;
                         }).collect(Collectors.toList());
        this.offset += filteredDetails.size();

        if (details.size() != filteredDetails.size()) {
            filteredDetails.forEach(this.detailCache::offer);
        } else {
            this.nextPage.addAll(filteredDetails);
        }

        Thread.sleep(1000);
    }
}
