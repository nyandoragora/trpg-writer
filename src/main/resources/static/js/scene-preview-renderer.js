// scene-preview-renderer.js

const scenePreviewRenderer = {
    render(previewCard, data) {
        if (!previewCard) return;

        const scene = data.scene;
        const sceneNpcs = data.sceneNpcs || [];
        const infoNames = data.infoNames || [];

        // Clear previous content
        previewCard.innerHTML = '';

        // Set background image
        if (scene.imagePath) {
            previewCard.style.backgroundImage = `url(${scene.imagePath})`;
            previewCard.style.backgroundSize = 'contain';
            previewCard.style.backgroundRepeat = 'no-repeat';
            previewCard.style.backgroundPosition = 'center';
        } else {
            previewCard.style.backgroundImage = 'none';
        }

        // Create overlay for text
        const overlay = document.createElement('div');
        overlay.style.cssText = 'position: absolute; top: 0; left: 0; width: 100%; height: 100%; display: flex; flex-direction: column; justify-content: space-between; padding: 15px; background-color: rgba(0, 0, 0, 0.5);';

        const topSection = document.createElement('div');
        const bottomSection = document.createElement('div');

        // Title
        const title = document.createElement('h5');
        title.className = 'card-title';
        title.textContent = scene.title;
        title.style.color = 'white';
        topSection.appendChild(title);

        // Info List
        const infoCount = document.createElement('p');
        infoCount.className = 'card-text';
        infoCount.textContent = `情報数: ${infoNames.length}`;
        infoCount.style.color = 'white';
        topSection.appendChild(infoCount);

        const infoList = document.createElement('ul');
        infoList.style.cssText = 'color: white; list-style-type: none; padding-left: 0;';
        infoNames.forEach(name => {
            const li = document.createElement('li');
            li.textContent = name;
            infoList.appendChild(li);
        });
        topSection.appendChild(infoList);

        // NPC List
        const npcCount = document.createElement('p');
        npcCount.className = 'card-text';
        npcCount.textContent = `登場NPC: ${sceneNpcs.length}`;
        npcCount.style.color = 'white';
        topSection.appendChild(npcCount);

        const npcList = document.createElement('ul');
        npcList.style.cssText = 'color: white; list-style-type: none; padding-left: 0;';
        sceneNpcs.forEach(sceneNpc => {
            const li = document.createElement('li');
            li.textContent = sceneNpc.npc.name;
            npcList.appendChild(li);
        });
        topSection.appendChild(npcList);

        // GM Info
        const gmInfoTitle = document.createElement('p');
        gmInfoTitle.className = 'card-text';
        gmInfoTitle.textContent = 'GM向け情報:';
        gmInfoTitle.style.color = 'white';
        bottomSection.appendChild(gmInfoTitle);

        const gmInfoContent = document.createElement('p');
        gmInfoContent.className = 'card-text';
        gmInfoContent.textContent = scene.gmInfo || '';
        gmInfoContent.style.color = 'white';
        bottomSection.appendChild(gmInfoContent);

        overlay.appendChild(topSection);
        overlay.appendChild(bottomSection);
        previewCard.appendChild(overlay);
    }
};
