package com.avatarconnex.tvshowsbe.models;

import lombok.Data;

// ShowDetailsDto.java
@Data
public class ShowDetailsDto {
  public Long id;
  public Integer tvmazev2Id;
  public String name;

  public String summaryHtml;
  public String language;
  public String status;
  public String showType;        // NEW (maps from tvmazev2 "type")
  public String network;         // e.g., "CBS"
  public String networkUrl;      // NEW (optional: link to network page if you store it)
  public String scheduleTime;    // "20:00"
  public String scheduleDays;    // "Tuesday"
  public String genres;          // "Drama, Action"
  public Integer runtime;        // minutes
  public String premiered;       // yyyy-mm-dd

  public Double rating;          // average 0..10
  public Integer ratingVotes;    // NEW (tvmazev2 sometimes exposes votes in other endpoints; optional)

  public String officialSite;

  public String imageMedium;
  public String imageOriginal;
}
