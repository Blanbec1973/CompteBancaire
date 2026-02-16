import React, { useState } from 'react'

export default function AccountLineForm({ onCreate }) {
  const [date, setDate] = useState('')
  const [libelle, setLibelle] = useState('')
  const [natureCode, setNatureCode] = useState('')
  const [numCheque, setNumCheque] = useState('')
  const [montant, setMontant] = useState('')


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
      <h3>Ajouter une ligne</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 8 }}>
        <input placeholder="Date *" value={date} onChange={e => setDate(e.target.value)} />
        <input placeholder="Libellé *" value={libelle} onChange={e => setLibelle(e.target.value)} />
        <input placeholder="Nature *" value={natureCode} onChange={e => setNatureCode(e.target.value)} />
        <input placeholder="Numéro chèque" value={numCheque} onChange={e => setNumCheque(e.target.value)} />
        <input placeholder="Montant *" value={montant} onChange={e => setMontant(e.target.value)} />
      </div>
      <button type="submit" style={{ marginTop: 10 }}>Créer</button>
    </form>
  )
}