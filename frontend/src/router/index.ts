import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import LeafletMap from '../components/LeafletMap.vue'
import DataManagement from '../components/DataManagement.vue'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: LeafletMap
    },
    {
        path: '/management',
        name: 'Management',
        component: DataManagement
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router ;