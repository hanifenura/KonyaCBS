<template>
  <div class="map-container">
    <div id="map" style="height: 600px; width: 800px"></div>
    <button @click="goToManagement" class="management-btn">
      Veri Yönetimi
    </button>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

const router = useRouter();

const goToManagement = () => {
  router.push("/management");
};

onMounted(() => {
  const map = L.map("map").setView([38.0, 32.5], 6);

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors",
    minZoom: 8,
    maxZoom: 19,
  }).addTo(map);

  const konyaLayer = L.tileLayer
    .wms("http://localhost:8080/api/wms-proxy", {
      layers: "harita:konya",
      format: "image/png",
      transparent: true,
      version: "1.3.0",
      attribution: "GeoServer WMS",
    })
    .addTo(map);
});
</script>

<style scoped>
.map-container {
  position: relative;
  display: inline-block;
}

#map {
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.management-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 10px 20px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.management-btn:hover {
  background-color: #1976d2;
}
</style>
