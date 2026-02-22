import React, { useState } from 'react'
import { formatMontant, formatDate, montantColor } from "../utils/format"

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

  // Affichage en mode lecture
  if (!edit) {
    return (
      <tr>
        <td>{accountLine.date}</td>
        <td>{accountLine.libelle}</td>
        <td>{accountLine.natureCode}</td>
        <td>{accountLine.numCheque ?? '-'}</td>
        <td style={{ textAlign: "right" }}>
          <span className="montant-pill" style={{ color: montantColor(accountLine.montant) }}>
            {formatMontant(accountLine.montant)}
          </span>
        </td>
        <td>
          <button onClick={() => setEdit(true)}>Modifier</button>
        </td>
      </tr>
    );
  }

  // Affichage en mode édition (tu peux aussi le faire dans un <tr>)
  return (
    <tr>
      <td><input value={form.date} onChange={e => change('date', e.target.value)} /></td>
      <td><input value={form.libelle} onChange={e => change('libelle', e.target.value)} /></td>
      <td><input value={form.natureCode} onChange={e => change('natureCode', e.target.value)} /></td>
      <td><input value={form.numCheque ?? ''} onChange={e => change('numCheque', e.target.value)} /></td>
      <td><input value={form.montant ?? ''} onChange={e => change('montant', e.target.value)} /></td>
      <td>
        <button onClick={save}>Enregistrer</button>
        <button onClick={() => setEdit(false)} style={{ marginLeft: 8 }}>Annuler</button>
      </td>
    </tr>
  );
}