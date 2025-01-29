<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

function goTo() {
  router.push({ name: 'home' });
}
</script>

<template>
  <div class="not-found">
    <div class="container">
      <div class="d-none d-md-block">
        <div class="row mt-4">
          <div class="col-md-6">
            <div class="d-flex h-100">
              <div class="justify-content-center align-self-center">
                <h2><strong>404 Page not found</strong></h2>
                <div class="form-inline my-2 my-md-0">
                  <button @click="goTo" type="button" class="btn btn-outline-success">Got To Home</button>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <img :src="'../assets/images/not_found.png'" alt="not_found.png" width="100%">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>