<template>
  <header class="app-header">
    <h2>Leaflet GeoServer Uygulaması</h2>
  </header>

  <div class="main-layout">
    <SelectPanel
      @ilceSecildi="handleIlceSecildi"
      @mahalleSecildi="handleMahalleSecildi"
    />
    <div class="map-section">
      <div id="map" class="map"></div>
      <div class="button-group">
        <button @click="goToManagement" class="management-btn">
          Veri Yönetimi
        </button>
        <button @click="logout" class="logout-btn">Çıkış Yap</button>
      </div>
    </div>

    <div class="info-section">
      <h3>Seçilen Bilgiler</h3>
      <h4>İlçe Bilgileri:</h4>
      <ul v-if="selectedData.ilce">
        <li v-for="(value, key) in selectedData.ilce" :key="key">
          <strong>{{ key }}:</strong> {{ value }}
        </li>
      </ul>
      <p v-else>İlçe bilgisi bulunamadı.</p>

      <h4>Mahalle Bilgileri:</h4>
      <ul v-if="selectedData.mahalle">
        <li v-for="(value, key) in selectedData.mahalle" :key="key">
          <strong>{{ key }}:</strong> {{ value }}
        </li>
      </ul>
      <p v-else>Mahalle bilgisi bulunamadı.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import axios from "axios";
import SelectPanel from "./SelectPanel.vue";

const router = useRouter();

const selectedData = reactive({
  ilce: null,
  mahalle: null,
});

const map = ref(null);

const goToManagement = () => {
  router.push("/management");
};

const logout = () => {
  localStorage.removeItem("token");
  router.push("/auth/login");
};

const checkLayerProperties = async () => {
  const token = localStorage.getItem("token");
  try {
    const ilceParams = {
      service: "WFS",
      version: "1.1.0",
      request: "GetFeature",
      typeName: "harita:ilceler",
      outputFormat: "application/json",
      maxFeatures: 1,
      authkey: token,
    };

    const ilceResponse = await axios.get(
      "http://localhost:8080/api/geoserver-proxy",
      {
        params: ilceParams,
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    console.log(
      "İlçe katmanı örnek özellikler:",
      ilceResponse.data?.features?.[0]?.properties
    );

    const mahalleParams = {
      service: "WFS",
      version: "1.1.0",
      request: "GetFeature",
      typeName: "harita:mahalleler",
      outputFormat: "application/json",
      maxFeatures: 1,
      authkey: token,
    };

    const mahalleResponse = await axios.get(
      "http://localhost:8080/api/geoserver-proxy",
      {
        params: mahalleParams,
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    console.log(
      "Mahalle katmanı örnek özellikler:",
      mahalleResponse.data?.features?.[0]?.properties
    );
  } catch (error) {
    console.error("Katman kontrol hatası:", error);
  }
};

onMounted(() => {
  selectedData.ilce = null;
  selectedData.mahalle = null;
  nextTick(async () => {
    map.value = L.map("map").setView([37.8746, 32.4932], 9);

    await checkLayerProperties();

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "© OpenStreetMap contributors",
      minZoom: 7,
      maxZoom: 19,
    }).addTo(map.value);

    const token = localStorage.getItem("token");

    const ilceLayer = L.tileLayer.wms(
      "http://localhost:8080/api/geoserver-proxy",
      {
        layers: "harita:ilceler",
        format: "image/png",
        transparent: true,
        version: "1.1.1",
        authkey: token,
        styles: "",
        zIndex: 1,
      }
    );

    const mahalleLayer = L.tileLayer.wms(
      "http://localhost:8080/api/geoserver-proxy",
      {
        layers: "harita:mahalleler",
        format: "image/png",
        transparent: true,
        version: "1.1.1",
        authkey: token,
        styles: "",
        zIndex: 2,
      }
    );

    const overlayMaps = {
      İlçeler: ilceLayer,
      Mahalleler: mahalleLayer,
    };

    L.control
      .layers(null, overlayMaps, {
        position: "topright",
        collapsed: false,
      })
      .addTo(map.value);

    ilceLayer.addTo(map.value);
    mahalleLayer.addTo(map.value);

    map.value.on("click", async (e) => {
      const latlng = e.latlng;
      const token = localStorage.getItem("token");

      try {
        const apiResponse = await axios.get(
          "http://localhost:8080/api/location-info",
          {
            params: { lat: latlng.lat, lng: latlng.lng },
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        const apiData = apiResponse.data;

        L.popup()
          .setLatLng(latlng)
          .setContent(
            `<b>API Bilgileri:</b><br>İlçe: ${
              apiData.ilce || "Bilinmiyor"
            }<br>Mahalle: ${apiData.mahalle || "Bilinmiyor"}`
          )
          .openOn(map.value);

        const buffer = 20;
        const point3857 = L.CRS.EPSG3857.project(latlng);
        const cqlFilter = `DWITHIN(geoloc, POINT(${point3857.x} ${point3857.y}), ${buffer}, meters)`;
        const ilceParams = {
          service: "WFS",
          version: "1.1.1",
          request: "GetFeature",
          typeName: "harita:ilceler",
          outputFormat: "application/json",
          cql_filter: cqlFilter,
          srsName: "EPSG:3857",
          maxFeatures: 5,
          authkey: token,
        };

        const ilceResponse = await axios.get(
          "http://localhost:8080/api/geoserver-proxy",
          {
            params: ilceParams,
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log("WFS İlçe yanıtı:", ilceResponse.data);

        const ilceFeatures = ilceResponse.data.features;

        if (ilceFeatures.length > 0) {
          selectedData.ilce = { ...ilceFeatures[0].properties };
        } else {
          selectedData.ilce = null;
        }

        const mahalleParams = {
          service: "WFS",
          version: "1.1.1",
          request: "GetFeature",
          typeName: "harita:mahalleler",
          outputFormat: "application/json",
          cql_filter: cqlFilter,
          maxFeatures: 5,
          srsName: "EPSG:3857",
          authkey: token,
        };

        const mahalleResponse = await axios.get(
          "http://localhost:8080/api/geoserver-proxy",
          {
            params: mahalleParams,
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log("WFS Mahalle yanıtı:", mahalleResponse.data);
        const mahalleFeatures = mahalleResponse.data.features;

        if (mahalleFeatures.length > 0) {
          selectedData.mahalle = { ...mahalleFeatures[0].properties };
        } else {
          selectedData.mahalle = null;
        }
      } catch (error) {
        console.error("Harita tıklama sorgu hatası:", error);
        selectedData.ilce = null;
        selectedData.mahalle = null;
      }
    });
  });
});
</script>

<style scoped>
.main-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  padding: 10px 0;
}

.map-section {
  flex: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
}

#map.map {
  height: 500px;
  width: 600px;
  max-width: 700px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.button-group {
  margin-top: 12px;
  display: flex;
  gap: 10px;
  justify-content: center;
  width: 100%;
  max-width: 700px;
}

.management-btn,
.logout-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.management-btn {
  background-color: #179c4cbc;
}

.management-btn:hover {
  background-color: #179c4c;
}

.logout-btn {
  background-color: #e84e1bc8;
}

.logout-btn:hover {
  background-color: #e84e1b;
}

.info-section {
  flex: 1;
  background-color: #bfe0eaf3;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(80, 127, 31, 0.1);
  min-width: 250px;
  max-height: 470px;
  overflow-y: auto;
}

.info-section h3 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #6a4ebc;
  font-weight: 600;
}

.info-section h4 {
  margin-bottom: 16px;
  margin-top: 12px;
  color: #694ebc;
}

.info-section ul {
  list-style-type: none;
  padding: 0;
  margin: 0 0 15px 0;
}

.info-section li {
  padding: 4px 0;
  border-bottom: 1px solid #ddd;
}

.info-section li:last-child {
  border-bottom: none;
}
</style>
