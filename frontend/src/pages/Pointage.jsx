import React, { useEffect, useState } from "react"
import {
  getNonPointedLines,
  getPointedToday,
  pointLine,
  unpointLine
} from "../api/accountLineApi"

export default function Pointage() {

  const [nonPointed, setNonPointed] = useState([])
  const [pointedToday, setPointedToday] = useState([])

  useEffect(() => {
    load()
  }, [])

  async function load() {
    const a = await getNonPointedLines()
    const b = await getPointedToday()
    setNonPointed(a)
    setPointedToday(b)
  }

  async function pointer(line) {
    const today = new Date().toISOString().slice(0, 10)
    const updated = await pointLine(line.id, today)

    setNonPointed(prev => prev.filter(l => l.id !== line.id))
    setPointedToday(prev => [updated, ...prev])
  }

  async function depointer(line) {
    const updated = await unpointLine(line.id)

    setPointedToday(prev => prev.filter(l => l.id !== line.id))
    setNonPointed(prev => [updated, ...prev])
  }

  return (
    <div style={{ maxWidth: 900, margin: "40px auto" }}>
      <h1>Pointage des écritures</h1>

      <h2>Écritures non pointées</h2>
      {nonPointed.map(line => (
        <div key={line.id}>
          {line.date} - {line.libelle} — {line. natureCode} - {line.montant}€
          <button onClick={() => pointer(line)}>Pointer</button>
        </div>
      ))}

      <h2>Écritures pointées aujourd’hui</h2>
      {pointedToday.map(line => (
        <div key={line.id}>
          {line.date} - {line.libelle} — {line. natureCode} - {line.montant}€
          <button onClick={() => depointer(line)}>Dépointer</button>
        </div>
      ))}
    </div>
  )
}