export default function Pager({ page, totalPages, onChange }) {
    const prev = () => onChange(Math.max(1, page - 1));
    const next = () => onChange(Math.min(totalPages || 1, page + 1));

    return (
        <div className="mt-6 flex items-center justify-center gap-2">
            <button
                onClick={prev}
                disabled={page <= 1}
                className="h-9 rounded border border-slate-300 px-3 text-sm disabled:opacity-40"
            >
                ‹
            </button>
            <div className="flex h-9 min-w-9 items-center justify-center rounded-full bg-blue-600 px-3 text-sm font-semibold text-white">
                {page}
            </div>
            <button
                onClick={next}
                disabled={!totalPages || page >= totalPages}
                className="h-9 rounded border border-slate-300 px-3 text-sm disabled:opacity-40"
            >
                ›
            </button>
        </div>
    );
}
