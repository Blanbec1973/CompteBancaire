import { http } from '../http'

export const getAppInfo = async () => {
  return http.get('/app-info');
};

