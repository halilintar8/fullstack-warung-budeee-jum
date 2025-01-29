<script setup>
import {computed, onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import {useAuthStore} from "../stores/authStore.js";
import axios from "axios";

const props = defineProps(['listCarts']);

const router = useRouter();
const authStore = useAuthStore();
const isAuthenticated = computed(() => authStore.isAuthenticated);
let carts = ref([]);

function countCart(data) {
  carts = data;
}

onMounted(async () => {
  await axios.get(`http://localhost:8080/api/v1/keranjangs/fetch`,
      {
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${authStore.token}`,
        },
      })
      .then((response) => countCart(response.data.keranjangs))
      .catch((error) => console.error(error));
})

async function logout() {
  try {
    const response = await axios.post(
        "http://localhost:8080/api/v1/auth/logout",
        {}, // request body
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authStore.token}`, // insert token from store (pinia global state)
          },
        }
    );
    // console.log(response); // debugging response logout
  } catch (error) {
    console.log("Headers:", error.response?.headers);
    console.error("Logout failed:", error.response?.data?.message || error.message);
  } finally {
    authStore.clearToken(); // delete token from store (pinia global state) and localstorage
    await router.push({name: "auth-login"}); // redirect to login
  }
}

</script>

<template>
  <div class="container">
    <div class="row">
      <nav class="navbar navbar-expand-lg navbar-light bg-light rounded w-100">
        <div class="container-fluid">
          <!-- Brand or Logo -->
          <div v-if="isAuthenticated">
          <router-link :to="{ name: 'home' }" class="navbar-brand">Lobby</router-link>
          </div>
          <!-- Toggle button for mobile view -->
          <button
              class="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarResponsive"
              aria-controls="navbarResponsive"
              aria-expanded="false"
              aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <!-- Navbar links -->
          <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item" v-if="!isAuthenticated">
                <router-link :to="{ name: 'auth-login' }" class="nav-link">Login</router-link>
              </li>
              <li class="nav-item" v-if="!isAuthenticated">
                <router-link :to="{ name: 'auth-register' }" class="nav-link">Register</router-link>
              </li>
              <li class="nav-item" v-if="isAuthenticated">
                <router-link :to="{ name: 'layout-main' }" class="nav-link">Home</router-link>
              </li>
              <li class="nav-item" v-if="isAuthenticated">
                <router-link :to="{ name: 'products' }" class="nav-link">Product</router-link>
              </li>
            </ul>

            <!-- Right-aligned items -->
            <div class="d-flex align-items-center">
              <div class="nav-item me-3" v-if="isAuthenticated">
                <router-link :to="{ name: 'cart-detail' }" class="nav-link d-flex align-items-center">
                  Cart
                  <i class="bi bi-cart-plus ms-2"></i>
                  <span class="badge bg-success ms-2">{{ props ? props.listCarts.length : carts.length }}</span>
                </router-link>
              </div>
              <div class="nav-item" v-if="isAuthenticated">
                <button @click="logout" type="button" class="btn btn-outline-danger btn-sm">Logout</button>
              </div>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </div>
</template>

<style scoped>
.navbar-toggler {
  border: none;
}

.navbar-brand {
  font-weight: bold;
}

.nav-link {
  font-size: 1.1rem;
}

.badge {
  font-size: 0.75rem;
}

@media (max-width: 768px) {
  .navbar-nav {
    text-align: center;
  }

  .nav-item {
    margin-bottom: 1rem;
  }
}

.bg-success {
  background-color: #6CB52D !important;
}
</style>