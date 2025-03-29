package toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import toonpick.dto.ToonCollectionResponseDTO;
import toonpick.entity.ToonCollection;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ToonCollectionMapper {
    ToonCollectionMapper INSTANCE = Mappers.getMapper(ToonCollectionMapper.class);

    ToonCollectionResponseDTO toonCollectionToToonCollectionResponseDTO(ToonCollection toonCollection);

    List<ToonCollectionResponseDTO> toonCollectionsToToonCollectionResponseDTOs(List<ToonCollection> toonCollections);
}
