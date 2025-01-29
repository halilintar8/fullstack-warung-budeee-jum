import {defineStore} from 'pinia';
import axios from 'axios';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem("authToken") || null, // collect token from LocalStorage
        refreshToken: localStorage.getItem("authRefreshToken") || null,
        isAuthenticated: !!localStorage.getItem("authToken"), // check does token there is
    }),
    actions: {
        setToken(token, refreshToken) {
            this.token = token;
            this.refreshToken = refreshToken;
            this.isAuthenticated = !!token; // true if token there is

            // save token to LocalStorage
            if (token) {
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; // Set header Authorization
                localStorage.setItem("authToken", token);
                localStorage.setItem("authRefreshToken", refreshToken);
            } else {
                //localStorage.removeItem("authToken");
                this.clearToken();
            }
        },
        clearToken() {
            this.token = null;
            this.refreshToken = null;
            this.isAuthenticated = false;

            // delete header Authorization  and token from LocalStorage
            delete axios.defaults.headers.common["Authorization"];
            localStorage.removeItem("authToken");
            localStorage.removeItem("authRefreshToken");
        },
        InterceptorToken() {
            // add interceptor for include token to each request
            axios.interceptors.request.use(
                (config) => {
                    if (this.token) {
                        config.headers["Authorization"] = `Bearer ${this.token}`;
                    }
                    return config;
                },
                (error) => {
                    return Promise.reject(error);
                }
            );
        },
        InterceptorRefreshToken(){
            axios.interceptors.response.use(
                (response) => response,
                async (error) => {
                    const originalRequest = error.config;
                    if (
                        error.response &&
                        error.response.status === 401 &&
                        error.response.data &&
                        error.response.data.errors === "Token expired" &&
                        !originalRequest._retry
                    ) {
                        originalRequest._retry = true;
                        try {
                            const response = await axios.post(
                                "http://localhost:8080/api/v1/auth/token/refresh",
                                {},
                                {
                                    withCredentials: true,
                                    headers: {
                                        "Content-Type": "application/json",
                                        "Authorization": `Bearer ${this.token}`,
                                    }
                                }
                            );
                            const { access_token, refresh_token } = response.data.data;
                            this.setToken(access_token, refresh_token);
                            originalRequest.headers["Authorization"] = `Bearer ${access_token}`;
                            return axios(originalRequest);
                        } catch (refreshError) {
                            this.clearToken();
                            return Promise.reject(refreshError);
                        }
                    }
                    return Promise.reject(error);
                }
            );
        }
    },
});

export const useCartStore = defineStore('cart', {
    state: () => ({
        carts: [],
    }),
    actions: {
        setCarts(carts) {
            this.carts = carts;
        },
        clearCarts() {
            this.carts = null;
        }
    },
});