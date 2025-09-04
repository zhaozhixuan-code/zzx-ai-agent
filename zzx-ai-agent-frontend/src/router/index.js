import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import WriteChat from '../pages/WriteChat.vue'
import ManusChat from '../pages/ManusChat.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'ZZX AI Agent - 首页',
      description: 'ZZX AI Agent：集中入口，选择 AI 写作大师或 AI 超级智能体，支持 SSE 流式对话。'
    }
  },
  {
    path: '/write',
    name: 'WriteChat',
    component: WriteChat,
    meta: {
      title: 'AI 写作大师 - ZZX AI Agent',
      description: 'AI 写作大师：支持 SSE 打字机效果，进入即生成 chatId，体验沉浸式写作对话。'
    }
  },
  {
    path: '/manus',
    name: 'ManusChat',
    component: ManusChat,
    meta: {
      title: 'AI 超级智能体 - ZZX AI Agent',
      description: 'AI 超级智能体：多步骤流式输出，每个步骤独立消息气泡，实时展示推理过程。'
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router


