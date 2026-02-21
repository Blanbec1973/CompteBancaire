import { useEffect, useState } from "react"
import { getSoldes } from '../api/soldesApi'

export default function SoldePecBanque() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    async function fetchSolde() {
      try {
        const result = await getSoldes()
        setData(result)
      } catch (err) {
        setError("Erreur lors de la récupération du solde")
      } finally {
        setLoading(false)
      }
    }

    fetchSolde()
  }, [])

  if (loading) return <p>Chargement...</p>
  if (error) return <p>{error}</p>

  return (
    <div>
      <p>Solde PEC Banque : {data.soldePecBanque} €</p>
      <p>Solde fin du mois : {data.soldeFinMois} €</p>
      <p>Date du calcul : {data.dateCalcul}</p>
    </div>
  )
}