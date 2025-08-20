package com.avatarconnex.tvshowsbe.mapper;

import com.avatarconnex.tvshowsbe.models.ShowListItemDto;
import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import org.springframework.stereotype.Service;

@Service
public class TVShowMapper {
    // This class can be used to map between different representations of TV shows,
    // such as DTOs and entity models. Currently, it is empty but can be expanded
    // as needed for mapping logic.

    public ShowListItemDto toListDto(TVShowDetails s) {
        var dto = new ShowListItemDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        if (s.getImage()!=null){ dto.setImageMedium(s.getImage().getMedium()); dto.setImageOriginal(s.getImage().getOriginal()); }
        if (s.getRating()!=null){ dto.setRating(s.getRating().getAverage()); }
        if (s.getNetwork()!=null){ dto.setNetwork(s.getNetwork().getName()); }
        return dto;
    }
}
