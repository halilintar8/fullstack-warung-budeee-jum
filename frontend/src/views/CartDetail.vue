<script setup>
import {ref, onMounted, computed} from "vue";
import {useRouter} from "vue-router";
import {useAuthStore, useCartStore} from "../stores/authStore";
import {useToast} from 'vue-toast-notification';
import axios from "axios";

const router = useRouter();
const authStore = useAuthStore();
const cartStore = useCartStore();
const $toast = useToast();
const carts = ref([]);
const order = ref({});

function setCarts(data) {
  carts.value = data;
  cartStore.setCarts(carts.value);
}

onMounted(async () => {
  try {
    const response = await axios.get("http://localhost:8080/api/v1/keranjangs/fetch",
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authStore.token}`,
          },
        }
    );
    if (response.data.keranjangs === null) {
      $toast.error("Belum ada <b>Pesanan!!!</b>", {
        type: "error",
        position: "top-right",
        duration: 6000,
        dismissible: true
      });
    }
    setCarts(response.data.keranjangs);
  } catch (error) {
    if (error.response.data.status_code === 404) {
      $toast.error("Belum ada <b>Pesanan!!!</b>", {
        type: "error",
        position: "top-right",
        duration: 6000,
        dismissible: true
      });
    }
    // console.error(error);
  }
});

async function cartRemove(productId, cartId) {
  await axios.delete(`http://localhost:8080/api/v1/product/${productId}/keranjangs/${cartId}/remove`,
      {
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${authStore.token}`,
        },
      }).then(async () => {
    $toast.error("Pesanan Berhasil <b>Di Hapus!!!</b>", {
      type: "error",
      position: "top-right",
      duration: 5000,
      dismissible: true
    });

    await axios.get("http://localhost:8080/api/v1/keranjangs/fetch",
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authStore.token}`,
          },
        }).then((response) => {
      if (response.data && response.data.keranjangs) {
        setCarts(response.data.keranjangs);
      } else {
        console.error("rest api do not collect data with structure expected.");
      }
    });
  }).catch((error) => console.error(error));
}

const totalHarga = computed(() => {
  return carts.value.reduce((items, data) => {
    return items + data.products.harga * data.jumlah_pemesanan;
  }, 0);
});

async function checkout() {
  if (order.value.nama && order.value.noMeja) {
    order.value.keranjang_ids = carts.value.map(keranjang => keranjang.id);
    await axios.post(`http://localhost:8080/api/v1/pesanans/create`, order.value,
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authStore.token}`,
          }
        }
    ).then(() => {
      carts.value.map(async function (cart) {
        return await axios.delete(`http://localhost:8080/api/v1/product/${cart.products.id}/keranjangs/${cart.id}/remove`,
            {
              withCredentials: true,
              headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${authStore.token}`,
              }
            }).catch((error) => console.error(error));
      });
      router.push({name: "order-success"});
      $toast.success("Pesanan Anda Sedang <b>Di Proses!!!</b>", {
        type: "success",
        position: "top-right",
        duration: 8000,
        dismissible: true
      });
    }).catch((error) => console.error(error));
  } else {
    $toast.error("Nama Pemesan dan Nomor Meja <b>Wajib di isi!!!</b>", {
      type: "error",
      position: "top-right",
      duration: 5000,
      dismissible: true
    });
  }
}

</script>

<template>
  <div class="keranjang">
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
              Your Cart
            </li>
          </ol>
        </nav>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <h2>
          Keranjang
          <strong>Saya</strong>
        </h2>
        <div class="table-responsive mt-3">
          <table class="table">
            <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Foto</th>
              <th scope="col">Makanan</th>
              <th scope="col">Keterangan</th>
              <th scope="col">Jumlah</th>
              <th scope="col">Harga</th>
              <th scope="col">Total Harga</th>
              <th scope="col">Hapus</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(cart, index) in carts" :key="cart.id">
              <td>{{ index + 1 }}</td>
              <td>
                <img v-bind:src="`../assets/images/${cart.products.gambar}`" class="img-fluid shadow" width="120"/>
              </td>
              <td><strong>{{ cart.products.nama }}</strong></td>
              <td>{{ cart.keterangan ? cart.keterangan : "-" }}</td>
              <td>{{ cart.jumlah_pemesanan }}</td>
              <td>Rp. {{ cart.products.harga }}</td>
              <td><strong>Rp. {{ cart.products.harga * cart.jumlah_pemesanan }}</strong></td>
              <td class="text-danger" style="cursor: pointer">
                <i @click="cartRemove(cart.products.id, cart.id)" class="bi bi-trash"></i>
              </td>
            </tr>
            <tr>
              <td>Daftar Total Harga :</td>
              <td style="color: #6CB52D; font-weight: bold; text-decoration: underline; text-underline-offset: 0.4rem;">
                <template v-if="totalHarga !== 0">
                  Rp. {{ totalHarga }}
                </template>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="row justify-content-end">
      <div class="col-md-4">
        <form @submit.prevent="checkout" class="mt-4">
          <!-- Nama Pesan Field -->
          <div>
            <label for="nama" class="sr-only">Nama Pemesan</label>
            <input
                type="text"
                id="nama"
                class="form-control"
                v-model="order.nama"
            >
          </div>
          <!-- Nama Pesan Field -->
          <!-- No Meja Field -->
          <div>
            <label for="no_meja" class="sr-only mt-3">Nomor Meja</label>
            <textarea
                type="text"
                id="no_meja"
                class="form-control"
                v-model="order.noMeja"
            />
          </div>
          <!-- No Meja Field -->

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