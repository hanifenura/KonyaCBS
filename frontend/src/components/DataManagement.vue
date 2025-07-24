<template>
  <MapBackground />
  <div class="data-management">
    <h2>Veri Yönetimi Sistemi</h2>

    <div class="layer-selection">
      <h3>Katman Seçimi</h3>
      <select v-model="selectedLayer" class="form-select">
        <option value="ilceler">Ilceler</option>
        <option value="mahalleler">Mahalleler</option>
      </select>
    </div>

    <div class="data-table" v-if="tableData.length">
      <h3>Veri Tablosu</h3>
      <table>
        <thead>
          <tr>
            <th v-for="header in tableHeaders" :key="header">{{ header }}</th>
            <th>İşlemler</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in tableData" :key="row.gid">
            <td v-for="header in tableHeaders" :key="header">
              {{ row[header] }}
            </td>
            <td>
              <button @click="editRow(row)" class="btn btn-edit">
                Düzenle
              </button>
              <button @click="deleteRow(row.gid)" class="btn btn-delete">
                Sil
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="add-data">
      <button @click="showAddForm = true" class="btn btn-add">
        Yeni Veri Ekle
      </button>
    </div>

    <div v-if="showAddForm" class="modal">
      <div class="modal-content">
        <h3>{{ editingId ? "Veri Düzenle" : "Yeni Veri Ekle" }}</h3>
        <form @submit.prevent="saveData">
          <div v-for="field in formFields" :key="field" class="form-group">
            <label>{{ field }}</label>
            <input v-model="formData[field]" :type="getInputType(field)" />
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-save">Kaydet</button>
            <button type="button" @click="cancelEdit" class="btn btn-cancel">
              İptal
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <div v-if="totalPages > 1" class="pagination">
    <button v-if="currentPage > 2" @click="changePage(0)">1</button>
    <span v-if="currentPage > 3">...</span>

    <button
      v-for="page in visiblePages"
      :key="page"
      @click="changePage(page)"
      :class="{ active: currentPage === page }"
    >
      {{ page + 1 }}
    </button>

    <span v-if="currentPage < totalPages - 4">...</span>
    <button
      v-if="currentPage < totalPages - 3"
      @click="changePage(totalPages - 1)"
    >
      {{ totalPages }}
    </button>
  </div>
</template>

<script setup>
import MapBackground from "../components/MapBackground.vue";
import { ref, onMounted, watch, computed } from "vue";
import SelectPanel from "../components/SelectPanel.vue";

const selectedLayer = ref("");
const tableData = ref([]);
const tableHeaders = ref([]);
const showAddForm = ref(false);
const editingId = ref(null);
const formData = ref({});
const formFields = ref([]);

const currentPage = ref(0);
const pageSize = 10;
const totalPages = ref(1);

const loadData = async () => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/data/${selectedLayer.value}?page=${currentPage.value}&size=${pageSize}`
    );
    const data = await response.json();
    console.log(data);

    let rows = data.content || [];

    tableData.value = rows;
    totalPages.value = data.totalPages || 1;

    if (rows.length > 0) {
      const allKeys = new Set();
      rows.forEach((item) => {
        Object.keys(item).forEach((key) => {
          if (key !== "id" && key !== "mahalleler") allKeys.add(key);
        });
      });
      tableHeaders.value = [...allKeys];
      formFields.value = [...tableHeaders.value];
    }
  } catch (error) {
    console.error("Veri yüklenirken hata:", error);
  }
};

const deleteRow = async (id) => {
  if (!confirm("Bu veriyi silmek istediğinizden emin misiniz?")) return;
  try {
    await fetch(`http://localhost:8080/api/data/${selectedLayer.value}/${id}`, {
      method: "DELETE",
    });
    await loadData();
  } catch (error) {
    console.error("Veri silinirken hata:", error);
  }
};

const editRow = (row) => {
  editingId.value = row.gid;
  formData.value = { ...row };
  showAddForm.value = true;
};

const saveData = async () => {
  try {
    const method = editingId.value ? "PUT" : "POST";
    const url = editingId.value
      ? `http://localhost:8080/api/data/${selectedLayer.value}/${editingId.value}`
      : `http://localhost:8080/api/data/${selectedLayer.value}`;

    await fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData.value),
    });

    showAddForm.value = false;
    editingId.value = null;
    formData.value = {};
    await loadData();
  } catch (error) {
    console.error("Veri kaydedilirken hata:", error);
  }
};

const cancelEdit = () => {
  showAddForm.value = false;
  editingId.value = null;
  formData.value = {};
};

const getInputType = (field) => {
  if (field.toLowerCase().includes("id")) return "number";
  if (field.toLowerCase().includes("date")) return "date";
  return "text";
};

watch(selectedLayer, loadData);

onMounted(() => {
  if (selectedLayer.value) {
    loadData();
  }
});

const changePage = (page) => {
  currentPage.value = page;
  loadData();
};

const visiblePages = computed(() => {
  const pages = [];
  const maxVisible = 5;
  let start = Math.max(currentPage.value - 2, 0);
  let end = Math.min(start + maxVisible, totalPages.value);

  if (end - start < maxVisible && totalPages.value >= maxVisible) {
    start = Math.max(end - maxVisible, 0);
  }

  for (let i = start; i < end; i++) {
    pages.push(i);
  }

  return pages;
});
</script>

<style scoped>
.data-management {
  position: relative;
  z-index: 10;
  background-color: rgba(255, 255, 255, 0.95);
  padding: 20px;
  border-radius: 8px;
  max-width: 1000px;
  margin: 40px auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}
.layer-selection {
  margin-bottom: 20px;
}
.form-select {
  width: 200px;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
.data-table {
  margin: 20px 0;
  overflow-x: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}
th,
td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}
th {
  background-color: #f5f5f5;
}
.btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  margin: 0 4px;
}
.btn-edit {
  background-color: #179c4c;
  color: white;
}
.btn-delete {
  background-color: #f44336;
  color: white;
}
.btn-add {
  background-color: #2196f3;
  color: white;
  margin-top: 20px;
}
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 500px;
  max-width: 90%;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
}
.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.form-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.btn-save {
  background-color: #4caf50;
  color: white;
}
.btn-cancel {
  background-color: #9e9e9e;
  color: white;
}
.pagination {
  margin-top: 20px;
}
.pagination button {
  margin: 0 5px;
  padding: 6px 12px;
  border-radius: 4px;
  border: 1px solid #ddd;
  background-color: #f9f9f9;
  cursor: pointer;
}
.pagination button.active {
  background-color: #2196f3;
  color: white;
}
</style>
