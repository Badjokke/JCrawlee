package org.src.etl.mapper;

import org.src.etl.model.yit.BuildingEntity;

import java.util.HashMap;
import java.util.Map;

public class ProjectionMapper {

    public static BuildingEntity map(BuildingEntity entity, Map<String, String> projection) {
        Map<String, Object> transformedFields = entity.Fields().entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(projection.getOrDefault(entry.getKey(), entry.getKey()), entry.getValue()), HashMap::putAll);
        return new BuildingEntity(entity.Id(), entity.Type(), entity.Score(), transformedFields);
    }

    public static BuildingEntity mapStrictProjection(BuildingEntity entity, Map<String, String> projection) {
        Map<String, Object> entityFields = entity.Fields();
        Map<String, Object> transformedFields = projection.entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(projection.get(entry.getKey()), entityFields.get(entry.getKey())), HashMap::putAll);
        return new BuildingEntity(entity.Id(), entity.Type(), entity.Score(), transformedFields);
    }
}
