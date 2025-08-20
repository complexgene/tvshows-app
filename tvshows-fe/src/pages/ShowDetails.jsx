import { useEffect, useState } from "react";
import { useParams, Link as RouterLink } from "react-router-dom";
import { api } from "../api"; // axios instance: api.get(`/api/shows/${id}`)
import {
    Box, Grid, Card, CardMedia, CardContent, Typography,
    Stack, Divider, Link as MLink, Chip, Skeleton, Rating, Button
} from "@mui/material";

export default function ShowDetails() {
    const { id } = useParams();
    const [show, setShow] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get(`/api/shows/${id}`)
            .then(res => setShow(res.data))
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) {
        return (
            <Grid container spacing={3}>
                <Grid item xs={12} md={8}><Skeleton variant="rounded" height={380} /></Grid>
                <Grid item xs={12} md={4}><Skeleton variant="rounded" height={380} /></Grid>
            </Grid>
        );
    }

    if (!show) return <Typography>Not found</Typography>;

    const {
        name,
        summaryHtml,
        imageOriginal,
        imageMedium,
        network,       // e.g., "CBS"
        networkUrl,    // optional link to network site/page
        status,        // "Running", "Ended", ...
        showType,      // "Scripted"
        genres,        // "Drama, Action, Crime" (string) or array
        runtime,       // minutes
        premiered,     // yyyy-mm-dd
        scheduleDays,  // "Tuesday" or "Tue, Thu"
        scheduleTime,  // "20:00"
        rating,        // 0..10
        ratingVotes,   // optional
        officialSite
    } = show;

    const posterSrc = imageOriginal || imageMedium;

    return (
        <Grid container spacing={3}>
            {/* MAIN */}
            <Grid item xs={12} md={8}>
                <Typography variant="h3" sx={{ mb: 2 }}>{name}</Typography>
                <Card sx={{ p: 2 }}>
                    <Grid container spacing={2}>
                        {posterSrc && (
                            <Grid item xs={12} sm="auto">
                                <CardMedia
                                    component="img"
                                    image={posterSrc}
                                    alt={name}
                                    sx={{ width: 240, borderRadius: 1 }}
                                />
                            </Grid>
                        )}
                        <Grid item xs>
                            <CardContent sx={{ pt: 0 }}>
                                <Box
                                    sx={{
                                        "& p": { mb: 1.2, lineHeight: 1.6 },
                                        "& em": { color: "text.secondary" }
                                    }}
                                    dangerouslySetInnerHTML={{
                                        __html: summaryHtml || "<em>No summary available.</em>"
                                    }}
                                />
                            </CardContent>
                        </Grid>
                    </Grid>
                </Card>
            </Grid>

            {/* SIDEBAR */}
            <Grid item xs={12} md={4}>
                <Typography variant="h6" sx={{ mb: 1 }}>Show Info</Typography>
                <Card>
                    <CardContent>
                        <InfoRow label="Network" value={
                            networkUrl
                                ? <MLink href={networkUrl} target="_blank" rel="noreferrer">{network}</MLink>
                                : network
                        }/>
                        <InfoRow label="Schedule" value={formatSchedule(scheduleDays, scheduleTime, runtime)} />
                        <InfoRow label="Status" value={status} />
                        <InfoRow label="Show Type" value={showType} />
                        <InfoRow label="Genres" value={
                            Array.isArray(genres)
                                ? <Stack direction="row" spacing={1} useFlexGap flexWrap="wrap">
                                    {genres.map(g => <Chip key={g} size="small" label={g} />)}
                                </Stack>
                                : genres
                        } />
                        <InfoRow label="Premiered" value={premiered} />
                        {officialSite && (
                            <InfoRow label="Official site" value={
                                <MLink href={officialSite} target="_blank" rel="noreferrer">{officialSite}</MLink>
                            }/>
                        )}

                        {/* Rating */}
                        {typeof rating === "number" && (
                            <>
                                <Typography variant="subtitle2" sx={{ mt: 1 }}>Rating</Typography>
                                <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 1.5 }}>
                                    <Rating value={rating / 2} precision={0.5} readOnly />
                                    <Typography variant="body2" color="text.secondary">
                                        {rating.toFixed(1)} / 10{typeof ratingVotes === "number" ? ` (${ratingVotes} votes)` : ""}
                                    </Typography>
                                </Stack>
                                <Divider sx={{ my: 1 }} />
                            </>
                        )}

                        <Stack direction="row" justifyContent="space-between" sx={{ mt: 1 }}>
                            <Button component={RouterLink} to="/" size="small">&larr; Back</Button>
                            {officialSite && (
                                <Button size="small" variant="contained" href={officialSite} target="_blank" rel="noreferrer">
                                    Official Site
                                </Button>
                            )}
                        </Stack>
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    );
}

function InfoRow({ label, value }) {
    if (!value) return null;
    return (
        <>
            <Typography variant="subtitle2" sx={{ textTransform: "uppercase", letterSpacing: ".04em", color: "text.secondary" }}>
                {label}
            </Typography>
            <Box sx={{ mb: 1.5, "&:last-child": { mb: 0 } }}>
                {typeof value === "string" ? <Typography variant="body2">{value}</Typography> : value}
            </Box>
            <Divider sx={{ mb: 1.5 }} />
        </>
    );
}

function formatSchedule(days, time, runtime) {
    const left = days || "";
    const right = [time, runtime ? `${runtime} min` : ""].filter(Boolean).join(" ");
    if (!left && !right) return "";
    return [left, right].filter(Boolean).join(" at ");
}
