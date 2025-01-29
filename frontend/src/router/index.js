import {createRouter, createWebHistory} from "vue-router";
import {useAuthStore} from "../stores/authStore";
import Nprogress from "nprogress";

const routes = [
    {
        path: '/home',
        component: () => import('../views/Home.vue'),
        name: 'home',
        meta: { requiresAuth: true }, // only for user who are login can access
    },
    {
        path: '/:notFound*',
        component: () => import('../views/NotFound.vue'),
        name: 'not-found',
    },
    {
        path: '/login',
        component: () => import('../views/Login.vue'),
        name: 'auth-login',
        meta: { guest: true }, // only for users who are not logged in
    },
    {
        path: '/register',
        component: () => import('../views/Register.vue'),
        name: 'auth-register',
        meta: { guest: true },
    },
    {
        path: "/",
        component: () => import("../views/LayoutMain.vue"),
        name: "layout-main",
        meta: { requiresAuth: true },
    },
    {
        path: '/product/:id',
        component: () => import('../views/ProductDetail.vue'),
        name: 'product-detail',
        meta: { requiresAuth: true },
    },
    {
        path: "/cart",
        component: () => import("../views/CartDetail.vue"),
        name: "cart-detail",
        meta: { requiresAuth: true },
    },
    {
        path: "/order",
        component: () => import('../views/OrderSuccess.vue'),
        name: 'order-success',
        meta: { requiresAuth: true },
    },
    {
        path: "/products",
        component: () => import('../views/Products.vue'),
        name: 'products',
        meta: { requiresAuth: true },
    }
]

export const router = createRouter({
    history: createWebHistory(),
    routes: routes
});

// Middleware for route guards
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore(); // Access the auth store

    // page who needed authentication
    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        console.log('Akses ditolak: Anda harus login untuk mengakses halaman ini.');
        return next({ name: 'auth-login' }); // Redirect ke halaman login
    }

    // Page for guest users (already logged in cannot access)
    if (to.meta.guest && authStore.isAuthenticated) {
        console.log('Akses ditolak: Anda sudah login.');
        return next({ name: 'home' }); // Redirect to main page
    }

    Nprogress.start();
    // consent access to the page
    next();

});

router.afterEach(() => {
   Nprogress.done();
});