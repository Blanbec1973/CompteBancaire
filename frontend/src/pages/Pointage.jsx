import React, { useEffect, useState } from "react"
import {
  getNonPointedLines,
  getPointedToday,
  pointLine,
  unpointLine
} from "../api/accountLineApi"
import { formatMontant } from "../utils/format"
import { MdCheck, MdClear } from "react-icons/md"

export default function Pointage() {

  const [nonPointed, setNonPointed] = useState([])
  const [pointedToday, setPointedToday] = useState([])

  useEffect(() => { load() }, [])

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
      <div className="card" style={{ maxWidth: 900, margin: "40px auto", fontFamily: "Arial" }}>
      <h1>Pointage des écritures</h1>

      <h2>À pointer</h2>

      <table className="table-pointage">
        <thead>
          <tr style={{ background: "#eee" }}>
            <th>Date</th>
            <th>Libellé</th>
            <th>Nature</th>
            <th>Num chèque</th>
            <th>Montant</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {nonPointed.map(line => (
            <tr key={line.id} style={{ borderBottom: "1px solid #ddd" }}>
              <td>{line.date}</td>
              <td>{line.libelle}</td>
              <td>{line.natureCode}</td>
              <td>{line.numCheque}</td>
              <td style={{ textAlign: "right" }}>
                    {formatMontant(line.montant)}
              </td>
              <td>
                <button onClick={() => pointer(line)} style={{ background: "none", border: "none", cursor: "pointer" }}>
                  <MdCheck size={20} color="green" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2 style={{ marginTop: "40px" }}>Pointées aujourd’hui</h2>

      <table className="table-pointage">
        <thead>
          <tr style={{ background: "#eee" }}>
            <th>Date</th>
            <th>Libellé</th>
            <th>Nature</th>
            <th>Num chèque</th>
            <th>Montant</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {pointedToday.map(line => (
            <tr key={line.id} style={{ borderBottom: "1px solid #ddd" }}>
              <td>{line.date}</td>
              <td>{line.libelle}</td>
              <td>{line.natureCode}</td>
              <td>{line.numCheque}</td>
              <td style={{ textAlign: "right" }}>
                   {formatMontant(line.montant)}
              </td>
              <td>
                <button onClick={() => depointer(line)} style={{ background: "none", border: "none", cursor: "pointer" }}>
                  <MdClear size={20} color="red" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}