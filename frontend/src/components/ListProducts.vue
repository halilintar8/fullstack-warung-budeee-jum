<script setup>
import {useRouter} from "vue-router";

const props = defineProps(['product']);
const router = useRouter();

function goTo() {
  router.push({ name: 'product-detail', params: { id: props.product.id } });
}

</script>

<template>
  <div class="card shadow card-product">
    <img :src="`/assets/images/${product.gambar}`" class="card-img-top" alt="..." loading="lazy"/>
    <div class="card-body">
      <h5 class="card-title">{{ props.product.nama }}</h5>
      <p
          class="card-text"
      >Harga : Rp. {{ props.product.harga }}</p>
      <button @click="goTo" class="btn btn-success">
        <i class="bi bi-cart2"></i>
        Pesan
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>