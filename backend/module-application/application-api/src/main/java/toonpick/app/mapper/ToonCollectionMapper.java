package toonpick.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.domain.toon_collection.ToonCollection;
import toonpick.app.dto.ToonCollectionResponseDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToonCollectionMapper {
    ToonCollectionMapper INSTANCE = Mappers.getMapper(ToonCollectionMapper.class);

    ToonCollectionResponseDTO toonCollectionToToonCollectionResponseDTO(ToonCollection toonCollection);

    List<ToonCollectionResponseDTO> toonCollectionsToToonCollectionResponseDTOs(List<ToonCollection> toonCollections);
}
