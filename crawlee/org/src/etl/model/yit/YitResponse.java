package org.src.etl.model.yit;

import java.util.List;

public record YitResponse(int TotalHits, boolean IsMoreAvailable, List<BuildingEntity> Hits) {
}
