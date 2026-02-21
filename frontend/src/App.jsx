import React, { useEffect, useState } from 'react'
import { getAccountLines, searchAccountLines, createAccountLine, updateAccountLine} from './api/accountLineApi'
import { getSoldes } from './api/soldesApi'
import AccountLineForm from './components/AccountLineForm'
import AccountLineRow from './components/AccountLineRow'
import SoldePecBanque from "./components/SoldePecBanque"


export default function App() {
/*     return (
        <div style={{ textAlign: 'center', marginTop: '40px'}}>
            <h1> ✅ Frontend React opérationnel</h1>
            <p>Prêt à se connecter au backend SpringBoot</p>
        </div>
    ) */

    const [accountLines, setAccountLines] = useState([ ])
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
    // dto doit contenir utilisateurId (temporaire : 1)
    if (!dto.accountLine) dto.accountLine = 1
    const created = await createAccountLine(dto)
    setAccountLines((prev) => [created, ...prev])
  }

  async function handleUpdate(id, dto) {
    const updated = await updateAccountLine(id, dto)
    setAccountLines((prev) => prev.map(c => c.id === id ? updated : c))
  }

  return (
    <div style={{ maxWidth: 900, margin: '40px auto', fontFamily: 'Arial, sans-serif' }}>
      <h1>Compte — Gestion des lignes</h1>
        <SoldePecBanque />
      <div style={{ margin: '12px 0' }}>
        <input
          placeholder="Rechercher par libellé…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          style={{ padding: 8, width: 300 }}
        />
      </div>

      <AccountLineForm onCreate={handleCreate} />

      {loading && <p>Chargement…</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && accountLines.length === 0 && <p>Aucune ligne.</p>}

      <div>
        {accountLines.map(c => (
          <AccountLineRow key={c.id} accountLine={c} onUpdate={handleUpdate} />
        ))}
      </div>
    </div>
  )
}