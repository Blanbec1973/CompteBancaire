import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import EcrituresGenerales from "./pages/EcrituresGenerales";
import Pointage from "./pages/Pointage";
import GenerationAnnuel from "./pages/GenerationAnnuel";
import SoldePecBanque from "./components/SoldePecBanque";
import "./styles/table.css";
import "./styles/theme.css";

export default function App() {
  const [refreshSolde, setRefreshSolde] = useState(0);

  function triggerRefreshSolde() {
    setRefreshSolde(prev => prev + 1);
  }

  return (
    <Router>
      <div className="main-header">
        <div className="nav-links">
          <Link to="/">Écritures</Link>
          <Link to="/pointage">Pointage</Link>
          <Link to="/generation-annuel">Génération annuelle</Link>
        </div>
        <div className="header-solde">
          <SoldePecBanque refreshSolde={refreshSolde} />
        </div>
      </div>
      <Routes>
        <Route path="/" element={<EcrituresGenerales triggerRefreshSolde={triggerRefreshSolde} />} />
        <Route path="/pointage" element={<Pointage triggerRefreshSolde={triggerRefreshSolde} />} />
        <Route path="/generation-annuel" element={<GenerationAnnuel />} />
      </Routes>
    </Router>
  );
}
