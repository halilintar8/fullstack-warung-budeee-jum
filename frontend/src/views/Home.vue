<script setup>
import { useAuthStore } from "../stores/authStore";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();

// State for showed login info
const isLoggedIn = authStore.isAuthenticated;
const token = authStore.token;
const refreshToken = authStore.refreshToken;

function goTo(){
  router.push({ name: 'layout-main' });
}

</script>

<template>
<!--  <div class="container">-->
    <h1>Login Status</h1>
    <p v-if="isLoggedIn">You are logged in! 🎉</p>
    <p v-else>You are not logged in.</p>

    <div v-if="token">
      <h3>Your Token:</h3>
      <p>{{ token }}</p>
      <h3>Your Refresh Token:</h3>
      <p>{{ refreshToken }}</p>
    </div>

    <div class="form-inline my-2 my-md-0">
      <button @click="goTo" type="button" class="btn btn-outline-primary">Got To Application</button>
    </div>

<!--  </div>-->
</template>

<style scoped>

</style>