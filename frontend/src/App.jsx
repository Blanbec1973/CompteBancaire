import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom"
import EcrituresGenerales from "./pages/EcrituresGenerales"
import Pointage from "./pages/Pointage"
import GenerationAnnuel from "./pages/GenerationAnnuel"

export default function App() {
  return (
    <Router>

      <nav style={{ padding: 20, borderBottom: "1px solid #ccc" }}>
        <Link to="/">Écritures</Link> |{" "}
        <Link to="/pointage">Pointage</Link> |{" "}
        <Link to="/generation-annuel">Génération annuelle</Link>
      </nav>

      <Routes>
        <Route path="/" element={<EcrituresGenerales />} />
        <Route path="/pointage" element={<Pointage />} />
        <Route path="/generation-annuel" element={<GenerationAnnuel />} />
      </Routes>

    </Router>
  )
}