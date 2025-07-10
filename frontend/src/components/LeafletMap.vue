<template>
  <div id="map" style="height: 600px; width: 800px"></div>
</template>

<script setup>
import { onMounted } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

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
#map {
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
</style>
