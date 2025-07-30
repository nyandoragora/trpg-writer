document.addEventListener('DOMContentLoaded', function() {
    // グローバルオブジェクトと、それが持つべきデータがなければ何もしない
    if (!window.trpgWriter || !window.trpgWriter.data) {
        return;
    }

    const { fetchWithCsrf, data } = window.trpgWriter;

    const npcDetailModal = document.getElementById('npcDetailModal');
    if (npcDetailModal) {
        const deleteNpcConfirmModal = new bootstrap.Modal(document.getElementById('deleteNpcConfirmModal'));

        npcDetailModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const npcId = button.getAttribute('data-npc-id');
            const modalBody = npcDetailModal.querySelector('.modal-body');
            modalBody.innerHTML = '<p>読み込み中...</p>';

            // この時点では `window.trpgWriter.data` は確実に存在しているはず
            const { allNpcs, allParts, allSkills, allBootys } = data;
            const npc = allNpcs.find(n => n.id == npcId);

            if (npc) {
                let content = `
                    <h4>${npc.name} (Lv: ${npc.level})</h4>
                    <p><strong>説明:</strong> ${npc.description || 'なし'}</p>
                    <hr>
                    <div class="row">
                        <div class="col-md-6"><strong>知能:</strong> ${npc.intelligence}</div>
                        <div class="col-md-6"><strong>知覚:</strong> ${npc.perception}</div>
                    </div>
                    <div class="row">
                        <div class="col-md-6"><strong>反応:</strong> ${npc.position}</div>
                        <div class="col-md-6"><strong>穢れ:</strong> ${npc.impurity}</div>
                    </div>
                    <div class="row">
                        <div class="col-md-6"><strong>言語:</strong> ${npc.language}</div>
                        <div class="col-md-6"><strong>生息地:</strong> ${npc.habitat}</div>
                    </div>
                    <div class="row">
                        <div class="col-md-6"><strong>知名度/弱点値:</strong> ${npc.popularity}</div>
                        <div class="col-md-6"><strong>弱点:</strong> ${npc.weakness}</div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><strong>先制値:</strong> ${npc.preemptive}</div>
                        <div class="col-md-4"><strong>移動速度:</strong> ${npc.movement}</div>
                        <div class="col-md-4"><strong>生命抵抗力:</strong> ${npc.lifeResist}</div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><strong>精神抵抗力:</strong> ${npc.mindResist}</div>
                    </div>
                    <hr>
                `;

                const parts = allParts.filter(p => npc.partIds.includes(p.id));
                if(parts.length > 0) {
                    content += '<h5>部位・攻撃方法</h5><table class="table table-sm"><thead><tr><th>部位名</th><th>命中力</th><th>打点</th><th>回避力</th><th>防護点</th><th>HP</th><th>MP</th></tr></thead><tbody>';
                    parts.forEach(part => {
                        content += `<tr><td>${part.name}</td><td>${part.hit}</td><td>${part.damage}</td><td>${part.evasion}</td><td>${part.protection}</td><td>${part.lifePoint}</td><td>${part.magicPoint}</td></tr>`;
                    });
                    content += '</tbody></table>';
                }

                const skills = allSkills.filter(s => npc.skillIds.includes(s.id));
                if(skills.length > 0) {
                    content += '<h5>特殊能力</h5><table class="table table-sm"><thead><tr><th>能力名</th><th>内容</th></tr></thead><tbody>';
                    skills.forEach(skill => {
                        content += `<tr><td>${skill.name}</td><td>${skill.content}</td></tr>`;
                    });
                    content += '</tbody></table>';
                }

                const bootys = allBootys.filter(b => npc.bootyIds.includes(b.id));
                if(bootys.length > 0) {
                    content += '<h5>戦利品</h5><table class="table table-sm"><thead><tr><th>ダイス</th><th>内容</th></tr></thead><tbody>';
                    bootys.forEach(booty => {
                        content += `<tr><td>${booty.diceNum}</td><td>${booty.content}</td></tr>`;
                    });
                    content += '</tbody></table>';
                }

                modalBody.innerHTML = content;
            } else {
                modalBody.innerHTML = '<p>NPC情報が見つかりませんでした。</p>';
            }
            
            const deleteNpcBtn = document.getElementById('deleteNpcBtn');
            deleteNpcBtn.setAttribute('data-npc-id-to-delete', npcId);
        });

        document.getElementById('deleteNpcBtn').addEventListener('click', (event) => {
            const npcId = event.currentTarget.getAttribute('data-npc-id-to-delete');
            const confirmBtn = document.getElementById('confirmNpcDeleteBtn');
            confirmBtn.setAttribute('data-npc-id-to-delete', npcId);
            deleteNpcConfirmModal.show();
        });

        document.getElementById('confirmNpcDeleteBtn').addEventListener('click', async (event) => {
            const npcIdToDelete = event.currentTarget.getAttribute('data-npc-id-to-delete');
            if (npcIdToDelete) {
                const { scenarioId, sceneId } = data;
                try {
                    const response = await fetchWithCsrf(`/scenarios/${scenarioId}/scenes/${sceneId}/npcs/${npcIdToDelete}/delete`, {
                        method: 'POST'
                    });

                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert('NPCの削除に失敗しました。');
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('エラーが発生しました。');
                }
            }
        });
    }
});

