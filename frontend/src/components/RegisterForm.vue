<template>
  <div class="register-form">
    <h2>Kayıt Ol</h2>
    <br />
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label>Kullanıcı Adı:</label>
        <input type="text" v-model="username" required />
      </div>

      <div class="form-group">
        <label>Şifre:</label>
        <input type="password" v-model="password" required />
      </div>

      <div class="form-group">
        <label>Şifreyi Tekrarla:</label>
        <input type="password" v-model="confirmPassword" required />
        <p v-if="passwordMismatch" style="color: red; font-size: 0.9em">
          Şifreler eşleşmiyor.
        </p>
      </div>

      <button type="submit" :disabled="passwordMismatch">Kayıt Ol</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      confirmPassword: "",
    };
  },
  computed: {
    passwordMismatch() {
      return this.password !== this.confirmPassword;
    },
  },
  methods: {
    async handleRegister() {
      if (this.passwordMismatch) return;

      try {
        const response = await fetch("http://localhost:8080/auth/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        });

        if (response.ok) {
          alert("Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz.");
          this.$router.push("/auth/login");
        } else {
          const data = await response.text();
          alert("Hata: " + data);
        }
      } catch (error) {
        alert("Bir hata oluştu: " + error.message);
      }
    },
  },
};
</script>

<style scoped>
.register-form {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.register-form h2 {
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
