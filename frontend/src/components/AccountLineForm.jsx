import React, { useEffect, useState, useRef } from 'react'
import { getNatures } from '../api/naturesApi'
import { searchAccountLines } from "../api/accountLineApi"

function previousMonthIsoDate() {
  const d = new Date();
  d.setMonth(d.getMonth() - 1);
  return d.toISOString().slice(0, 10); // yyyy-mm-dd
}

export default function AccountLineForm({ onCreate }) {
  const [date, setDate] = useState(previousMonthIsoDate());
  const [libelle, setLibelle] = useState('')
  const [natureCode, setNatureCode] = useState('')
  const [numCheque, setNumCheque] = useState('')
  const [montant, setMontant] = useState('')
  const [natures, setNatures] = useState([])
  const [loading, setLoading] = useState(true)

  const [suggestions, setSuggestions] = useState([])
  const [openSug, setOpenSug] = useState(false)
  const [hi, setHi] = useState(-1) // index surligné
  const debounceRef = useRef(null)
  const natureRef = useRef(null)   // pour focus le champ suivant (select nature)
  const [libelleFocused, setLibelleFocused] = useState(false)

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

//Ecoute libellé :
useEffect(() => {
  // Nettoyage si vide / trop court
  const q = (libelle ?? "").trim()
  if (q.length < 2) {
    setSuggestions([])
    setOpenSug(false)
    setHi(-1)
    return
  }

  // Debounce : on attend un peu avant d’appeler l’API
  if (debounceRef.current) clearTimeout(debounceRef.current)

  debounceRef.current = setTimeout(async () => {
    try {
      const lines = await searchAccountLines(q) // renvoie des écritures matchées
      // On extrait les libellés uniques
      const uniq = []
      const seen = new Set()
      for (const l of (lines ?? [])) {
        const lab = (l.libelle ?? "").trim()
        if (!lab) continue
        const key = lab.toLowerCase()
        if (!seen.has(key)) {
          seen.add(key)
          uniq.push(lab)
        }
        if (uniq.length >= 8) break // limite à 8 suggestions
      }
      setSuggestions(uniq)
      setOpenSug(uniq.length > 0)
      setHi(uniq.length > 0 ? 0 : -1)
    } catch (e) {
      // en cas d’erreur, on n’affiche juste rien
      setSuggestions([])
      setOpenSug(false)
      setHi(-1)
    }
  }, 180)

  return () => {
    if (debounceRef.current) clearTimeout(debounceRef.current)
  }
}, [libelle])

function acceptSuggestion(value) {
  setLibelle(value)
  setOpenSug(false)
  setSuggestions([])
  setHi(-1)
}

function onLibelleKeyDown(e) {
  if (!openSug || suggestions.length === 0) {
      return
  }

  if (e.key === "ArrowDown") {
    e.preventDefault()
    setHi((prev) => Math.min(prev + 1, suggestions.length - 1))
  } else if (e.key === "ArrowUp") {
    e.preventDefault()
    setHi((prev) => Math.max(prev - 1, 0))
  } else if (e.key === "Enter") {
    // Enter accepte la suggestion surlignée
    e.preventDefault()
    if (hi >= 0) acceptSuggestion(suggestions[hi])
  } else if (e.key === "Tab") {
    // ⭐ Ton besoin : Tab accepte la suggestion, puis on passe au champ suivant
    if (hi >= 0) {
      e.preventDefault()
      acceptSuggestion(suggestions[hi])
      // focus sur le select nature
      // petit timeout pour laisser React rerender
      setTimeout(() => natureRef.current?.focus(), 0)
    }
  } else if (e.key === "Escape") {
    e.preventDefault()
    setOpenSug(false)
    setHi(-1)
  }
}

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

    setDate(previousMonthIsoDate())
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
        <div style={{ position: "relative", width: "100%", gridColumn: "span 1" }}>
          <input
            style={{ width: "100%", boxSizing: "border-box" }}
            placeholder="Libellé *"
            value={libelle}
            onChange={(e) => setLibelle(e.target.value)}
            onKeyDown={onLibelleKeyDown}
            onFocus={() => {
              setLibelleFocused(true)
              if (suggestions.length > 0) setOpenSug(true)
            }}
            onBlur={() => {
              setLibelleFocused(false)
              setTimeout(() => setOpenSug(false), 100)
            }}
          />

          {openSug && libelleFocused && suggestions.length > 0 && (
            <div
              style={{
                position: "absolute",
                top: "100%",
                left: 0,
                right: 0,
                background: "white",
                border: "1px solid #ddd",
                borderRadius: 6,
                boxShadow: "0px 4px 12px rgba(0,0,0,0.08)",
                zIndex: 50,
                marginTop: 4,
                overflow: "hidden",
              }}
            >
              {suggestions.map((s, idx) => (
                <div
                  key={s + idx}
                  onMouseDown={() => acceptSuggestion(s)} // mouseDown > click (évite blur)
                  onMouseEnter={() => setHi(idx)}
                  style={{
                    padding: "8px 10px",
                    cursor: "pointer",
                    background: idx === hi ? "#f3f4f6" : "white",
                  }}
                >
                  {s}
                </div>
              ))}
            </div>
          )}
        </div>
        {/* ✨ Dropdown de natures au lieu de texte libre */}
        <select ref={natureRef} value={natureCode} onChange={e => setNatureCode(e.target.value)} required disabled={loading}>
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