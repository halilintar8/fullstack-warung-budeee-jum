<script setup>
import {ref, onMounted} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useAuthStore} from "../stores/authStore";
import axios from "axios";
import {useToast} from 'vue-toast-notification';

const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();
const $toast = useToast();

const productId = route.params.id;
const product = ref({});
const data = ref({});

function setData(data) {
  product.value = data;
}

onMounted(async () => {
  try {
    const response = await axios.get(
        `http://localhost:8080/api/v1/products/${productId}/detail`,
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authStore.token}`,
          },
        }
    );
    setData(response.data.data);
  } catch (error) {
    console.error(error);
  }
});

async function ordered() {
  if (data.value.jumlahPemesanan) {
    await axios.post(`http://localhost:8080/api/v1/product/${productId}/keranjangs/create`, data.value,
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authStore.token}`,
          },
        }
    );
    await router.push({name: 'cart-detail'});
    $toast.success("Pesanan di masukan <b>Dalam Keranjang</b>", {
      type: "success",
      position: "top-right",
      duration: 6000,
      dismissible: true
    });
  } else {
    $toast.warning("<b>Jumlah Pesanan</b> Harus diisi!!!!!", {
      type: "warning",
      position: "top-right",
      duration: 4000,
      dismissible: true,
    });
  }
}

</script>

<template>
  <div class="food-detail">
    <div class="row mt-4">
      <div class="col">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb">
            <li @click="router.push({name: 'layout-main'})" style="cursor: pointer;" class="breadcrumb-item">
              Home
            </li>
            <li @click="router.push({name: 'products'})" style="cursor: pointer;" class="breadcrumb-item">
              Products
            </li>
            <li class="breadcrumb-item active" aria-current="page">
              Food Order
            </li>
          </ol>
        </nav>
      </div>
    </div>

    <div class="row mt-3">
      <div class="col-md-6">
        <img v-bind:src="`../assets/images/${product.gambar}`" class="img-fluid shadow"/>
      </div>
      <div class="col-md-6">
        <h2>
          <strong>{{ product.nama }}</strong>
        </h2>
        <hr>
        <h4>
          Harga:
          <strong>Rp. {{ product.harga }}</strong>
        </h4>
        <form @submit.prevent="ordered" class="mt-4">
          <!-- Pesan Field -->
          <div>
            <label for="countJumlah" class="sr-only">Jumlah Pesan</label>
            <input
                v-model="data.jumlahPemesanan"
                type="number"
                id="countJumlah"
                placeholder="jumlah pemesanan 1 ~ n"
                class="form-control"
            >
          </div>
          <!-- Pesan Field -->
          <!-- Keterangan Field -->
          <div>
            <label for="information" class="sr-only mt-3">Keterangan</label>
            <textarea
                v-model="data.keterangan"
                type="text"
                id="information"
                placeholder="pedas, sedang... minum: dingin, panas"
                class="form-control"
            />
          </div>
          <!-- Keterangan Field -->

          <button class="btn btn-success mt-3" type="submit">
            <i class="bi bi-cart2"></i>
            Pesan
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>