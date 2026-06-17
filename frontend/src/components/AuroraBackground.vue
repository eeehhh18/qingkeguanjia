<template>
  <main class="aurora-main">
    <div :class="['aurora-container', className]" v-bind="props">
      <div class="aurora-overlay">
        <div :class="['aurora-gradient', showRadialGradient ? 'aurora-radial' : '']"></div>
      </div>
      <slot></slot>
    </div>
  </main>
</template>

<script setup>
defineProps({
  className: { type: String, default: '' },
  showRadialGradient: { type: Boolean, default: true },
  props: { type: Object, default: () => ({}) }
})
</script>

<style scoped>
.aurora-main {
  position: relative;
  min-height: 100vh;
}

.aurora-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary, #f8fafc);
  color: var(--text-primary, #0f172a);
  transition: background 0.3s ease;
}

.dark .aurora-container {
  background: var(--bg-primary-dark, #0f172a);
  color: var(--text-primary-dark, #f8fafc);
}

.aurora-overlay {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.aurora-gradient {
  position: absolute;
  inset: -10px;
  opacity: 0.5;
  filter: blur(10px);
  will-change: transform;
  --white-gradient: repeating-linear-gradient(
    100deg,
    rgba(255,255,255,0.8) 0%,
    rgba(255,255,255,0.8) 7%,
    transparent 10%,
    transparent 12%,
    rgba(255,255,255,0.8) 16%
  );
  --dark-gradient: repeating-linear-gradient(
    100deg,
    rgba(0,0,0,0.8) 0%,
    rgba(0,0,0,0.8) 7%,
    transparent 10%,
    transparent 12%,
    rgba(0,0,0,0.8) 16%
  );
  --aurora-gradient: repeating-linear-gradient(
    100deg,
    rgba(59, 130, 246, 0.4) 10%,
    rgba(129, 140, 248, 0.4) 15%,
    rgba(96, 165, 250, 0.4) 20%,
    rgba(167, 139, 250, 0.4) 25%,
    rgba(59, 130, 246, 0.4) 30%
  );
  background-image: var(--white-gradient), var(--aurora-gradient);
  background-size: 300%, 200%;
  background-position: 50% 50%, 50% 50%;
  animation: aurora-bg 60s linear infinite;
}

.dark .aurora-gradient {
  background-image: var(--dark-gradient), var(--aurora-gradient);
}

.aurora-gradient::after {
  content: '';
  position: absolute;
  inset: 0;
  background-image: var(--white-gradient), var(--aurora-gradient);
  background-size: 200%, 100%;
  background-position: 50% 50%, 50% 50%;
  animation: aurora-bg 60s linear infinite;
  background-attachment: fixed;
  mix-blend-mode: difference;
}

.dark .aurora-gradient::after {
  background-image: var(--dark-gradient), var(--aurora-gradient);
}

.aurora-radial {
  mask-image: radial-gradient(ellipse at 100% 0%, black 10%, transparent 70%);
  -webkit-mask-image: radial-gradient(ellipse at 100% 0%, black 10%, transparent 70%);
}

@keyframes aurora-bg {
  from {
    background-position: 50% 50%, 50% 50%;
  }
  to {
    background-position: 350% 50%, 350% 50%;
  }
}
</style>