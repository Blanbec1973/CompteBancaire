import { useEffect, useState } from "react"
import { getSoldes } from '../api/soldesApi'
import { formatMontant, formatDate, montantColor } from "../utils/format"

export default function SoldePecBanque({ refreshSolde }) {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let isMounted = true; // Pour éviter les updates après démontage
    setLoading(true);
    async function fetchSolde() {
      try {
        const result = await getSoldes();
        if (isMounted) setData(result);
      } catch (err) {
        if (isMounted) setError("Erreur lors de la récupération du solde");
      } finally {
        if (isMounted) setLoading(false);
      }
    }
    fetchSolde();
    return () => { isMounted = false; };
  }, [refreshSolde]);

  if (loading) return <p>Chargement...</p>
  if (error) return <p>{error}</p>

  return (
     <div className="card" style={{ width: "250px" }}>
      <p>Solde PEC Banque :
          <span className="montant-pill"
                style={{ color: montantColor(data.soldePecBanque) }}>
            {formatMontant(data.soldePecBanque)}
          </span>
      </p>
      <p>Solde au {formatDate(data.dateCalcul)} :
          <span className="montant-pill"
                          style={{ color: montantColor(data.soldeFinMois) }}>
          {formatMontant(data.soldeFinMois)}
          </span>
      </p>
    </div>
  )
}