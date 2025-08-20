function L({ label, children }) {
    return (
        <div>
            <div className="text-[11px] uppercase text-slate-500">{label}</div>
            {children}
        </div>
    );
}

const Select = (props) => (
    <select
        className="mt-1 block w-full rounded border-slate-300 text-sm focus:border-slate-400 focus:ring-0"
        {...props}
    >
        <option value="">Any</option>
    </select>
);

export default function FiltersSidebar() {
    return (
        <div className="rounded border border-slate-200 bg-white p-4">
            <div className="mb-3 text-sm font-semibold">Filters</div>
            <div className="flex flex-col gap-3">
                <L label="Show Status"><Select /></L>
                <L label="Show Type"><Select /></L>
                <L label="Genre"><Select /></L>
                <L label="Language"><Select /></L>
                <L label="Country"><Select /></L>
                <L label="Network"><Select /></L>
                <L label="Web Channel"><Select /></L>
                <L label="Runtime"><Select /></L>
                <L label="Rating"><Select /></L>
                <div className="my-2 h-px bg-slate-200" />
                <L label="Premiered since"><Select /></L>
                <L label="Premiered until"><Select /></L>
            </div>
        </div>
    );
}
