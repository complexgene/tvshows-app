import { Routes, Route, Link } from "react-router-dom";
import ShowsList from "./pages/ShowsList";
import ShowDetails from "./pages/ShowDetails"; // your existing details page

export default function App() {
    return (
        <div className="min-h-screen">
            {/* Top bar */}
            <header className="border-b border-slate-200">
                <div className="mx-auto max-w-7xl px-4">
                    <div className="h-14 flex items-center">
                        <Link to="/" className="text-lg font-bold">TVMAZE</Link>
                    </div>
                </div>
            </header>

            {/* Main content area */}
            <main className="mx-auto max-w-7xl px-4 py-6">
                <Routes>
                    <Route path="/" element={<ShowsList />} />
                    <Route path="/shows/:id" element={<ShowDetails />} />
                </Routes>
            </main>
        </div>
    );
}
