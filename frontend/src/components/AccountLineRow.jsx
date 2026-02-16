import React, { useState } from 'react'

export default function AccountLineRow({ accountLine, onUpdate }) {
  const [edit, setEdit] = useState(false)
  const [form, setForm] = useState({ ...accountLine })

  function change(k, v) { setForm({ ...form, [k]: v }) }

  async function save() {
    await onUpdate(accountLine.id, {
      date: form.date,
      libelle: form.libelle,
      natureCode: form.natureCode,
      numCheque: form.numCheque,
      montant: form.montant,
    })
    setEdit(false)
  }

if (!edit) {
    return (
      <li style={{ marginBottom: 8 }}>
        <strong>{accountLine.date} {accountLine.libelle}</strong>
        {' — '}
        {accountLine.natureCode}
        {' · Chq: '}
        {accountLine.numCheque ?? '-'}
        {' · Montant: '}
        {accountLine.montant ?? '-'}
        {' '}
        <button onClick={() => setEdit(true)}>Modifier</button>
      </li>
    )
  }

  return (
    <div style={{ border: '1px solid #ddd', padding: 10, marginBottom: 8 }}>
      {!edit ? (
        <>
          <div><b>{accountLine.date} {accountLine.libelle}</b> — {accountLine.natureCode}</div>
          <div style={{ fontSize: 13, color: '#555' }}>
            {accountLine.numCheque || '-'} · {accountLine.montant || '-'}
          </div>
          <div style={{ marginTop: 6 }}>
            <button onClick={() => setEdit(true)}>Modifier</button>
          </div>
        </>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)', gap: 6 }}>
          <input value={form.date} onChange={e => change('date', e.target.value)} />
          <input value={form.libelle} onChange={e => change('libelle', e.target.value)} />
          <input value={form.natureCode} onChange={e => change('nature', e.target.value)} />
          <input value={form.numCheque || ''} onChange={e => change('numcheque', e.target.value)} />
          <input value={form.montant || ''} onChange={e => change('montant', e.target.value)} />
          <div style={{ gridColumn: '1 / -1', marginTop: 6 }}>
            <button onClick={save}>Enregistrer</button>
            <button onClick={() => setEdit(false)} style={{ marginLeft: 8 }}>Annuler</button>
          </div>
        </div>
      )}
    </div>
  )
}