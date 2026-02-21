import { http } from '../http'

// LISTER
export async function getAccountLines() {
  const { data } = await http.get('/accountLines')
  return data
}

// CHERCHER par nom
export async function searchAccountLines(q) {
  const { data } = await http.get('/accountLines/search', { params: { q } })
  return data
}

// CREER
export async function createAccountLine(dto) {
  const { data } = await http.post('/accountLines', dto)
  return data
}

// METTRE A JOUR
export async function updateAccountLine(id, dto) {
  const { data } = await http.put(`/accountLines/${id}`, dto)
  return data
}

// pointer une ligne
export async function pointLine(id, date) {
  const { data } = await http.put(`/accountLines/${id}/pointage`, { pecBanque: date })
  return data
}

// dépointer une ligne
export async function unpointLine(id) {
  const { data } = await http.put(`/accountLines/${id}/pointage`, { pecBanque: null })
  return data
}


// Lignes non pointées :
export async function getNonPointedLines() {
      const { data } = await http.get('/accountLines/non-pointed')
      return data
}

// lignes pointées ce jour :
export async function getPointedToday() {
  const today = todayIso()
  const { data } = await http.get(`/accountLines/pointed/${today}`)
  return data
}

function todayIso() {  return new Date().toISOString().slice(0, 10)}