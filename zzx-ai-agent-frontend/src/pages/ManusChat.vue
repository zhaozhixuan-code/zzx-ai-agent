<script setup>
import { ref, nextTick } from 'vue'

const inputMessage = ref('')
const messages = ref([])
const messagesEl = ref(null)
let eventSource = null
const aiAvatar = '🤖'

function appendMessage(role, content) {
  messages.value.push({ role, content })
}

function startSse(message) {
  if (eventSource) {
    try { eventSource.close() } catch {}
    eventSource = null
  }
  const url = `/api/ai/manus?message=${encodeURIComponent(message)}`
  eventSource = new EventSource(url)
  appendMessage('user', message)
  inputMessage.value = ''
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

    let stepText = ''
    try {
      const parsed = JSON.parse(raw)
      if (parsed && typeof parsed === 'object') {
        if (typeof parsed.content === 'string') {
          stepText = parsed.content
        } else if (typeof parsed.text === 'string') {
          stepText = parsed.text
        } else if (typeof parsed.delta === 'string') {
          stepText = parsed.delta
        }
      } else if (typeof parsed === 'string') {
        stepText = parsed
      }
    } catch {
      stepText = raw
    }

    if (stepText) {
      appendMessage('ai', stepText)
    }
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
</script>

<template>
  <div class="chat-page container">
    <header class="topbar card">
      <h2>AI 超级智能体</h2>
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
  background: #e0f2fe;
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


