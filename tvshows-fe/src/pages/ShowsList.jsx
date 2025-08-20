import { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { api } from "../api";

export default function ShowsList() {
  const [shows, setShows] = useState([]);
  const [page, setPage] = useState(0); // zero-based for backend
  const [size] = useState(24);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    api
      .get(`/api/shows/paged?page=${page}&size=${size}&sort=name,asc`)
      .then((res) => {
        if (cancelled) return;
        // Support Spring Page JSON or plain array
        const data = res.data;
        const content = Array.isArray(data) ? data : data?.content || [];
        setShows(content);
        if (Array.isArray(data)) {
          // Try to infer total pages from headers if available; otherwise default to single page
          const totalCountHeader =
            res.headers?.["x-total-count"] || res.headers?.["X-Total-Count"]; // varies
          if (totalCountHeader) {
            const total = parseInt(totalCountHeader, 10) || content.length;
            setTotalPages(Math.max(1, Math.ceil(total / size)));
          } else {
            setTotalPages(1);
          }
        } else {
          const tp = Number.isFinite(data?.totalPages) ? data.totalPages : 1;
          setTotalPages(Math.max(1, tp));
        }
      })
      .catch(() => {
        if (!cancelled) {
          setShows([]);
          setTotalPages(1);
        }
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [page, size]);

  // Build a compact pager like tvmazev2 (1..10, next/prev)
  const pages = useMemo(() => {
    const maxButtons = 10;
    const curr = page + 1; // to 1-based
    const last = totalPages;
    let start = Math.max(1, curr - 4);
    let end = Math.min(last, start + maxButtons - 1);
    start = Math.max(1, end - maxButtons + 1);
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }, [page, totalPages]);

  return (
    <div className="mx-auto max-w-7xl px-4 py-6">
      <h1 className="mb-6 text-4xl font-semibold tracking-tight">Shows</h1>

      {/* Grid */}
      <div className="grid grid-cols-[repeat(auto-fill,minmax(210px,1fr))] gap-x-6 gap-y-7">
        {shows.map((s) => {
          const poster = s?.image?.medium || s?.imageMedium || s?.imageOriginal || s?.image?.original;
          const rating = s?.rating?.average ?? s?.rating;
          const name = s?.name || "Untitled";
          return (
            <Link to={`/shows/${s.id}`} key={s.id} className="group block">
              <div className="overflow-hidden rounded shadow-sm">
                {poster ? (
                  <img
                    src={poster}
                    alt={name}
                    className="aspect-[210/295] w-full object-cover transition-transform duration-200 group-hover:scale-[1.02]"
                    loading="lazy"
                    decoding="async"
                  />
                ) : (
                  <div className="aspect-[210/295] w-full bg-slate-100" />
                )}
                {/* Name bar */}
                <div className="bg-teal-700 px-3 py-2 text-sm font-medium text-white line-clamp-2 min-h-[2.5rem]">
                  {name}
                </div>
                {/* Love + rating bar */}
                <div className="flex items-center justify-between bg-teal-600/90 px-3 py-2 text-white">
                  <span className="inline-flex items-center text-sm opacity-90">
                    <HeartIcon className="mr-1 h-4 w-4" />
                    <span>Fav</span>
                  </span>
                  {typeof rating === "number" && (
                    <span className="inline-flex items-center rounded bg-white/15 px-2 py-0.5 text-xs font-semibold">
                      {rating.toFixed(1)}
                    </span>
                  )}
                </div>
              </div>
            </Link>
          );
        })}
      </div>

      {/* Empty state */}
      {!loading && shows.length === 0 && (
        <div className="mt-8 text-sm text-slate-500">No shows to display.</div>
      )}

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="mt-8 flex items-center justify-center gap-2">
          <button
            onClick={() => setPage((p) => Math.max(0, p - 1))}
            disabled={page === 0}
            className="rounded border border-slate-300 px-2 py-1 text-sm disabled:opacity-40"
          >
            ‹
          </button>
          {pages.map((p) => (
            <button
              key={p}
              onClick={() => setPage(p - 1)}
              className={`h-8 w-8 rounded-full text-sm ${p - 1 === page ? "bg-blue-600 text-white" : "border border-slate-300 text-slate-700"}`}
            >
              {p}
            </button>
          ))}
          <button
            onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
            disabled={page >= totalPages - 1}
            className="rounded border border-slate-300 px-2 py-1 text-sm disabled:opacity-40"
          >
            ›
          </button>
        </div>
      )}
    </div>
  );
}

function HeartIcon({ className = "h-4 w-4" }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="currentColor"
      className={className}
      aria-hidden="true"
    >
      <path d="M11.645 20.91l-.007-.003-.022-.012a15.247 15.247 0 01-1.944-1.17 25.18 25.18 0 01-3.71-3.01C3.26 14.37 1.5 12.347 1.5 9.75A4.5 4.5 0 016 5.25c1.294 0 2.475.524 3.322 1.372L12 9.3l2.678-2.678A4.687 4.687 0 0118 5.25a4.5 4.5 0 014.5 4.5c0 2.598-1.76 4.621-4.462 6.965a25.18 25.18 0 01-3.71 3.01 15.247 15.247 0 01-1.944 1.17l-.022.012-.007.003a.75.75 0 01-.644 0z" />
    </svg>
  );
}
