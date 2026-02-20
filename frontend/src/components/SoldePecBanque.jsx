import { useEffect, useState } from "react"
import { getSoldePecBanque } from '../api/accountLineApi'

export default function SoldePecBanque() {
  const [solde, setSolde] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    async function fetchSolde() {
      try {
        const data = await getSoldePecBanque()
        setSolde(data.soldePecBanque)
      } catch (err) {
        setError("Erreur lors de la récupération du solde")
      } finally {
        setLoading(false)
      }
    }

    fetchSolde()
  }, [])

  if (loading) return <p>Chargement du solde...</p>
  if (error) return <p style={{color:"red"}}>{error}</p>

  return (
    <div style={{ fontSize: "1.4rem", fontWeight: "bold" }}>
      Solde PEC Banque : {solde} €
    </div>
  )
}