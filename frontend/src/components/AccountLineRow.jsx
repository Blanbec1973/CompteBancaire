import React, { useEffect, useState } from 'react'
import { formatMontant, formatDate, montantColor } from "../utils/format"
import { getNatures } from '../api/naturesApi'

export default function AccountLineRow({ accountLine, onUpdate }) {
  const [edit, setEdit] = useState(false)
  const [form, setForm] = useState({ ...accountLine })
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

  function change(k, v) { setForm({ ...form, [k]: v }) }

  function startEdit() {
    setForm({
      date: (accountLine.date ?? "").slice(0, 10),
      libelle: accountLine.libelle ?? "",
      natureCode: accountLine.natureCode ?? accountLine.nature?.code ?? "",
      numCheque: accountLine.numCheque ?? "",
      montant: accountLine.montant ?? "",
    });
    setEdit(true);
  }

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
          <button onClick={startEdit}>Modifier</button>
        </td>
      </tr>
    );
  }

  // Affichage en mode édition
  return (
    <tr>
      <td><input type="date" value={form?.date ?? ""} onChange={e => change('date', e.target.value)} /></td>
      <td><input value={form?.libelle ?? ""} onChange={e => change('libelle', e.target.value)} /></td>
      <td>
        <select
          value={form?.natureCode ?? ""}
          onChange={(e) => change("natureCode", e.target.value)}
          disabled={loading}
        >
          <option value="">-- Select nature --</option>
          {natures.map((n) => (
            <option key={n.code} value={n.code}>
              {n.label}
            </option>
          ))}
        </select>
      </td>
      <td><input value={form.numCheque ?? ''} onChange={e => change('numCheque', e.target.value)} /></td>
      <td><input type="number" step="0.01" value={form.montant ?? ''} onChange={e => change('montant', e.target.value)} /></td>
      <td>
        <button onClick={save}>Enregistrer</button>
        <button onClick={() => setEdit(false)} style={{ marginLeft: 8 }}>Annuler</button>
      </td>
    </tr>
  );
}

