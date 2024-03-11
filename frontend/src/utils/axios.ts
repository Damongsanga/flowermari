// import Axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
// import { useNavigate } from 'react-router-dom';
// import { useAuthStore } from '../stores/auth.ts';
//
// // Token 인증이 필요한 요청에 대해서는 파라미터를 주지 않아도 자동으로 헤더에 토큰 넣음, Interceptor 재인증
// // 파라미터로 isAuth: boolean 기본값은 true
// // 로그인, 전체 리스트 조회 등 일부 Token 인증이 필요없는 요청에 대해서만 파라미터 isAuth false를 명시해주면 된다.
//
// const useLocalAxios = (isAuth?: boolean): AxiosInstance => {
//     const authenticated: boolean = isAuth !== undefined ? isAuth : true;
//     const authStore = useAuthStore();
//     const navigate = useNavigate();
//
//     const instance = Axios.create({
//         baseURL: import.meta.env.VITE_API_BASE_URL,
//     });
//
//     if (authenticated) {
//         instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
//             if (authStore.accessToken) {
//                 config.headers = config.headers || {};
//                 config.headers.Authorization = `Bearer ${authStore.accessToken}`;
//             }
//
//             return config;
//         });
//
//         instance.interceptors.response.use(
//             (response) => response,
//             async (error) => {
//                 if (error.response.status === 401 && error.config.url !== '/auth/refresh') {
//                     if (!authStore.refreshToken) {
//                         authStore.clearAuth();
//                         return Promise.resolve();
//                     }
//
//                     const refreshAxios = Axios.create({
//                         baseURL: import.meta.env.VITE_API_BASE_URL,
//                     });
//
//                     let refreshResponse: AxiosResponse | null = null;
//                     try {
//                         refreshResponse = await refreshAxios.get('/auth/refresh', {
//                             headers: {
//                                 Refresh: authStore.refreshToken,
//                             },
//                         });
//
//                         if (!refreshResponse?.data.accessToken) {
//                             authStore.clearAuth();
//                             return Promise.resolve();
//                         }
//
//                         authStore.setAccessToken(refreshResponse.data.accessToken);
//                     } catch (e) {
//                         console.error(e);
//                         authStore.clearAuth();
//                         return Promise.resolve();
//                     }
//
//                     console.log('changed accessToken');
//                     error.config.Authorization = `Bearer ${refreshResponse.data.accessToken}`;
//                     navigate('/');
//                 }
//
//                 return Promise.resolve();
//             },
//         );
//     }
//
//     return instance;
// };
//
// export { useLocalAxios };
