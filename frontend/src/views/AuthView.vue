<template>
  <div>
    <MapBackground />
    <div class="auth-container">
      <nav>
        <router-link to="/auth/login">Giriş Yap</router-link>
        <span class="separator">|</span>
        <router-link to="/auth/register">Kayıt Ol</router-link>
      </nav>
      <router-view />
    </div>
  </div>
</template>

<script setup>
import MapBackground from "../components/MapBackground.vue";
</script>

<style scoped>
.auth-container {
  position: relative;
  z-index: 10;
  background-color: rgba(255, 255, 255, 0.85);
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  margin: 1rem auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);

  display: flex;
  flex-direction: column;
  align-items: center;
}

nav {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.1rem;
  gap: 8px;
  height: 40px;
}

nav .separator {
  color: #666;
  user-select: none;
  line-height: 40px;
}

nav a {
  line-height: 40px;
  text-decoration: none;
  color: #3f5fc0;
  font-weight: 600;
}

a:hover {
  transform: scale(1.05);
  transition: all 0.5s ease;
  color: #22c55e;
}
</style>
