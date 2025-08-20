import { useEffect, useMemo, useState } from "react";
import { api } from "../api";
import ShowTile from "../ui/ShowTile";
import FiltersSidebar from "../ui/FiltersSidebar";
import Pager from "../ui/Pager";

const PAGE_SIZE = 24;                 // server-side size
const DEFAULT_SORT = "name,asc";

export default function ShowsList() {
    const [page, setPage] = useState(1);       // UI 1-based
    const [items, setItems] = useState([]);
    const [totalPages, setTotalPages] = useState(1);
    const [loading, setLoading] = useState(true);

    const params = useMemo(
        () => ({ page: page - 1, size: PAGE_SIZE, sort: DEFAULT_SORT }),
        [page]
    );

    useEffect(() => {
        let cancelled = false;
        setLoading(true);
        api.get("/api/shows", { params })
            .then((res) => {
                if (cancelled) return;
                const data = res.data;
                // Accept Spring Page OR plain array
                const list = Array.isArray(data?.content)
                    ? data.content
                    : Array.isArray(data) ? data : [];
                setItems(list.map(normalize));
                setTotalPages(Number.isFinite(data?.totalPages) ? data.totalPages : 1);
            })
            .finally(() => !cancelled && setLoading(false));
        return () => { cancelled = true; };
    }, [params]);

    return (
        <div className="grid grid-cols-12 gap-8">
            {/* Left: shows grid */}
            <section className="col-span-12 lg:col-span-9">

                <h1 className="mb-6 text-4xl font-semibold">Shows</h1>

                <div className="grid grid-cols-2 gap-6 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6">
                    {loading &&
                        Array.from({ length: 24 }).map((_, i) => (
                            <div key={i} className="h-[360px] animate-pulse rounded border border-slate-200 bg-slate-100" />
                        ))}

                    {!loading && items.map(s => (
                        <div key={s.id} className="h-full">
                            <ShowTile show={s} />
                        </div>
                    ))}

                    {!loading && items.length === 0 && (
                        <div className="col-span-full py-6 text-slate-500">No shows found.</div>
                    )}
                </div>


                {/* Pagination */}
                <Pager page={page} totalPages={totalPages} onChange={setPage} />
            </section>

            {/* Right: filters */}
            <aside className="col-span-12 lg:col-span-3">
                <div className="sticky top-6">
                    <FiltersSidebar />
                </div>
            </aside>
        </div>
    );
}

function normalize(e) {
    return {
        id: e.id,
        name: e.name,
        imageMedium: e.image?.medium,
        imageOriginal: e.image?.original,
        rating: e.rating?.average, // 0..10
    };
}
