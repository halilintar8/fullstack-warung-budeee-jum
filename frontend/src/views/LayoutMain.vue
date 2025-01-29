<script setup>
import {ref, onMounted, defineAsyncComponent} from "vue";
const Hero = defineAsyncComponent(() => import("../components/Hero.vue"));
const ListProductsCustom = defineAsyncComponent(() => import("../components/ListProductsCustom.vue"));
import {useAuthStore} from "../stores/authStore";
import axios from "axios";

const authStore = useAuthStore();
const products = ref([]);

function setProducts(data) {
  products.value = data;
}

onMounted(async () => {
  await axios.get(`http://localhost:8080/api/v1/products/fetch/best`,
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
</script>

<template>
  <div class="home">
    <!-- component hero -->
    <Hero/>
    <!-- component hero -->
    <div class="row mt-4">
      <div class="col">
        <h2>Terbaik <strong>Makanan</strong></h2>
      </div>
    </div>

    <div class="row mb-4">
      <div class="col-md-4 mt-4" v-for="product in products" :key="product.id">
        <!-- component list product -->
        <ListProductsCustom :product="product"/>
        <!-- component list product -->
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>