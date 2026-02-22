export function formatMontant(montant) {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR'
  }).format(montant)
}

export function formatDate(dateStr) {
  const date = new Date(dateStr)
  return date.toLocaleDateString("fr-FR")
}

export function montantColor(montant) {
  if (montant === 0) return "#444" // neutre
  if (montant > 0) return "green"
  return "red"
}