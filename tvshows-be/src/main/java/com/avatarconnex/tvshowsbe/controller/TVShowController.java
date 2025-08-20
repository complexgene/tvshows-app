package com.avatarconnex.tvshowsbe.controller;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import com.avatarconnex.tvshowsbe.models.ShowListItemDto;
import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import com.avatarconnex.tvshowsbe.service.TVShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
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

    @GetMapping("/all")
    @Operation(summary = "[Non-Paginated] List all TV Shows", description = "Fetches non-paginated list of TV shows")
    public List<TVShowDetails> getAllTvShows() {
        return tvShowService.fetchAllTvShows();
    }

    @GetMapping("/paged")
    @Operation(summary = "[Paginated] List all TV Shows", description = "Paginated list of TV shows available in the system")
    public ResponseEntity<Page<ShowListItemDto>> getAllTvShowsPaged(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ShowListItemDto> tvShows = tvShowService.fetchAllTVShows(pageable);
        return ResponseEntity.ok(tvShows);
    }

    @PostMapping("/import")
    @Operation(summary = "Import details of TV Shows", description = "Reads the given file and fetches the TV show details from external API")
    public ResponseEntity<AppResponse> importTvShows() {
        AppResponse appResponse = tvShowService.importShowDataFromTitles();
        return ResponseEntity.ok(appResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get TV Show by ID", description = "Fetch details of a TV show using its ID")
    public ResponseEntity<TVShowDetails> getTvShowById(@PathVariable("id") Long showId) {
        TVShowDetails tvShowDetails = tvShowService.fetchShowDetail(showId);
        return ResponseEntity.ok(tvShowDetails);
    }
}
