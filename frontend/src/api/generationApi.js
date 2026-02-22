import { http } from '../http'

export async function generateAnnualLines(dto) {
  const { data } = await http.post("/accountLines/generateAnnual", dto)
  return data
}