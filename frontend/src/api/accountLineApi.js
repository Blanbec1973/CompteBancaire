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
