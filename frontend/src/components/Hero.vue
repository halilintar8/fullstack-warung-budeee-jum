<script setup>
import {useRouter} from "vue-router";

const router = useRouter();

function goTo() {
  router.push({ name: 'products' });
}
</script>

<template>
  <div class="hero">
    <!--  Desktop  -->
    <div class="d-none d-md-block">
      <div class="row mt-4">
        <div class="col-md-6">
          <div class="d-flex h-100">
            <div class="justify-content-center align-self-center">
              <h2><b>Warung</b> <span class="dejum">BuDeeee'Jum</span></h2>
              <h2>Rasakan Nikmatnya <strong>Hidangan Favorit,</strong></h2>
              <p>Makanan lezat favorit Anda kini hanya sekali suap! Pesan sekarang dan nikmati!!!!!</p>
              <button @click="goTo" class="btn btn-lg btn-success">
                <i class="bi bi-arrow-right-circle"></i>
                Pesan
              </button>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <img :src="`/assets/images/hero.jpg`" alt="hero.jpg" class="img-fluid" width="100%">
        </div>
      </div>
    </div>

    <!--  Mobile  -->
    <div class="d-sm-block d-md-none">
      <div class="row mt-4">
        <div class="col-md-6 mb-3">
          <img :src="`../public/assets/images/hero.jpg`" width="100%">
        </div>
        <div class="col-md-6">
          <div class="d-flex h-100">
            <div class="justify-content-center align-self-center">
              <h2><b>Warung</b> <span class="dejum">BuDeeee'Jum</span></h2>
              <h2>Rasakan Nikmatnya <strong>Hidangan Favorit,</strong></h2>
              <p>Makanan lezat favorit Anda kini hanya sekali suap! Pesan sekarang dan nikmati!!!!!</p>
              <button @click="goTo" class="btn btn-lg btn-success">
                <i class="bi bi-arrow-right-circle"></i>
                Pesan
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>