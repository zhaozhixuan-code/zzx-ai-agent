<script setup>
import { useRouter } from 'vue-router'
import { ref, nextTick } from 'vue'

const router = useRouter()

function goWrite() {
  router.push({ name: 'WriteChat' })
}

function goManus() {
  router.push({ name: 'ManusChat' })
}

const logs = ref([
  "=== ZZX AI Terminal ===",
  "Type 'help' to view commands."
]) 
const cmd = ref('')
const termBody = ref(null)

function push(line) {
  logs.value.push(line)
  nextTick(() => {
    try { termBody.value?.scrollTo({ top: termBody.value.scrollHeight }) } catch {}
  })
}

function run() {
  const raw = cmd.value.trim()
  if (!raw) return
  push(`$ ${raw}`)
  const [base, arg] = raw.split(/\s+/, 2)
  switch ((base || '').toLowerCase()) {
    case 'help':
      push('commands: help, about, ls, open <write|manus>, banner, clear')
      break
    case 'about':
      push('ZZX AI Agent: SSE streaming with Write Master and Super Agent.')
      break
    case 'ls':
      push('apps/  write  manus')
      break
    case 'banner':
      push('=== ZZX AI Terminal ===')
      push('apps: write, manus')
      break
    case 'clear':
      logs.value = []
      break
    case 'open':
      if (arg === 'write') goWrite()
      else if (arg === 'manus') goManus()
      else push("usage: open write | open manus")
      break
    default:
      push(`command not found: ${base}`)
  }
  cmd.value = ''
}
</script>

<template>
  <div class="home container">
    <div class="bg-decor" aria-hidden="true">
      <div class="orb orb-blue"></div>
      <div class="orb orb-pink"></div>
      <div class="orb orb-green"></div>
    </div>
    <div class="hero">
      <h1 class="home-title glitch" data-text="ZZX AI 应用中心"><span>ZZX</span> AI 应用中心</h1>
      <p class="home-sub">赛博霓虹 · 实时流式 · 双应用体验</p>
      <div class="badges">
        <span class="badge"># GEEK</span>
        <span class="badge"># CYBERPUNK</span>
        <span class="badge"># STREAMING</span>
      </div>
    </div>
    <div class="cards">
      <div class="app-card card write" @click="goWrite">
        <div class="app-head">
          <div class="app-icon">📝</div>
          <div class="app-title">AI 写作大师</div>
        </div>
        <div class="app-desc">模拟写作场景对话，流式打字机回复，沉浸式互动</div>
        <div class="chips">
          <span class="chip">SSE</span>
          <span class="chip">Realtime</span>
          <span class="chip">ChatID</span>
        </div>
        <div class="actions">
          <button class="btn btn-primary neon" @click.stop="goWrite">立即进入</button>
        </div>
      </div>
      <div class="app-card card manus" @click="goManus">
        <div class="app-head">
          <div class="app-icon">🤖</div>
          <div class="app-title">AI 超级智能体</div>
        </div>
        <div class="app-desc">多步骤推理与执行，每一步皆可视化呈现</div>
        <div class="chips">
          <span class="chip">SSE</span>
          <span class="chip">Agent</span>
          <span class="chip">Steps</span>
        </div>
        <div class="actions">
          <button class="btn btn-primary neon" @click.stop="goManus">立即进入</button>
        </div>
      </div>
    </div>
    <div class="terminal card">
      <div class="term-head">
        <span class="dot red"></span>
        <span class="dot yellow"></span>
        <span class="dot green"></span>
        <span class="title">Terminal</span>
      </div>
      <div class="term-body" ref="termBody">
        <div v-for="(line, i) in logs" :key="i" class="line">{{ line }}</div>
      </div>
      <div class="term-input">
        <span class="prompt">$</span>
        <input v-model="cmd" @keyup.enter="run" placeholder="help | about | ls | banner | clear | open write|manus" />
      </div>
    </div>
  </div>
  
</template>

<style scoped>
.home {
  position: relative;
  padding-top: 40px;
  padding-bottom: 40px;
}
/* 赛博网格背景与光晕 */
.home::before {
  content: '';
  position: absolute;
  inset: 0;
  background:
    radial-gradient(60% 60% at 50% 0%, rgba(37,99,235,0.12), transparent 60%),
    repeating-linear-gradient(
      0deg,
      rgba(100,116,139,0.08) 0px,
      rgba(100,116,139,0.08) 1px,
      transparent 1px,
      transparent 24px
    ),
    repeating-linear-gradient(
      90deg,
      rgba(100,116,139,0.08) 0px,
      rgba(100,116,139,0.08) 1px,
      transparent 1px,
      transparent 24px
    );
  pointer-events: none;
  z-index: 0;
  background-size: auto, auto 26px, 26px auto;
  animation: gridDrift 18s linear infinite;
}
.home::after {
  /* 扫描线 */
  content: '';
  position: absolute;
  inset: 0;
  background: repeating-linear-gradient(
    0deg,
    rgba(15,23,42,0.02) 0px,
    rgba(15,23,42,0.02) 2px,
    transparent 2px,
    transparent 4px
  );
  mix-blend-mode: multiply;
  pointer-events: none;
  z-index: 0;
}
.hero { position: relative; z-index: 1; text-align: center; margin-bottom: 24px; }
.home-title {
  margin: 0 0 8px;
  font-size: 40px;
  line-height: 1.1;
  font-weight: 800;
  letter-spacing: 0.5px;
  background: linear-gradient(90deg, #60a5fa, #34d399, #a78bfa, #f472b6);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  filter: drop-shadow(0 8px 24px rgba(37,99,235,0.25));
}
.home-title span { opacity: 0.9; }
.home-sub { margin: 0; color: var(--color-muted); }

/* 标题 Glitch 故障风格 */
.glitch { position: relative; display: inline-block; }
.glitch::before,
.glitch::after {
  content: attr(data-text);
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  overflow: hidden;
}
.glitch::before {
  text-shadow: -2px 0 rgba(96,165,250,0.8);
  clip-path: polygon(0 0, 100% 0, 100% 45%, 0 55%);
  animation: glitchTop 2.2s infinite ease-in-out alternate;
}
.glitch::after {
  text-shadow: 2px 0 rgba(244,114,182,0.8);
  clip-path: polygon(0 65%, 100% 55%, 100% 100%, 0 100%);
  animation: glitchBottom 2.4s infinite ease-in-out alternate;
}

.cards {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
  margin-top: 20px;
}
.app-card {
  position: relative;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  border: 1px solid transparent;
  backdrop-filter: blur(10px);
  box-shadow: var(--shadow-sm);
  border-radius: 16px;
  transition: transform 240ms ease, box-shadow 240ms ease, border-color 240ms ease, background 240ms ease;
  background-image:
    linear-gradient(180deg, rgba(15,23,42,0.72), rgba(15,23,42,0.5))
    ,conic-gradient(from 0deg, #60a5fa, #a78bfa, #f472b6, #34d399, #60a5fa);
  background-origin: border-box;
  background-clip: padding-box, border-box;
  cursor: pointer;
}
.app-card:hover {
  transform: perspective(800px) rotateX(1deg) rotateY(-2deg) translateY(-8px);
  box-shadow: 0 18px 44px rgba(37,99,235,0.18);
  border-color: transparent;
}
.app-card::after {
  content: '';
  position: absolute;
  inset: -2px;
  border-radius: 18px;
  background: linear-gradient(120deg, rgba(96,165,250,0.65), rgba(167,139,250,0.65), rgba(244,114,182,0.65));
  filter: blur(18px);
  opacity: 0;
  transition: opacity 240ms ease;
  z-index: -1;
}
.app-card:hover::after { opacity: 0.9; }
.app-card::before {
  /* 扫光 */
  content: '';
  position: absolute;
  top: 0;
  left: -30%;
  width: 30%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.5), transparent);
  transform: skewX(-15deg);
  opacity: 0;
}
.app-card:hover::before {
  animation: sweep 800ms ease forwards;
}
.app-head { display: flex; align-items: center; gap: 10px; }
.app-icon { font-size: 22px; filter: drop-shadow(0 4px 12px rgba(0,0,0,0.1)); }
.app-title { font-weight: 700; letter-spacing: 0.2px; }
.app-desc { color: var(--color-muted); font-size: 14px; }
.chips { display: flex; gap: 8px; flex-wrap: wrap; }
.chip {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 999px;
  background: rgba(2,6,23,0.05);
  border: 1px dashed rgba(2,6,23,0.15);
  color: var(--color-muted);
}
.actions { margin-top: 6px; }
.neon {
  position: relative;
  overflow: hidden;
  border: none;
}
.neon::before {
  content: '';
  position: absolute;
  inset: -2px;
  background: conic-gradient(from 0deg, #60a5fa, #a78bfa, #f472b6, #34d399, #60a5fa);
  filter: blur(12px);
  opacity: 0.6;
  z-index: -1;
}
.neon:hover::before { opacity: 0.9; }
.neon { animation: pulseShadow 1.8s ease-in-out infinite; }
.write .app-icon { text-shadow: 0 6px 18px rgba(244,114,182,0.45); }
.manus .app-icon { text-shadow: 0 6px 18px rgba(96,165,250,0.45); }

/* per-card accent themes */
.app-card.write {
  background-image:
    linear-gradient(180deg, rgba(15,23,42,0.72), rgba(15,23,42,0.5))
    ,conic-gradient(from 0deg, #f472b6, #a78bfa, #60a5fa, #f472b6);
}
.app-card.manus {
  background-image:
    linear-gradient(180deg, rgba(15,23,42,0.72), rgba(15,23,42,0.5))
    ,conic-gradient(from 0deg, #60a5fa, #34d399, #a78bfa, #60a5fa);
}

/* gradient titles inside cards */
.app-card .app-title {
  background: linear-gradient(90deg, #60a5fa, #34d399, #a78bfa, #f472b6);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.app-card.write .app-title {
  background: linear-gradient(90deg, #f472b6, #a78bfa, #60a5fa);
  -webkit-background-clip: text;
  background-clip: text;
}
.app-card.manus .app-title {
  background: linear-gradient(90deg, #60a5fa, #34d399, #a78bfa);
  -webkit-background-clip: text;
  background-clip: text;
}

/* chips tweak to neon-dark */
.chip {
  background: rgba(255,255,255,0.06);
  border: 1px dashed rgba(255,255,255,0.18);
  color: #cbd5e1;
}

/* Terminal */
.terminal { margin-top: 24px; overflow: hidden; }
.term-head {
  display: flex; align-items: center; gap: 8px; padding: 10px 12px;
  border-bottom: 1px solid var(--color-border);
}
.dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block; }
.dot.red { background: #ef4444; }
.dot.yellow { background: #f59e0b; }
.dot.green { background: #22c55e; }
.term-head .title { margin-left: auto; color: var(--color-muted); font-size: 12px; }
.term-body { height: 160px; overflow: auto; padding: 12px; font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; font-size: 12px; }
.line { color: var(--color-text); opacity: 0.9; }
.term-input { display: flex; align-items: center; gap: 8px; padding: 10px 12px; border-top: 1px solid var(--color-border); }
.term-input .prompt { color: #22c55e; font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace; }
.term-input input { flex: 1; background: transparent; border: none; outline: none; color: var(--color-text); font-family: inherit; }

@media (max-width: 768px) {
  .home-title { font-size: 28px; }
  .cards { grid-template-columns: 1fr; }
}

@keyframes sweep {
  0% { left: -30%; opacity: 0; }
  40% { opacity: 1; }
  100% { left: 110%; opacity: 0; }
}
@keyframes pulseShadow {
  0%, 100% { box-shadow: 0 0 0 rgba(37,99,235,0); }
  50% { box-shadow: 0 0 24px rgba(37,99,235,0.35); }
}
@keyframes gridDrift {
  0% { background-position: 0 0, 0 0, 0 0; }
  100% { background-position: 0 0, 0 60px, 60px 0; }
}
@keyframes glitchTop {
  0% { transform: translate(0,0); }
  20% { transform: translate(-1px,-1px); }
  40% { transform: translate(1px,0); }
  60% { transform: translate(-1px,1px); }
  80% { transform: translate(1px,-1px); }
  100% { transform: translate(0,0); }
}
@keyframes glitchBottom {
  0% { transform: translate(0,0); }
  20% { transform: translate(1px,1px); }
  40% { transform: translate(-1px,0); }
  60% { transform: translate(1px,-1px); }
  80% { transform: translate(-1px,1px); }
  100% { transform: translate(0,0); }
}
</style>


