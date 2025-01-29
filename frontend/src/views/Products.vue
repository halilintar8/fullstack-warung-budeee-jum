<script setup>
import ListProductsCustom from "../components/ListProductsCustom.vue";
import {ref, onMounted} from "vue";
import {useAuthStore, useCartStore} from "../stores/authStore";
import axios from "axios";

const authStore = useAuthStore();
const products = ref([]);
const search = ref("");

function setProducts(data) {
  products.value = data;
}

onMounted(async () => {
  await axios.get(`http://localhost:8080/api/v1/products/fetch`,
      {
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${authStore.token}`,
        },
      })
      .then((response) => setProducts(response.data.products))
      .catch((error) => console.error(error));
});

async function searchProduct() {
  await axios.get(`http://localhost:8080/api/v1/products/fetch?search=${search.value}`,
      {
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${authStore.token}`,
        },
      })
      .then((response) => setProducts(response.data.products))
      .catch((error) => console.error(error));
}

</script>

<template>
  <div class="row mt-4">
    <div class="col">
      <h2>
        Daftar
        <strong>Makanan</strong>
      </h2>
    </div>
  </div>

  <div class="row mt-3">
    <div class="col">
      <div class="input-group mb-3">
        <input
            v-model="search"
            @keyup="searchProduct"
            type="text"
            class="form-control"
            placeholder="Cari Makanan Kesukaan Anda .."
            aria-label="Cari"
            aria-describedby="basic-addon1"
        />

        <div class="input-group-prepend">
          <span class="input-group-text" id="basic-addon1">
            <i class="bi bi-search"></i>
          </span>
        </div>

      </div>
    </div>
  </div>

  <div class="row mb-4">
    <div class="col-md-4 mt-4" v-for="product in products" :key="product.id">
      <ListProductsCustom :product="product"/>
    </div>
  </div>

</template>

<style scoped>

</style>