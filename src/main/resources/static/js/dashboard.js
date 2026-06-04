/**
 * RSPCM Dashboard JS
 */
(function () {
  'use strict';

  // ── Sidebar toggle ──────────────────────────────────────────────
  const toggleBtn = document.getElementById('sidebarToggle');
  const sidebar   = document.querySelector('.sidebar');
  const overlay   = document.getElementById('sidebarOverlay');

  function openSidebar() {
    sidebar && sidebar.classList.add('open');
    overlay && overlay.classList.add('visible');
    document.body.style.overflow = 'hidden';
  }

  function closeSidebar() {
    sidebar && sidebar.classList.remove('open');
    overlay && overlay.classList.remove('visible');
    document.body.style.overflow = '';
  }

  if (toggleBtn) {
    toggleBtn.addEventListener('click', function () {
      sidebar && sidebar.classList.contains('open') ? closeSidebar() : openSidebar();
    });
  }

  if (overlay) {
    overlay.addEventListener('click', closeSidebar);
  }

  // Close sidebar on ESC
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') closeSidebar();
  });

  // ── Auto-dismiss alerts ─────────────────────────────────────────
  document.querySelectorAll('.alert[data-auto-dismiss]').forEach(function (alert) {
    var delay = parseInt(alert.dataset.autoDismiss, 10) || 4000;
    setTimeout(function () {
      alert.style.transition = 'opacity 0.4s';
      alert.style.opacity = '0';
      setTimeout(function () { alert.remove(); }, 400);
    }, delay);
  });

  // ── Active nav-item highlight (fallback for JS-only navigation) ─
  var currentPath = window.location.pathname;
  document.querySelectorAll('.nav-item[data-href]').forEach(function (item) {
    if (item.dataset.href && currentPath.startsWith(item.dataset.href)) {
      item.classList.add('active');
    }
  });

  // ── Simple tooltip ──────────────────────────────────────────────
  document.querySelectorAll('[data-tooltip]').forEach(function (el) {
    el.setAttribute('title', el.dataset.tooltip);
  });

})();
