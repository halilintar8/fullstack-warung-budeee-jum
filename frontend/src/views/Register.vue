<script setup>
import {ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";
import * as yup from "yup";
import {useField, useForm} from "vee-validate";

const schema = yup.object({
  email: yup.string().email("Email is invalid").required("Email is required"),
  password: yup.string().required("Password is required"),
  role: yup.string().required("Role is required")
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
const { value: role } = useField("role");

const router = useRouter();

const apiError = ref("");

const sumbit = handleSubmit(async (values) => {
  try{
    await axios.post('http://localhost:8080/api/v1/auth/register', values);

    await router.push('/login');
  } catch (error) {
    if (error.response && error.response.data) {
      const { status_code, errors } = error.response.data;

      if (status_code === 400) {
        apiError.value = errors;
      } else {
        apiError.value = "An unexpected error occurred. Please try again.";
      }
    }
  }
});

</script>

<template>

  <form @submit.prevent="sumbit" class="form-signin">
    <h1 class="h3 mb-3 font-weight-normal">Register</h1>

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
      />
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
    <!-- role Field -->
    <div class="input-group mb-3">
      <div class="input-group-prepend">
        <label class="input-group-text" for="inputGroupSelect01">Role</label>
      </div>
      <select
          v-model="role"
          class="custom-select"
          id="inputGroupSelect01"
          :class="{ 'is-invalid': errors.password }">
        <option selected>Choose...</option>
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
      </select>
      <span v-if="errors.role" class="text-danger">{{ errors.role }}</span>
    </div>
    <!-- role Field -->
    <div>
      <button class="btn btn-lg btn-primary btn-block mt-3" type="submit" :disabled="isSubmitting">
        Submit
      </button>
    </div>
  </form>

</template>

<style scoped>

</style>