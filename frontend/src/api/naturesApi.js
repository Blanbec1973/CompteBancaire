import { http } from '../http'

/**
 * Récupère toutes les natures disponibles
 * Utilisé pour les dropdowns
 * @returns {Promise<Array>} liste des natures [{code, label, requiresChequeNumber}, ...]
 */
export async function getNatures() {
  const { data } = await http.get('/natures')
  return data
}

/**
 * Récupère une nature spécifique par son code
 * @param {string} code - code de la nature (ex: "CHQ", "VIR")
 * @returns {Promise<Object>} la nature {code, label, requiresChequeNumber}
 */
export async function getNatureByCode(code) {
  const { data } = await http.get(`/natures/${code}`)
  return data
}

