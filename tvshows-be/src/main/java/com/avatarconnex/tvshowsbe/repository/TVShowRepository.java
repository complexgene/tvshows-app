package com.avatarconnex.tvshowsbe.repository;

import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TVShowRepository extends JpaRepository<TVShowDetails, Long> { }
