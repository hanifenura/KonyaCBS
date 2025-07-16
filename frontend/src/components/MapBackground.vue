<template>
  <div id="map-background"></div>
</template>

<script setup>
import { onMounted } from "vue";
import L from "leaflet";

onMounted(() => {
  const map = L.map("map-background", {
    center: [38.9637, 35.2433], // Türkiye koordinatları
    zoom: 6,
    zoomControl: false,
    dragging: false,
    scrollWheelZoom: false,
    doubleClickZoom: false,
    boxZoom: false,
    keyboard: false,
    attributionControl: false,
  });

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 18,
  }).addTo(map);
});
</script>

<style scoped>
#map-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1; /* Arkada kalsın */
  filter: blur(2px) brightness(0.8);
}
</style>
