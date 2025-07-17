import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import LeafletMap from '../components/LeafletMap.vue'
import DataManagement from '../components/DataManagement.vue'
import AuthView from '../views/AuthView.vue'
import LoginForm from '../components/LoginForm.vue' 
import RegisterForm from '../components/RegisterForm.vue'

const routes: Array<RouteRecordRaw> = [
    {
    path: '/',
    redirect: '/auth'
    },
    {
    path: '/auth',
    component: AuthView, 
    children: [
      { path: 'login', component: LoginForm },
      { path: 'register', component: RegisterForm }
    ]
    },
    {
        path: '/home',
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

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path.startsWith('/auth') && token) {
    next('/home') 
  } else if (to.path === '/home' && !token) {
    next('/auth/login') 
  } else {
    next()
  }
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  const protectedRoutes = ["/", "/management"];

  if (protectedRoutes.includes(to.path) && !token) {
    next("/auth/login"); 
  } else {
    next(); 
  }
});

export default router ;