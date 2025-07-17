<template>
  <div class="login-form">
    <h2>Giriş Yap</h2>
    <br />

    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label>Kullanıcı Adı:</label>
        <input type="text" v-model="username" required />
      </div>

      <div class="form-group">
        <label>Şifre:</label>
        <input type="password" v-model="password" required />
      </div>

      <p v-if="errorMessage" style="color: red; font-size: 0.9em">
        {{ errorMessage }}
      </p>

      <button type="submit">Giriş Yap</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      errorMessage: "",
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await fetch("http://localhost:8080/auth/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          localStorage.setItem("token", data.token);
          alert("Giriş başarılı!");
          this.$router.push("/home");
        } else {
          const error = await response.text();
          this.errorMessage = error || "Giriş başarısız.";
        }
      } catch (error) {
        this.errorMessage = "Bir hata oluştu: " + error.message;
      }
    },
  },
};
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.login-form h2 {
  text-align: center;
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1rem;
  text-align: left;
}

input {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #22c55e;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}
</style>
