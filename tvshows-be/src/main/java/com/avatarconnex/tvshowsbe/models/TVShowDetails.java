package com.avatarconnex.tvshowsbe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tvshow_details")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TVShowDetails {

    /** TVMaze show id */
    @Id
    private Long id;

    private String url;

    /** show name */
    @Column(name = "show_name")      // avoid collisions with embedded names
    private String name;

    private String type;
    private String language;

    @ElementCollection
    @CollectionTable(name = "tvshow_genres", joinColumns = @JoinColumn(name = "tvshow_id"))
    @Column(name = "genre")
    private List<String> genres;

    private String status;
    private Integer runtime;

    @Column(name = "avg_runtime")
    private Integer averageRuntime;

    private String premiered;
    private String ended;
    private String officialSite;

    /* ===== Embeddeds with column overrides ===== */

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "time", column = @Column(name = "schedule_time"))
    })
    private Schedule schedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "average", column = @Column(name = "rating_average"))
    })
    private Rating rating;

    private Integer weight;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id",           column = @Column(name = "network_id")),
            @AttributeOverride(name = "name",         column = @Column(name = "network_name")),
            @AttributeOverride(name = "officialSite", column = @Column(name = "network_official_site"))
    })
    private Network network;

    /** Optional — may be null */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id",           column = @Column(name = "webchannel_id")),
            @AttributeOverride(name = "name",         column = @Column(name = "webchannel_name")),
            @AttributeOverride(name = "officialSite", column = @Column(name = "webchannel_official_site"))
    })
    private WebChannel webChannel;

    /** Optional — may be null */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name",     column = @Column(name = "country_name")),
            @AttributeOverride(name = "code",     column = @Column(name = "country_code")),
            @AttributeOverride(name = "timezone", column = @Column(name = "country_timezone"))
    })
    private Country country;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tvrage",  column = @Column(name = "ext_tvrage")),
            @AttributeOverride(name = "thetvdb", column = @Column(name = "ext_thetvdb")),
            @AttributeOverride(name = "imdb",    column = @Column(name = "ext_imdb"))
    })
    private Externals externals;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "medium",   column = @Column(name = "image_medium")),
            @AttributeOverride(name = "original", column = @Column(name = "image_original"))
    })
    private Image image;

    @Column(length = 8000)
    private String summary;

    /** Unix epoch seconds from API */
    private Long updated;

    @JsonProperty("_links")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "self.href",               column = @Column(name = "self_href")),
            @AttributeOverride(name = "previousepisode.href",    column = @Column(name = "previousepisode_href")),
            @AttributeOverride(name = "previousepisode.name",    column = @Column(name = "previousepisode_name"))
    })
    private Links links;

    /* ===== Nested types ===== */

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule {
        private String time;

        @Column(name = "days")
        private List<String> days;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        private Double average;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        private Long id;
        private String name;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "name",     column = @Column(name = "network_country_name")),
                @AttributeOverride(name = "code",     column = @Column(name = "network_country_code")),
                @AttributeOverride(name = "timezone", column = @Column(name = "network_country_timezone"))
        })
        private Country country;

        private String officialSite;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebChannel {
        private Long id;
        private String name;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "name",     column = @Column(name = "webchannel_country_name")),
                @AttributeOverride(name = "code",     column = @Column(name = "webchannel_country_code")),
                @AttributeOverride(name = "timezone", column = @Column(name = "webchannel_country_timezone"))
        })
        private Country country;

        private String officialSite;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Country {
        private String name;
        private String code;
        private String timezone;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Externals {
        private Integer tvrage;
        private Integer thetvdb;
        private String imdb;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String medium;
        private String original;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        @Embedded
        private Link self;

        @Embedded
        private PreviousEpisode previousepisode;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Link {
        private String href;
    }

    @Embeddable @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PreviousEpisode {
        private String href;
        private String name;
    }
}
