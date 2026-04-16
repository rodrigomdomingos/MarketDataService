package com.marketdata.application.ports.out;

import java.time.OffsetDateTime;

public interface MarketDataUpdatedPortOut {
    void publishAfterCommit(Long stockId, OffsetDateTime snapshotAt);
}
