<script setup>
import { RouterLink, RouterView } from 'vue-router'
const currentYear = new Date().getFullYear()
</script>

<template>
  <div class="layout">
    <header class="nav">
      <div class="container nav-inner">
        <div class="brand">ZZX AI Agent</div>
        <RouterLink to="/">主页</RouterLink>
        <RouterLink to="/write">AI 写作大师</RouterLink>
        <RouterLink to="/manus">AI 超级智能体</RouterLink>
        <div class="spacer"></div>
      </div>
    </header>
    <main class="content container">
      <RouterView />
    </main>
    <footer class="site-footer">
      <div class="container footer-inner">
        <div>© {{ currentYear }} ZZX AI Agent. All rights reserved.</div>
        <div class="contact">
          <span>邮箱：<a href="mailto:53525430x@gmail.com">53525430x@gmail.com</a></span>
          <span>微信：zzx15560194511</span>
        </div>
      </div>
    </footer>
  </div>
  
</template>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.content { padding: 16px 0; flex: 1; }
.site-footer {
  margin-top: 24px;
  border-top: 1px solid var(--color-border);
  background: var(--color-surface);
}
.footer-inner {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 0;
  color: var(--color-muted);
}
.footer-inner a { color: var(--color-primary); }
</style>
