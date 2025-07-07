<template>
  <div id="map" style="height: 600px; width: 800px"></div>
</template>

<script setup>
import { onMounted } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

onMounted(() => {
  const map = L.map("map").setView([39.0, 34.5], 6);

  const baseLayer = L.tileLayer(
    "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    {
      attribution: "© OpenStreetMap contributors",
      minZoom: 5,
      maxZoom: 19,
    }
  ).addTo(map);

  const konyaLayer = L.tileLayer.wms(
    "http://localhost:1922/geoserver/harita/wms",
    {
      layers: "harita:konya",
      format: "image/png",
      transparent: true,
      version: "1.3.0",
    }
  );

  konyaLayer.addTo(map);

  // const overlays = {
  //   Mahalle: mahalleLayer,
  //   İlçe: ilceLayer,
  // };

  // L.control.layers(null, overlays, { collapsed: false }).addTo(map);

  // map.on("overlayadd", (e) => {
  //   map.eachLayer((layer) => {
  //     if (layer !== baseLayer) map.removeLayer(layer);
  //   });
  //   map.addLayer(e.layer);

  //   if (e.layer === mahalleLayer) selectedLayer = "mahalle";
  //   else if (e.layer === ilceLayer) selectedLayer = "ilce";
  // });
});
</script>

<style scoped>
#map {
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
</style>
