import { Link } from "react-router-dom";

export default function ShowTile({ show }) {
    const img = show.imageMedium || show.imageOriginal;

    return (
        <Link to={`/shows/${show.id}`} className="block h-full">
            <div className="h-full overflow-hidden rounded border border-slate-200 bg-white shadow-sm hover:shadow transition">
                {/* Poster: fixed height, cover center so all cards align */}
                {img ? (
                    <img
                        src={img}
                        alt={show.name}
                        className="w-full h-[295px] object-cover object-center bg-slate-100"
                        loading="lazy"
                    />
                ) : (
                    <div className="w-full h-[295px] bg-slate-100" />
                )}

                {/* Title */}
                <div className="px-3 py-3">
                    <div className="font-semibold truncate" title={show.name}>
                        {show.name}
                    </div>
                </div>

                {/* Teal footer (heart + rating) */}
                <div className="flex items-center justify-between bg-[#5ca49a] text-white px-3 py-2">
                    <div className="flex items-center gap-1">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none"
                             stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.6l-1-1a5.5 5.5 0 1 0-7.8 7.8l1 1L12 21l7.8-7.6 1-1a5.5 5.5 0 0 0 0-7.8z"/>
                        </svg>
                    </div>
                    <div className="rounded bg-white/25 px-2 py-0.5 text-sm font-bold min-w-9 text-center">
                        {typeof show.rating === "number" ? show.rating.toFixed(1) : "—"}
                    </div>
                </div>
            </div>
        </Link>
    );
}
