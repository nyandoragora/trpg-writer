document.addEventListener('DOMContentLoaded', function () {
    // Part (部位) の処理
    const savePartButton = document.getElementById('savePartButton');
    const addPartModalElement = document.getElementById('addPartModal');
    const partsTableBody = document.querySelector('#partsTable tbody');
    const partErrorAlert = document.getElementById('partErrorAlert');

    if (savePartButton) {
        savePartButton.addEventListener('click', function () {
            const partData = {
                scenarioId: document.getElementById('modalScenarioId').value,
                name: document.getElementById('partName').value,
                hit: document.getElementById('partHit').value,
                damage: document.getElementById('partDamage').value,
                evasion: document.getElementById('partEvasion').value,
                protection: document.getElementById('partProtection').value,
                lifePoint: document.getElementById('partLifePoint').value,
                magicPoint: document.getElementById('partMagicPoint').value
            };

            const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch('/api/parts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [header]: token
                },
                body: JSON.stringify(partData)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json()
                        .then(err => {
                            const validationMessages = err.map(e => e.defaultMessage).filter(Boolean).join('<br>');
                            throw new Error(validationMessages || 'サーバーエラーが発生しました。');
                        })
                        .catch(() => {
                            if (response.status === 400) {
                                throw new Error('入力内容を確認してください。必須項目が未入力の可能性があります。');
                            }
                            throw new Error('サーバーからの応答を処理できませんでした。');
                        });
                }
                return response.json().catch(jsonError => {
                    console.warn('Failed to parse JSON response for Part (success path), but status was OK:', jsonError);
                    return null;
                });
            })
            .then(newPart => {
                if (newPart) {
                    const newRow = partsTableBody.insertRow();
                    newRow.innerHTML = `
                        <td><input type="checkbox" name="partIds" value="${newPart.id}" checked></td>
                        <td>${newPart.name}</td>
                        <td>${newPart.hit}</td>
                        <td>${newPart.damage}</td>
                        <td>${newPart.evasion}</td>
                        <td>${newPart.protection}</td>
                        <td>${newPart.lifePoint}</td>
                        <td>${newPart.magicPoint}</td>
                        <td><button type="button" class="btn btn-danger btn-sm delete-part-button" data-part-id="${newPart.id}">削除</button></td>
                    `;
                } else {
                    console.warn('Part creation succeeded, but no new part data was returned (or JSON parsing failed).');
                }
                const addPartForm = document.getElementById('addPartForm');
                if (addPartForm) {
                    addPartForm.reset();
                }
                partErrorAlert.style.display = 'none';
                if (addPartModalElement) {
                    bootstrap.Modal.getInstance(addPartModalElement).hide();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                partErrorAlert.innerHTML = error.message;
                partErrorAlert.style.display = 'block';
            });
        });
    }

    // Skill (特殊能力) の処理
    const saveSkillButton = document.getElementById('saveSkillButton');
    const addSkillModalElement = document.getElementById('addSkillModal');
    const skillsTableBody = document.querySelector('#skillsTable tbody');
    const skillErrorAlert = document.getElementById('skillErrorAlert');

    if (saveSkillButton) {
        saveSkillButton.addEventListener('click', function () {
            const skillData = {
                scenarioId: document.getElementById('modalSkillScenarioId').value,
                name: document.getElementById('skillName').value,
                content: document.getElementById('skillContent').value
            };

            const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch('/api/skills', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [header]: token
                },
                body: JSON.stringify(skillData)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json()
                        .then(err => {
                            const validationMessages = err.map(e => e.defaultMessage).filter(Boolean).join('<br>');
                            throw new Error(validationMessages || 'サーバーエラーが発生しました。');
                        })
                        .catch(() => {
                            if (response.status === 400) {
                                throw new Error('入力内容を確認してください。必須項目が未入力の可能性があります。');
                            }
                            throw new Error('サーバーからの応答を処理できませんでした。');
                        });
                }
                return response.json().catch(jsonError => {
                    console.warn('Failed to parse JSON response for Skill (success path), but status was OK:', jsonError);
                    return null;
                });
            })
            .then(newSkill => {
                if (newSkill) {
                    const newRow = skillsTableBody.insertRow();
                    newRow.innerHTML = `
                        <td><input type="checkbox" name="skillIds" value="${newSkill.id}" checked></td>
                        <td>${newSkill.name}</td>
                        <td>${newSkill.content}</td>
                        <td><button type="button" class="btn btn-danger btn-sm delete-skill-button" data-skill-id="${newSkill.id}">削除</button></td>
                    `;
                } else {
                    console.warn('Skill creation succeeded, but no new skill data was returned (or JSON parsing failed).');
                }
                const addSkillForm = document.getElementById('addSkillForm');
                if (addSkillForm) {
                    addSkillForm.reset();
                }
                skillErrorAlert.style.display = 'none';
                if (addSkillModalElement) {
                    bootstrap.Modal.getInstance(addSkillModalElement).hide();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                skillErrorAlert.innerHTML = error.message;
                skillErrorAlert.style.display = 'block';
            });
        });
    }

    // Booty (戦利品) の処理
    const saveBootyButton = document.getElementById('saveBootyButton');
    const addBootyModalElement = document.getElementById('addBootyModal');
    const bootysTableBody = document.querySelector('#bootysTable tbody');
    const bootyErrorAlert = document.getElementById('bootyErrorAlert');

    if (saveBootyButton) {
        saveBootyButton.addEventListener('click', function () {
            const bootyData = {
                scenarioId: document.getElementById('modalBootyScenarioId').value,
                diceNum: document.getElementById('bootyDiceNum').value,
                content: document.getElementById('bootyContent').value
            };

            const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch('/api/bootys', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [header]: token
                },
                body: JSON.stringify(bootyData)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json()
                        .then(err => {
                            const validationMessages = err.map(e => e.defaultMessage).filter(Boolean).join('<br>');
                            throw new Error(validationMessages || 'サーバーエラーが発生しました。');
                        })
                        .catch(() => {
                            if (response.status === 400) {
                                throw new Error('入力内容を確認してください。必須項目が未入力の可能性があります。');
                            }
                            throw new Error('サーバーからの応答を処理できませんでした。');
                        });
                }
                return response.json().catch(jsonError => {
                    console.warn('Failed to parse JSON response for Booty (success path), but status was OK:', jsonError);
                    return null;
                });
            })
            .then(newBooty => {
                if (newBooty) {
                    const newRow = bootysTableBody.insertRow();
                    newRow.innerHTML = `
                        <td><input type="checkbox" name="bootyIds" value="${newBooty.id}" checked></td>
                        <td>${newBooty.diceNum}</td>
                        <td>${newBooty.content}</td>
                        <td><button type="button" class="btn btn-danger btn-sm delete-booty-button" data-booty-id="${newBooty.id}">削除</button></td>
                    `;
                } else {
                    console.warn('Booty creation succeeded, but no new booty data was returned (or JSON parsing failed).');
                }
                const addBootyForm = document.getElementById('addBootyForm');
                if (addBootyForm) {
                    addBootyForm.reset();
                }
                bootyErrorAlert.style.display = 'none';
                if (addBootyModalElement) {
                    bootstrap.Modal.getInstance(addBootyModalElement).hide();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                bootyErrorAlert.innerHTML = error.message;
                bootyErrorAlert.style.display = 'block';
            });
        });
    }

    // 削除処理
    function addDeleteFunctionality(tableId, buttonClass, entityName, apiPath, idAttribute) {
        const table = document.getElementById(tableId);
        if (!table) return;

        table.addEventListener('click', function(event) {
            if (event.target.classList.contains(buttonClass)) {
                const button = event.target;
                const entityId = button.getAttribute(idAttribute);

                if (confirm(`本当にこの${entityName}を削除しますか？`)) {
                    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                    fetch(`${apiPath}/${entityId}`, {
                        method: 'DELETE',
                        headers: {
                            [header]: token
                        }
                    })
                    .then(response => {
                        if (response.ok) {
                            button.closest('tr').remove();
                        } else {
                            alert(`${entityName}の削除に失敗しました。`);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert(`${entityName}の削除中にエラーが発生しました。`);
                    });
                }
            }
        });
    }

    addDeleteFunctionality('partsTable', 'delete-part-button', '部位', '/api/parts', 'data-part-id');
    addDeleteFunctionality('skillsTable', 'delete-skill-button', '特殊能力', '/api/skills', 'data-skill-id');
    addDeleteFunctionality('bootysTable', 'delete-booty-button', '戦利品', '/api/bootys', 'data-booty-id');
});
