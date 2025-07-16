<template>
  <header class="app-header">
    <h2>Leaflet GeoServer Uygulaması</h2>
  </header>

  <div class="map-container">
    <div id="map" style="height: 500px; width: 700px"></div>
    <div class="button-group">
      <button @click="goToManagement" class="management-btn">
        Veri Yönetimi
      </button>
      <button @click="logout" class="logout-btn">Çıkış Yap</button>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import axios from "axios";

const router = useRouter();

const goToManagement = () => {
  router.push("/management");
};

const logout = () => {
  localStorage.removeItem("token");
  router.push("/auth/login");
};

onMounted(() => {
  const map = L.map("map").setView([38.0, 32.5], 6);

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors",
    minZoom: 7,
    maxZoom: 19,
  }).addTo(map);

  const ilceLayer = L.tileLayer.wms("http://localhost:8080/api/wms-proxy", {
    layers: "harita:ilceler",
    format: "image/png",
    transparent: true,
    version: "1.3.0",
    attribution: "GeoServer WMS",
  });

  const mahalleLayer = L.tileLayer.wms("http://localhost:8080/api/wms-proxy", {
    layers: "harita:mahalleler",
    format: "image/png",
    transparent: true,
    version: "1.3.0",
    attribution: "GeoServer WMS",
  });

  const overlayMaps = {
    İlçe: ilceLayer,
    Mahalle: mahalleLayer,
  };

  L.control
    .layers(null, overlayMaps, { position: "topright", collapsed: false })
    .addTo(map);

  ilceLayer.addTo(map);
  mahalleLayer.addTo(map);

  map.on("click", async (e) => {
    const { lat, lng } = e.latlng;

    try {
      const response = await axios.get(
        "http://localhost:8080/api/location-info",
        {
          params: { lat, lng },
        }
      );

      const data = response.data;

      const popupContent = `
        <b>Bilgiler:</b><br>
        İlçe: ${data.ilce || "Bilinmiyor"}<br>
        Mahalle: ${data.mahalle || "Bilinmiyor"}
      `;

      L.popup().setLatLng(e.latlng).setContent(popupContent).openOn(map);
    } catch (error) {
      console.error("API hatası:", error);
    }
  });
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

.button-group {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  justify-content: center;
}

.management-btn {
  padding: 10px 20px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.management-btn:hover {
  background-color: #1976d2;
}

.logout-btn {
  padding: 10px 20px;
  background-color: #ef4444;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.logout-btn:hover {
  background-color: #dc2626;
}

.leaflet-control-layers-overlays {
  display: block;
  text-align: left;
}

.leaflet-control-layers-overlays label {
  display: block;
  margin-bottom: 6px;
  text-align: left;
}
</style>
