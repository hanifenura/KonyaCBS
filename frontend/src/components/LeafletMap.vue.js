import { onMounted } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
const authKey = import.meta.env.VITE_AUTH_KEY;
onMounted(() => {
    const map = L.map("map").setView([38.0, 32.5], 6);
    const baseLayer = L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "© OpenStreetMap contributors",
        minZoom: 8,
        maxZoom: 19,
    }).addTo(map);
    const konyaLayer = L.tileLayer.wms("http://localhost:1922/api/wms", {
        layers: "harita:konya",
        format: "image/png",
        transparent: true,
        version: "1.3.0",
        fetchOptions: {
            headers: {
                "X-Auth-Key": authKey,
            },
        },
    });
    konyaLayer.addTo(map);
});
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    id: "map",
    ...{ style: {} },
});
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
