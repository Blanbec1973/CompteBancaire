import React from "react"

export default function GenerationAnnuel() {
  return (
    <div style={{ maxWidth: 600, margin: "40px auto", fontFamily: "Arial" }}>
      <h1>Génération d'écritures annuelles</h1>

      <p>
        Ceci est une version statique pour tester la navigation.
        Plus tard, cet écran permettra de générer automatiquement
        des écritures récurrentes (EDF, assurance, abonnements, etc.).
      </p>

      <div style={{
        marginTop: "20px",
        padding: "20px",
        border: "1px solid #ddd",
        borderRadius: "8px",
        background: "#fafafa"
      }}>
        <p><strong>Exemple :</strong></p>
        <ul>
          <li>Prélèvement EDF — 15 de chaque mois</li>
          <li>Montant : 92,30 €</li>
          <li>Année cible : 2026</li>
        </ul>

        <p style={{ opacity: 0.6 }}>
          (Interface fonctionnelle à venir…)
        </p>
      </div>
    </div>
  )
}