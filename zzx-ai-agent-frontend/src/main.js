import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)

router.afterEach((to) => {
  const defaultTitle = 'ZZX AI Agent'
  const defaultDesc = '包含 AI 写作大师与 AI 超级智能体的对话应用，支持 SSE 实时流式回复。'
  const title = to.meta?.title || defaultTitle
  const desc = to.meta?.description || defaultDesc
  document.title = title

  function setMeta(name, content) {
    let el = document.querySelector(`meta[name="${name}"]`)
    if (!el) {
      el = document.createElement('meta')
      el.setAttribute('name', name)
      document.head.appendChild(el)
    }
    el.setAttribute('content', content)
  }

  function setOg(property, content) {
    let el = document.querySelector(`meta[property="${property}"]`)
    if (!el) {
      el = document.createElement('meta')
      el.setAttribute('property', property)
      document.head.appendChild(el)
    }
    el.setAttribute('content', content)
  }

  setMeta('description', desc)
  setMeta('keywords', 'AI, 智能体, 写作大师, 聊天, Vue3, SSE, 实时, ZZX')
  setOg('og:title', title)
  setOg('og:description', desc)

  let link = document.querySelector('link[rel="canonical"]')
  if (!link) {
    link = document.createElement('link')
    link.setAttribute('rel', 'canonical')
    document.head.appendChild(link)
  }
  link.setAttribute('href', window.location.href)
})

app.use(router).mount('#app')
