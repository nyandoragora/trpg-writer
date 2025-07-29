CREATE TABLE IF NOT EXISTS roles(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS scenarios (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    introduction TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS scenes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    scenario_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    gm_info TEXT, 
    image_path VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

CREATE TABLE IF NOT EXISTS infos (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    scenario_id INT NOT NULL,
    name VARCHAR(255) NOT NULL, -- 情報名用の新しいカラム
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

CREATE TABLE IF NOT EXISTS npcs (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    scenario_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    level INT,
    intelligence VARCHAR(255),
    perception VARCHAR(255),
    position VARCHAR(255),
    language VARCHAR(255),
    popularity VARCHAR(255),
    weakness VARCHAR(255),
    preemptive INT,
    movement VARCHAR(255),
    life_resist INT,
    mind_resist INT,
    impurity INT,
    habitat VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

CREATE TABLE IF NOT EXISTS parts (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    scenario_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    hit INT,
    damage VARCHAR(255),
    evasion INT,
    protection INT,
    life_point INT,
    magic_point INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

CREATE TABLE IF NOT EXISTS skills (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    scenario_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

CREATE TABLE IF NOT EXISTS bootys (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    scenario_id INT NOT NULL,
    dice_num VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

CREATE TABLE IF NOT EXISTS scene_npcs (
    scene_id INT NOT NULL,
    npc_id INT NOT NULL,
    appearance_notes TEXT,
    PRIMARY KEY (scene_id, npc_id),
    FOREIGN KEY (scene_id) REFERENCES scenes(id),
    FOREIGN KEY (npc_id) REFERENCES npcs(id)
);

CREATE TABLE IF NOT EXISTS scene_infos (
    scene_id INT NOT NULL,
    info_id INT NOT NULL,
    display_condition TEXT, -- カラム名を変更
    PRIMARY KEY (scene_id, info_id),
    FOREIGN KEY (scene_id) REFERENCES scenes(id),
    FOREIGN KEY (info_id) REFERENCES infos(id)
);

CREATE TABLE IF NOT EXISTS npc_parts (
    npc_id INT NOT NULL,
    part_id INT NOT NULL,
    PRIMARY KEY (npc_id, part_id),
    FOREIGN KEY (npc_id) REFERENCES npcs(id),
    FOREIGN KEY (part_id) REFERENCES parts(id)
);

CREATE TABLE IF NOT EXISTS npc_skills (
    npc_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (npc_id, skill_id),
    FOREIGN KEY (npc_id) REFERENCES npcs(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);

CREATE TABLE IF NOT EXISTS npc_bootys (
    npc_id INT NOT NULL,
    booty_id INT NOT NULL,
    PRIMARY KEY (npc_id, booty_id),
    FOREIGN KEY (npc_id) REFERENCES npcs(id),
    FOREIGN KEY (booty_id) REFERENCES bootys(id)
);

CREATE TABLE IF NOT EXISTS faqs (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
