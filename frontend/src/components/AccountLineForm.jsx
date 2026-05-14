import React, { useEffect, useState } from 'react'
import { getNatures } from '../api/naturesApi'

export default function AccountLineForm({ onCreate }) {
  const [date, setDate] = useState('')
  const [libelle, setLibelle] = useState('')
  const [natureCode, setNatureCode] = useState('')
  const [numCheque, setNumCheque] = useState('')
  const [montant, setMontant] = useState('')
  const [natures, setNatures] = useState([])
  const [loading, setLoading] = useState(true)

  // Charger les natures au montage du composant
  useEffect(() => {
    async function loadNatures() {
      try {
        const data = await getNatures()
        setNatures(data)
      } catch (error) {
        console.error('Erreur chargement natures:', error)
      } finally {
        setLoading(false)
      }
    }
    loadNatures()
  }, [])


  async function handleSubmit(e) {
    e.preventDefault()
    if (!date || !libelle || !natureCode || !montant) {
      alert('Date, libellé, nature et montant sont obligatoires')
      return
    }

    const dto = {
      date,
      libelle,
      natureCode, // si ton backend attend natureCode, renomme ici
      numCheque: numCheque ? Number(numCheque) : null,
      montant: Number(montant)
    }

    await onCreate(dto)

    setDate('')
    setLibelle('')
    setNatureCode('')
    setNumCheque('')
    setMontant('')
  }

  return (
    <form onSubmit={handleSubmit} style={{ border: '1px solid #eee', padding: 12, margin: '12px 0' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 8 }}>
        {/* ✨ type="date" : contrôle de date natif */}
        <input type="date" value={date} onChange={e => setDate(e.target.value)} required />
        <input placeholder="Libellé *" value={libelle} onChange={e => setLibelle(e.target.value)} />
        {/* ✨ Dropdown de natures au lieu de texte libre */}
        <select value={natureCode} onChange={e => setNatureCode(e.target.value)} required disabled={loading}>
          <option value="">-- Sélectionner une nature --</option>
          {natures.map(n => (
            <option key={n.code} value={n.code}>{n.label} ({n.code})</option>
          ))}
        </select>
        <input placeholder="Numéro chèque" value={numCheque} onChange={e => setNumCheque(e.target.value)} />
        {/* ✨ type="number" : saisie numérique avec . comme séparateur */}
        <input
            type="number"
            step="0.01"
            placeholder="Montant *"
            value={montant}
            onChange={e => setMontant(e.target.value)}
            onKeyDown={(e) => {
                if (e.key === "Enter") {
                    e.preventDefault();
                    e.currentTarget.form.requestSubmit();
                }
            }}
        />
      </div>
      <button type="submit" style={{ marginTop: 10 }}>Créer</button>
    </form>
  )
}