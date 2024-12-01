package org.src.etl.model.yit;

import java.util.Map;

public record BuildingEntity(String Id, String Type, double Score, Map<String, Object> Fields) {
}
