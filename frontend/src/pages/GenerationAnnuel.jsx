import React, { useState } from "react"
import { generateAnnualLines } from "../api/generationApi"

export default function GenerationAnnuel() {

  const [date, setDate] = useState("")
  const [libelle, setLibelle] = useState("")
  const [natureCode, setNatureCode] = useState("")
  const [montant, setMontant] = useState("")
  const [message, setMessage] = useState("")

  async function handleSubmit(e) {
    e.preventDefault()

    try {
      await generateAnnualLines({
        date,
        libelle,
        natureCode,
        montant: Number(montant)
      })

      setMessage("Écritures générées avec succès !")

      // reset
      setDate("")
      setLibelle("")
      setNatureCode("")
      setMontant("")
    } catch (err) {
      setMessage("Erreur lors de la génération")
    }
  }

  return (
    <div style={{ maxWidth: 600, margin: "40px auto", fontFamily: "Arial" }}>
      <h1>Génération d'écritures annuelles</h1>

      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "12px", marginTop: "20px" }}>

        <label>Date de la première écriture :</label>
        <input type="date" value={date} onChange={(e) => setDate(e.target.value)} required />

        <label>Libellé :</label>
        <input type="text" value={libelle} onChange={(e) => setLibelle(e.target.value)} required />

        <label>Nature :</label>
        <input type="text" value={natureCode} onChange={(e) => setNatureCode(e.target.value)} required />

        <label>Montant :</label>
        <input type="number" step="0.01" value={montant} onChange={(e) => setMontant(e.target.value)} required />

        <button type="submit">Générer</button>
      </form>

      {message && <p style={{ marginTop: "20px" }}>{message}</p>}
    </div>
  )
}