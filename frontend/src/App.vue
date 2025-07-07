<template>
  <el-container>
    <el-header style="font-size: 22px; font-weight: bold"
      >GeoServer Leaflet Uygulaması</el-header
    >
    <el-main>
      <LeafletMap />
    </el-main>
  </el-container>
</template>

<script setup>
import LeafletMap from "./components/LeafletMap.vue";
</script>

<style>
/* İsteğe bağlı stil */
</style>
