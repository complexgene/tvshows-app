package com.avatarconnex.tvshowsbe.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowListItemDto {
  private Long id;
  private String name;
  private String imageMedium;
  private String imageOriginal;
  private Double rating;      // 0..10
  private String network;     // e.g. "CBS"
}
