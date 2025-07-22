<template>
  <div class="select-panel-card">
    <h3>Konum Seçimi</h3>
    <label for="ilce">İlçe Seç:</label>
    <select id="ilce" v-model="selectedIlce" @change="ilceSecildi">
      <option disabled value="">İlçe Seçin</option>
      <option v-for="ilce in ilceler" :key="ilce.id" :value="ilce">
        {{ ilce.ad }}
      </option>
    </select>

    <label for="mahalle">Mahalle Seç:</label>
    <select
      id="mahalle"
      v-model="selectedMahalle"
      :disabled="!selectedIlce"
      @change="mahalleSecildi"
    >
      <option disabled value="">Mahalle Seçin</option>
      <option v-for="mahalle in mahalleler" :key="mahalle.id" :value="mahalle">
        {{ mahalle.ad }}
      </option>
    </select>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, onMounted, watch } from "vue";

const selectedIlce = ref("");
const selectedMahalle = ref("");
const ilceler = ref([]);
const mahalleler = ref([]);

const emit = defineEmits(["ilceSecildi", "mahalleSecildi"]);

onMounted(async () => {
  const response = await axios.get("http://localhost:8080/api/ilceler");
  ilceler.value = response.data;
});

const ilceSecildi = async () => {
  selectedMahalle.value = "";
  emit("ilceSecildi", selectedIlce.value.ad, selectedIlce.value);

  const response = await axios.get(
    `http://localhost:8080/api/mahalleler?ilceId=${selectedIlce.value.id}`
  );
  mahalleler.value = response.data;
};

const mahalleSecildi = () => {
  emit("mahalleSecildi", selectedMahalle.value.ad, selectedMahalle.value);
};
</script>

<style scoped>
.select-panel-card {
  border: 1px solid #ccc;
  background-color: #bfe0eaf3;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  min-width: 250px;
  max-height: 470px;
}

label {
  display: block;
  margin-top: 10px;
  font-weight: bold;
  color: #694ebc;
}

.select-panel-card h3 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #6a4ebc;
  font-weight: 600;
}
select {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
}
</style>
