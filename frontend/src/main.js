import { createApp } from 'vue';
import App from './App.vue';
import { router } from './router';
import { createPinia } from "pinia";
import { useAuthStore } from "./stores/authStore.js";
import ToastPlugin from 'vue-toast-notification';

import './assets/css/main.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import 'vue-toast-notification/dist/theme-bootstrap.css';
import 'nprogress/nprogress.css'

const app = createApp(App);
const pinia = createPinia();
app.use(pinia);
app.use(router);
app.use(ToastPlugin);

const authStore = useAuthStore();
authStore.InterceptorRefreshToken();

app.mount("#app");