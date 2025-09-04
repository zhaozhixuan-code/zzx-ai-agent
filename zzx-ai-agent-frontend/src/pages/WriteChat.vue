<script setup>
import { ref, onMounted, nextTick } from 'vue'

const chatId = ref('')
const inputMessage = ref('')
const messages = ref([])
const messagesEl = ref(null)
let eventSource = null
const aiAvatar = '📝'

function generateChatId() {
  return 'chat_' + Math.random().toString(36).slice(2) + Date.now().toString(36)
}

function appendMessage(role, content) {
  messages.value.push({ role, content })
}

function startSse(message) {
  if (eventSource) {
    try { eventSource.close() } catch {}
    eventSource = null
  }
  if (!chatId.value) chatId.value = generateChatId()
  const url = `/api/ai/write_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId.value)}`
  eventSource = new EventSource(url)
  appendMessage('user', message)
  inputMessage.value = ''
  let aiBuffer = ''
  appendMessage('ai', '')
  const aiIndex = messages.value.length - 1
  let lastData = ''
  eventSource.onmessage = async (e) => {
    const raw = e?.data
    if (!raw) return

    if (raw === '[DONE]' || raw === 'DONE' || raw === '__END__') {
      try { eventSource.close() } catch {}
      eventSource = null
      return
    }

    if (raw === lastData) return

    let piece = ''
    try {
      const parsed = JSON.parse(raw)
      if (parsed && typeof parsed === 'object') {
        if ('delta' in parsed) {
          piece = String(parsed.delta ?? '')
          aiBuffer += piece
        } else if ('content' in parsed) {
          aiBuffer = String(parsed.content ?? '')
        } else if (typeof parsed.text === 'string') {
          piece = parsed.text
          aiBuffer += piece
        }
      } else if (typeof parsed === 'string') {
        piece = parsed
        aiBuffer += piece
      }
    } catch {
      piece = raw
      aiBuffer += piece
    }

    messages.value[aiIndex].content = aiBuffer
    lastData = raw
    await nextTick()
    try { messagesEl.value?.scrollTo({ top: messagesEl.value.scrollHeight, behavior: 'smooth' }) } catch {}
  }
  eventSource.onerror = () => {
    try { eventSource?.close() } catch {}
    eventSource = null
  }
}

function onSend() {
  const msg = inputMessage.value?.trim()
  if (!msg) return
  startSse(msg)
}

onMounted(() => {
  chatId.value = generateChatId()
})
</script>

<template>
  <div class="chat-page container">
    <header class="topbar card">
      <h2>AI 写作大师</h2>
      <div class="chat-id">ID: {{ chatId }}</div>
    </header>
    <section class="messages card" ref="messagesEl">
      <div
        v-for="(m, idx) in messages"
        :key="idx"
        class="message"
        :class="m.role === 'user' ? 'right' : 'left'"
      >
        <div v-if="m.role !== 'user'" class="avatar">{{ aiAvatar }}</div>
        <div class="bubble">{{ m.content }}</div>
      </div>
    </section>
    <footer class="composer card">
      <input
        v-model="inputMessage"
        type="text"
        placeholder="说点什么..."
        @keyup.enter="onSend"
      />
      <button @click="onSend">发送</button>
    </footer>
  </div>
</template>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding: 16px 0;
}
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  width: 800px;
  margin: 0 auto;
}
.messages {
  height: 520px;
  overflow-y: auto;
  padding: 16px;
  background: var(--color-surface);
  width: 800px;
  margin: 0 auto;
}
.message {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  margin-bottom: 12px;
}
.message.left { justify-content: flex-start; }
.message.right { justify-content: flex-end; }
.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #fee2e2;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-sm);
}
.bubble {
  max-width: 70%;
  padding: 10px 12px;
  border-radius: 10px;
  background: #fff;
  box-shadow: var(--shadow-sm);
  color: #000;
  white-space: pre-wrap;
  text-align: left;
}
.message.right .bubble {
  background: #dbeafe;
}
.composer {
  display: flex;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid var(--color-border);
}
.composer input {
  flex: 1;
  padding: 8px 10px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
}
.composer button {
  padding: 8px 14px;
}
@media (max-width: 768px) {
  .messages { height: 420px; }
}
/* 固定宽度在移动端全宽 */
@media (max-width: 900px) {
  .topbar,
  .messages,
  .composer { width: 100%; }
}
</style>



