package com.avatarconnex.tvshowsbe.controller;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import com.avatarconnex.tvshowsbe.models.ShowListItemDto;
import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import com.avatarconnex.tvshowsbe.service.TVShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/shows")
public class TVShowController {

    @Autowired
    private TVShowService tvShowService;

    @GetMapping
    public List<TVShowDetails> getAllTvShows() {
        return tvShowService.fetchAllTvShows();
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ShowListItemDto>> getAllTvShowsPaged(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ShowListItemDto> tvShows = tvShowService.fetchAllTVShows(pageable);
        return ResponseEntity.ok(tvShows);
    }

    @PostMapping("/import")
    public ResponseEntity<AppResponse> importTvShows() {
        AppResponse appResponse = tvShowService.importShowDataFromTitles();
        return ResponseEntity.ok(appResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TVShowDetails> getAllTvShowsPaged(@PathVariable("id") Long showId) {
        TVShowDetails tvShowDetails = tvShowService.fetchShowDetail(showId);
        return ResponseEntity.ok(tvShowDetails);
    }
}
