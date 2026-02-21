import { http } from '../http'

//recupérer le solde pec par la Banque :
export async function getSoldes() {
const { data } = await http.get('/soldes/getsoldes')
  return data
}
