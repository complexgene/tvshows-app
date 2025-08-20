import { useEffect, useState } from "react";
import { useParams, Link as RouterLink } from "react-router-dom";
import { api } from "../api";

export default function ShowDetails() {
  const { id } = useParams();
  const [show, setShow] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    api
      .get(`/api/shows/${id}`)
      .then((res) => {
        if (!cancelled) setShow(res.data);
      })
      .finally(() => !cancelled && setLoading(false));
    return () => {
      cancelled = true;
    };
  }, [id]);

  if (loading) {
    return (
      <div className="mx-auto max-w-6xl px-4">
        <div className="grid grid-cols-12 gap-8">
          <div className="col-span-12 md:col-span-8">
            <div className="h-96 w-full animate-pulse rounded border border-slate-200 bg-slate-100" />
          </div>
          <div className="col-span-12 md:col-span-4">
            <div className="h-96 w-full animate-pulse rounded border border-slate-200 bg-slate-100" />
          </div>
        </div>
      </div>
    );
  }

  if (!show) return <div className="mx-auto max-w-6xl px-4 py-8">Not found</div>;

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

  // Normalize potential API shapes (e.g., tvmazev2-like objects)
  const posterSrc =
    imageOriginal || imageMedium || show?.image?.original || show?.image?.medium;

  const networkName = typeof network === "string" ? network : network?.name;
  const networkHref =
    networkUrl ||
    (typeof network === "object" ? network?.officialSite || network?.url : null);

  const scheduleDaysText = Array.isArray(scheduleDays)
    ? scheduleDays.join(", ")
    : show?.schedule?.days
    ? show.schedule.days.join(", ")
    : scheduleDays;
  const scheduleTimeText = scheduleTime || show?.schedule?.time || "";

  const ratingValue =
    typeof rating === "number"
      ? rating
      : rating && typeof rating.average === "number"
      ? rating.average
      : null;

  const displayGenres = Array.isArray(genres)
    ? genres
    : typeof genres === "string"
    ? genres
        .split(",")
        .map((g) => g.trim())
        .filter(Boolean)
    : Array.isArray(show?.genres)
    ? show.genres
    : null;

  return (
    <div className="mx-auto max-w-6xl px-4 pb-10">
      {/* Title */}
      <h1 className="mb-4 mt-2 text-4xl font-semibold">{name}</h1>

      <div className="grid grid-cols-12 gap-8">
        {/* MAIN */}
        <section className="col-span-12 md:col-span-8">
          <div className="rounded border border-slate-200 bg-white p-4">
            <div className="flex flex-col gap-4 sm:flex-row">
              {posterSrc && (
                <div className="shrink-0">
                  <img
                    src={posterSrc}
                    alt={name}
                    className="w-[180px] sm:w-[220px] md:w-[280px] rounded shadow"
                    loading="lazy"
                    decoding="async"
                  />
                </div>
              )}

              <div className="prose prose-sm max-w-none [&amp;_p]:mb-3 [&amp;_p]:leading-6 [&amp;_em]:text-slate-500">
                <div
                  dangerouslySetInnerHTML={{
                    __html:
                      summaryHtml ||
                      show?.summary ||
                      "<em>No summary available.</em>",
                  }}
                />
              </div>
            </div>
          </div>
        </section>

        {/* SIDEBAR */}
        <aside className="col-span-12 md:col-span-4">
          <h2 className="mb-2 text-lg font-semibold">Show Info</h2>
          <div className="rounded border border-slate-200 bg-white">
            <div className="divide-y divide-slate-200 p-4">
              <InfoRow
                label="Network"
                value={
                  networkHref ? (
                    <a
                      href={networkHref}
                      target="_blank"
                      rel="noreferrer"
                      className="text-blue-600 hover:underline"
                    >
                      {networkName || "—"}
                    </a>
                  ) : (
                    networkName || "—"
                  )
                }
              />
              <InfoRow
                label="Schedule"
                value={formatSchedule(
                  scheduleDaysText,
                  scheduleTimeText,
                  runtime
                )}
              />
              <InfoRow label="Status" value={status || "—"} />
              <InfoRow label="Show Type" value={showType || "—"} />
              <InfoRow
                label="Genres"
                value={
                  Array.isArray(displayGenres) ? (
                    <div className="flex flex-wrap gap-2">
                      {displayGenres.map((g) => (
                        <span
                          key={g}
                          className="rounded bg-slate-100 px-2 py-0.5 text-xs"
                        >
                          {g}
                        </span>
                      ))}
                    </div>
                  ) : (
                    displayGenres || "—"
                  )
                }
              />
              <InfoRow label="Premiered" value={premiered || "—"} />
              {officialSite && (
                <InfoRow
                  label="Official site"
                  value={
                    <a
                      href={officialSite}
                      target="_blank"
                      rel="noreferrer"
                      className="break-all text-blue-600 hover:underline"
                    >
                      {officialSite}
                    </a>
                  }
                />
              )}

              {typeof ratingValue === "number" && (
                <div className="pt-3">
                  <div className="mb-1 text-[11px] uppercase tracking-wide text-slate-500">
                    Rating
                  </div>
                  <div className="mb-3 flex items-center gap-2">
                    {/* 5-star visual based on /10 rating */}
                    <Stars outOfTen={ratingValue} />
                    <div className="text-sm text-slate-600">
                      {ratingValue.toFixed(1)} / 10
                      {typeof ratingVotes === "number"
                        ? ` (${ratingVotes} votes)`
                        : ""}
                    </div>
                  </div>
                  <hr className="my-2 border-slate-200" />
                </div>
              )}

              <div className="flex items-center justify-between pt-2">
                <RouterLink
                  to="/"
                  className="text-sm text-slate-700 hover:underline"
                >
                  &larr; Back
                </RouterLink>
                {officialSite && (
                  <a
                    href={officialSite}
                    target="_blank"
                    rel="noreferrer"
                    className="rounded bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700"
                  >
                    Official Site
                  </a>
                )}
              </div>
            </div>
          </div>
        </aside>
      </div>
    </div>
  );
}

function InfoRow({ label, value }) {
  if (!value) return null;
  return (
    <div className="py-2 first:pt-0 last:pb-0">
      <div className="mb-1 text-[11px] uppercase tracking-wide text-slate-500">
        {label}
      </div>
      <div className="text-sm text-slate-800">{value}</div>
    </div>
  );
}

function formatSchedule(days, time, runtime) {
  const left = days || "";
  const right = [time, runtime ? `${runtime} min` : ""]
    .filter(Boolean)
    .join(" ");
  if (!left && !right) return "";
  return [left, right].filter(Boolean).join(" at ");
}

// Simple star renderer for /10 scale
function Stars({ outOfTen = 0 }) {
  const outOfFive = Math.max(0, Math.min(5, outOfTen / 2));
  const full = Math.floor(outOfFive);
  const half = outOfFive - full >= 0.5;
  const empty = 5 - full - (half ? 1 : 0);
  return (
    <div className="flex items-center gap-0.5 text-amber-500">
      {Array.from({ length: full }).map((_, i) => (
        <svg key={`f${i}`} className="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.803 2.037a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118L10 13.348l-2.984 2.126c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L3.38 8.72c-.783-.57-.38-1.81.588-1.81H7.43a1 1 0 00.95-.69l1.07-3.292z" />
        </svg>
      ))}
      {half && (
        <svg className="h-4 w-4" viewBox="0 0 24 24">
          <defs>
            <linearGradient id="half">
              <stop offset="50%" stopColor="currentColor" />
              <stop offset="50%" stopColor="transparent" />
            </linearGradient>
          </defs>
          <path fill="url(#half)" d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
          <path fill="currentColor" fillOpacity="0.25" d="M12 2l2.81 6.63 7.19.61-5.46 4.73L18.18 21 12 17.27 5.82 21l1.64-7.03L2 9.24l7.19-.61z"/>
        </svg>
      )}
      {Array.from({ length: empty }).map((_, i) => (
        <svg key={`e${i}`} className="h-4 w-4 text-slate-300" viewBox="0 0 20 20" fill="currentColor">
          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.803 2.037a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118L10 13.348l-2.984 2.126c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L3.38 8.72c-.783-.57-.38-1.81.588-1.81H7.43a1 1 0 00.95-.69l1.07-3.292z" />
        </svg>
      ))}
    </div>
  );
}
