<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useField, useForm } from "vee-validate";
import * as yup from "yup";
import { useAuthStore } from "../stores/authStore";

// schema validator used Yup
const schema = yup.object({
  email: yup.string().email("Email is invalid").required("Email is required"),
  password: yup.string().required("Password is required"),
});

// used `useForm` to validator vee-validate
const { handleSubmit, setErrors, errors, isSubmitting } = useForm({
  validationSchema: schema,
  validateOnSubmit: true,
  validateOnBlur: false,
  validateOnChange: false,
});

// used `useField` to validator vee-validate
const { value: email } = useField("email");
const { value: password } = useField("password");

const router = useRouter();
const authStore = useAuthStore();

// state for error message from backend
const apiError = ref("");

// sumbit
const submit = handleSubmit(async (values) => {
  try {
    const response = await axios.post("http://localhost:8080/api/v1/auth/login", values, {
      withCredentials: true,
    });

    // set token
    authStore.setToken(response.data.data.access_token, response.data.data.refresh_token); // save token to store pinia state if login is successful
    await router.push({ name: 'home' });

  } catch (error) {
    if (error.response && error.response.data) {
      const { status_code, errors } = error.response.data;

      // set error message to state
      if (status_code === 401) {
        apiError.value = errors;
      } else {
        apiError.value = "An unexpected error occurred. Please try again.";
      }
    }
  }
});
</script>

<template>
  <form @submit.prevent="submit" class="form-signin">
    <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>

    <!-- Alert for error from API -->
    <div v-if="apiError" class="alert alert-danger" role="alert">
      {{ apiError }}
    </div>

    <!-- Email Field -->
    <div>
      <label for="inputEmail" class="sr-only">Email address</label>
      <input
          v-model="email"
          type="email"
          id="inputEmail"
          class="form-control"
          placeholder="Email address"
          :class="{ 'is-invalid': errors.email }"
      >
      <span v-if="errors.email" class="text-danger">{{ errors.email }}</span>
    </div>
    <!-- Email Field -->
    <!-- Password Field -->
    <div>
      <label for="inputPassword" class="sr-only">Password</label>
      <input
          v-model="password"
          type="password"
          id="inputPassword"
          class="form-control"
          placeholder="Password"
          :class="{ 'is-invalid': errors.password }"
      />
      <span v-if="errors.password" class="text-danger">{{ errors.password }}</span>
    </div>
    <!-- Password Field -->
    <div>
      <button class="btn btn-lg btn-primary btn-block mt-3" type="submit" :disabled="isSubmitting">
        Sign in
      </button>
    </div>
  </form>
</template>

<style scoped>

</style>