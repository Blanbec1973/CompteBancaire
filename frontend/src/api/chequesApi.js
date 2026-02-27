import { http } from '../http'

export async function fetchCheques() {
   const { data } = await http.get('/accountLines/listcheques')
   return data
}