// /pages/EcrituresGenerales.jsx
import React, { useEffect, useState } from 'react'
import { getAccountLines, searchAccountLines, createAccountLine, updateAccountLine} from '../api/accountLineApi'
import SoldePecBanque from "../components/SoldePecBanque"
import AccountLineForm from '../components/AccountLineForm'
import AccountLineRow from '../components/AccountLineRow'


export default function EcrituresGenerales({ triggerRefreshSolde }) {

  const [accountLines, setAccountLines] = useState([])
  const [loading, setLoading]  = useState(true)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')

  async function load() {
    try {
      setLoading(true)
      const data = query ? await searchAccountLines(query) : await getAccountLines()
      setAccountLines(data)
    } catch (e) {
      setError("Impossible de charger les lignes")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [query])

  async function handleCreate(dto) {
    if (!dto.accountLine) dto.accountLine = 1
    await createAccountLine(dto)
    await load()               // ✅ recharge avec GET /accountLines
    triggerRefreshSolde()
  }

  async function handleUpdate(id, dto) {
    const updated = await updateAccountLine(id, dto)
    setAccountLines((prev) => prev.map(c => c.id === id ? updated : c))
  }

  return (
    <div style={{ maxWidth: 900, margin: '40px auto' }}>
      <h1>Compte — Toutes les écritures</h1>

      <input
        placeholder="Rechercher…"
        value={query}
        onChange={e => setQuery(e.target.value)}
      />

      <AccountLineForm onCreate={handleCreate} />

      {loading && <p>Chargement…</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <table className="table-pointage">
        <thead>
          <tr>
            <th>Date</th>
            <th>Libellé</th>
            <th>Nature</th>
            <th>Num chèque</th>
            <th>Montant</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {accountLines.map(c => (
            <AccountLineRow key={c.id} accountLine={c} onUpdate={handleUpdate} />
          ))}
        </tbody>
      </table>
    </div>
  )
}