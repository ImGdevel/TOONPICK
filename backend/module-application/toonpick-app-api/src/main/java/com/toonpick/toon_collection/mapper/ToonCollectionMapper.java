package com.toonpick.toon_collection.mapper;

import com.toonpick.entity.ToonCollection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.toonpick.toon_collection.response.ToonCollectionResponseDTO;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ToonCollectionMapper {
    ToonCollectionMapper INSTANCE = Mappers.getMapper(ToonCollectionMapper.class);

    ToonCollectionResponseDTO toonCollectionToToonCollectionResponseDTO(ToonCollection toonCollection);

    List<ToonCollectionResponseDTO> toonCollectionsToToonCollectionResponseDTOs(List<ToonCollection> toonCollections);
}
