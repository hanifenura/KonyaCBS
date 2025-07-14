<template>
  <header class="app-header">
    <h2>Leaflet GeoServer Uygulaması</h2>
  </header>
  <MapComponent />
</template>

<script setup>
import MapComponent from "./components/LeafletMap.vue";
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
}
</style>
