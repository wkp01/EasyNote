const API_BASE = "/api/notes";
let currentMode = "all"; // all, search, recommend
let currentPage = 0;
const pageSize = 12;

window.onload = () => {
    createShootingStars();
    refreshRecommend(); // 初始化右侧推荐
    loadAllNotes(0);    // 初始化中间列表
};

// --- 核心数据获取 ---
async function loadAllNotes(page = 0) {
    currentMode = "all";
    document.getElementById('pagination').style.display = 'block';
    const res = await fetch(`${API_BASE}/all?page=${page}&size=${pageSize}`);
    const result = await res.json();
    renderMainCards(result.data.content);
    currentPage = page;
    document.getElementById('page-info').innerText = `PAGE: ${page + 1}`;
    document.getElementById('prev-btn').disabled = result.data.first;
    document.getElementById('next-btn').disabled = result.data.last;
}

async function handleSearch() {
    const keyword = document.getElementById('keyword').value;
    if(!keyword) return;
    currentMode = "search";
    document.getElementById('pagination').style.display = 'none';
    const res = await fetch(`${API_BASE}/search?keyword=${keyword}`);
    const result = await res.json();
    renderMainCards(result.data);
}

async function refreshRecommend() {
    const res = await fetch(`${API_BASE}/recommend`);
    const result = await res.json();
    const panel = document.getElementById('recommend-panel');
    panel.innerHTML = result.data.slice(0, 10).map(item => `
        <div class="mini-card" onclick="showDetail(${JSON.stringify(item).replace(/"/g, '&quot;')}, 1)">
            <div style="color:var(--neon-blue)">${item.content}</div>
            <div style="font-size:0.75rem; color:#888; margin-top:4px;">${item.translation.substring(0,20)}...</div>
        </div>
    `).join('');
}

// --- 渲染与交互 ---
function renderMainCards(data) {
    const container = document.getElementById('content-display');
    const inc = (currentMode === "search") ? 2 : 1;
    container.innerHTML = data.map(item => `
        <div class="note-card" onclick="showDetail(${JSON.stringify(item).replace(/"/g, '&quot;')}, ${inc})">
            <div style="color:var(--neon-purple); font-size:0.7rem; position:absolute; top:10px; right:10px;">W:${item.weight}</div>
            <h3 style="margin:0; color:var(--neon-blue);">${item.content}</h3>
            <div style="margin-top:10px; color:#aaa; font-size:0.8rem;"># ${item.tags || 'General'}</div>
        </div>
    `).join('');
}

function showDetail(item, increment) {
    const body = document.getElementById('detail-body');
    body.innerHTML = `
        <h1 style="color:var(--neon-blue); text-align:center;">${item.content}</h1>
        <div style="background:rgba(255,255,255,0.05); padding:20px; border-radius:8px; margin:20px 0; font-size:1.4rem; text-align:center;">
            ${item.translation}
        </div>
        <div style="display:flex; justify-content:space-between; color:#666; font-size:0.9rem;">
            <span>TYPE: ${item.type === 1 ? '词汇' : item.type === 2 ? '短语' : '句子'}</span>
            <span>WEIGHT: ${item.weight}</span>
        </div>
    `;
    document.getElementById('detail-modal').style.display = "block";
    updateWeight(item.id, increment);
}

async function updateWeight(id, inc) {
    await fetch(`${API_BASE}/update?id=${id}&increment=${inc}`, { method: 'PUT' });
    if(currentMode === "recommend") refreshRecommend();
}

// --- 新增数据 ---
async function submitNewNote() {
    const dto = {
        content: document.getElementById('add-content').value,
        translation: document.getElementById('add-translation').value,
        type: parseInt(document.getElementById('add-type').value),
        tags: document.getElementById('add-tags').value
    };
    const res = await fetch(`${API_BASE}/save`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(dto)
    });
    const result = await res.json();
    if(result.code === 888) {
        alert("碎片已同步至云端");
        closeAddModal();
        loadAllNotes(0);
        refreshRecommend();
    }
}

// --- 工具函数 ---
function switchView(view) {
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    document.getElementById('nav-' + view).classList.add('active');
    if(view === 'all') loadAllNotes(0);
}

function changePage(step) { loadAllNotes(currentPage + step); }
function openAddModal() { document.getElementById('add-modal').style.display = 'block'; }
function closeAddModal() { document.getElementById('add-modal').style.display = 'none'; }
function closeDetailModal() { document.getElementById('detail-modal').style.display = 'none'; }

function createShootingStars() {
    const container = document.querySelector('.shooting-stars');
    setInterval(() => {
        const star = document.createElement('div');
        star.className = 'star';
        star.style.top = Math.random() * 60 + '%';
        star.style.left = Math.random() * 100 + '%';
        container.appendChild(star);
        setTimeout(() => star.remove(), 3000);
    }, 1000);
}

async function refreshRecommend(isManual = false) {
    const icon = document.getElementById('sync-icon');
    if(isManual) icon.classList.add('scanning');

    try {
        const res = await fetch(`${API_BASE}/recommend`);
        const result = await res.json();
        const panel = document.getElementById('recommend-panel');

        // 动画感：先清空，模拟扫描过程
        if(isManual) {
            panel.style.opacity = '0.3';
            await new Promise(r => setTimeout(r, 500)); // 故意延迟 0.5s 增加“扫描感”
        }

        panel.innerHTML = result.data.slice(0, 10).map(item => `
            <div class="mini-card" onclick="showDetail(${JSON.stringify(item).replace(/"/g, '&quot;')}, 1)">
                <div style="color:var(--neon-blue)">${item.content}</div>
                <div style="font-size:0.75rem; color:#888; margin-top:4px;">${item.translation.substring(0,20)}...</div>
            </div>
        `).join('');

        panel.style.opacity = '1';
    } finally {
        if(isManual) icon.classList.remove('scanning');
    }
}