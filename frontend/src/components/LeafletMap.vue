<template>
  <div id="map" style="height: 600px; width: 800px"></div>
</template>

<script setup>
import { onMounted } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

onMounted(() => {
  const map = L.map("map").setView([39.0, 34.5], 6);

  // Taban katmanı (OpenStreetMap)
  const baseLayer = L.tileLayer(
    "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    {
      attribution: "© OpenStreetMap contributors",
      minZoom: 5,
      maxZoom: 19,
    }
  ).addTo(map);

  // Mahalle Katmanı
  const mahalleLayer = L.tileLayer.wms(
    "http://localhost:1922/geoserver/harita/wms",
    {
      layers: "harita:mahalleler",
      format: "image/png",
      transparent: true,
      version: "1.3.0",
    }
  );

  //MAHALLE ETİKET KATMANI
  const mahalleLabelLayer = L.tileLayer.wms(
    "http://localhost:1922/geoserver/harita/wms",
    {
      layers: "harita:mahalleler_etiket",
      format: "image/png",
      transparent: true,
      version: "1.3.0",
    }
  );

  // İlçe Katmanı
  const ilceLayer = L.tileLayer.wms(
    "http://localhost:1922/geoserver/harita/wms",
    {
      layers: "harita:ilceler",
      format: "image/png",
      transparent: true,
      version: "1.3.0",
    }
  );

  // Başlangıçta mahalle katmanı gösterilsin
  mahalleLayer.addTo(map);

  // Layer kontrolü
  const overlays = {
    Mahalle: mahalleLayer,
    İlçe: ilceLayer,
  };

  const layerControl = L.control
    .layers(null, overlays, { collapsed: false })
    .addTo(map);

  // Katmanlar arasında sadece birisi aktif olacak şekilde event ayarı
  map.on("overlayadd", function (e) {
    // Seçilen katman dışındakileri kaldır
    Object.values(overlays).forEach((layer) => {
      if (layer !== e.layer && map.hasLayer(layer)) {
        map.removeLayer(layer);
      }
    });
  });
});
</script>

<style scoped>
#map {
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
</style>
