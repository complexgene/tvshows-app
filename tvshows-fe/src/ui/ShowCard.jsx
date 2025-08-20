import { Card, CardActionArea, CardContent, CardMedia, Chip, Stack, Typography } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";

export default function ShowCard({ show }) {
    const img = show.imageMedium || show.imageOriginal;

    return (
        <Card sx={{ borderRadius: 2, overflow: "hidden", height: "100%" }}>
            <CardActionArea component={RouterLink} to={`/shows/${show.id}`} sx={{ height: "100%" }}>
                {img && (
                    <CardMedia
                        component="img"
                        image={img}
                        alt={show.name}
                        sx={{ height: 300, objectFit: "cover" }}
                        loading="lazy"
                    />
                )}
                <CardContent sx={{ pb: 1.5 }}>
                    <Typography variant="subtitle1" gutterBottom noWrap title={show.name}>
                        {show.name}
                    </Typography>
                    <Stack direction="row" spacing={1} useFlexGap flexWrap="wrap">
                        {show.network && <Chip size="small" label={show.network} />}
                        {typeof show.rating === "number" && <Chip size="small" label={`★ ${show.rating}`} />}
                    </Stack>
                </CardContent>
            </CardActionArea>
        </Card>
    );
}
