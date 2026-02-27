import { useEffect, useState } from "react";
import { fetchCheques } from "../api/chequesApi";
import { formatMontant, formatDate, montantColor } from "../utils/format"

export default function ChequesPage() {
  const [cheques, setCheques] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCheques(1) // ex: compteId = 1
      .then(data => setCheques(data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Chargement...</p>;

  return (
    <div className="card" style={{ maxWidth: 900, margin: "40px auto", fontFamily: "Arial" }}>
      <h1>Liste des chèques</h1>

      <table className="table-pointage">
        <thead>
          <tr style={{ background: "#eee" }}>
            <th>Date</th>
            <th>Libellé</th>
            <th>Numéro</th>
            <th>Montant</th>
            <th>Pointage</th>
          </tr>
        </thead>
        <tbody>
          {cheques.map(cheque => (
            <tr key={cheque.id}>
              <td  style={{ textAlign: "center" }}>{formatDate(cheque.date)}</td>
              <td style={{ textAlign: "left" }}>{cheque.libelle}</td>
              <td style={{ textAlign: "center" }}>{cheque.numCheque}</td>
              <td style={{ textAlign: "right" }}>
                  <span
                    className="montant-pill"
                    style={{ color: montantColor(cheque.montant) }}
                  >
                    {formatMontant(cheque.montant)}
                  </span>
              </td>
              <td>{cheque.pecBanque}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const styles = {
  table: {
    width: "100%",
    borderCollapse: "collapse"
  }
};