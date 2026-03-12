package se.omegapoint.bankservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.omegapoint.bankservice.dtos.ProfileResponseDTO;
import se.omegapoint.bankservice.models.Profile;

@Mapper(componentModel = "jsr330")
public interface ProfileMapper {

    @Mapping(target = "userId", expression = "java(profile.getRawUserId())")
    ProfileResponseDTO toResponseDto(Profile profile);
}
