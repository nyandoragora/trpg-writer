-- roles
INSERT IGNORE INTO roles (name) VALUES ('ROLE_PL');
INSERT IGNORE INTO roles (name) VALUES ('ROLE_GM');

-- users
INSERT IGNORE INTO users (name, email, password, enabled) VALUES ('山田 太郎', 'taro.yamada@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);
INSERT IGNORE INTO users (name, email, password, enabled) VALUES ('鈴木 花子', 'hanako.suzuki@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);
INSERT IGNORE INTO users (name, email, password, enabled) VALUES ('佐藤 次郎', 'jiro.sato@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);

-- scenarios
INSERT IGNORE INTO scenarios (user_id, title, introduction) VALUES (1, '古城の秘宝', '忘れ去られた古城に眠るという伝説の秘宝を求める冒険。');
INSERT IGNORE INTO scenarios (user_id, title, introduction) VALUES (2, '電脳都市の影', 'サイバーパンク都市を舞台に、巨大企業の陰謀を暴く。');
INSERT IGNORE INTO scenarios (user_id, title, introduction) VALUES (1, '森の守り人', '古代の森が病んでいる。その原因を突き止め、森を救う。');

-- scenes
INSERT IGNORE INTO scenes (scenario_id, title, content) VALUES (1, '城門', '古城の巨大な城門の前に立つ。');
INSERT IGNORE INTO scenes (scenario_id, title, content) VALUES (1, '玉座の間', '広大な玉座の間。主のいない玉座が静かに佇んでいる。');
INSERT IGNORE INTO scenes (scenario_id, title, content) VALUES (2, 'ネオン街', '煌びやかなネオンが輝く雑踏の中、情報屋と接触する。');

-- infos
INSERT IGNORE INTO infos (scenario_id, role_id, content) VALUES (1, 1, 'あなたはこの城の元城主の子孫である。');
INSERT IGNORE INTO infos (scenario_id, role_id, content) VALUES (1, 2, '城には秘密の通路があるという噂だ。');
INSERT IGNORE INTO infos (scenario_id, role_id, content) VALUES (2, 1, 'あなたの旧友が事件に巻き込まれたらしい。');

-- npcs
INSERT IGNORE INTO npcs (scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist) VALUES (1, 'ゴブリン', '小柄で緑色の肌をしたモンスター。', 1, 5, 10, '前衛', 'ゴブリン語', 5, '火', 10, 10, 5, 5);
INSERT IGNORE INTO npcs (scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist) VALUES (1, 'スケルトン', '動く骸骨の戦士。', 2, 3, 8, '前衛', 'なし', 3, '打撃', 8, 8, 10, 0);
INSERT IGNORE INTO npcs (scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist) VALUES (2, '情報屋「K」', '裏社会に精通した謎の男。', 5, 15, 15, '後衛', '共通語', 12, '金', 12, 10, 10, 12);

-- parts
INSERT IGNORE INTO parts (scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (1, 'こん棒', 5, '1d6', 0, 0, 0, 0);
INSERT IGNORE INTO parts (scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (1, '錆びた剣', 8, '1d8', 0, 0, 0, 0);
INSERT IGNORE INTO parts (scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (2, 'ハンドガン', 10, '2d6', 0, 0, 0, 0);

-- skills
INSERT IGNORE INTO skills (scenario_id, name, content) VALUES (1, '強打', '力を込めて殴りつけ、追加ダメージを与える。');
INSERT IGNORE INTO skills (scenario_id, name, content) VALUES (2, 'ハッキング', '電子ロックを解除したり、情報を盗み見たりする。');
INSERT IGNORE INTO skills (scenario_id, name, content) VALUES (3, '応急手当', '自分や他人の傷を癒す。');

-- bootys
INSERT IGNORE INTO bootys (scenario_id, dice_num, content) VALUES (1, 6, 'ゴブリンの耳。換金アイテム。');
INSERT IGNORE INTO bootys (scenario_id, dice_num, content) VALUES (1, 4, '魔法のポーション。ライフを回復する。');
INSERT IGNORE INTO bootys (scenario_id, dice_num, content) VALUES (2, 10, '謎のデータチップ。');

-- scene_npcs
INSERT IGNORE INTO scene_npcs (scene_id, npc_id, appearance_notes) VALUES (1, 1, '城門を守っている。');
INSERT IGNORE INTO scene_npcs (scene_id, npc_id, appearance_notes) VALUES (2, 2, '玉座の間に潜んでいる。');
INSERT IGNORE INTO scene_npcs (scene_id, npc_id, appearance_notes) VALUES (3, 3, 'バーのカウンターに座っている。');

-- scene_infos
INSERT IGNORE INTO scene_infos (scene_id, info_id, `condition`) VALUES (1, 1, '城門を調べるとわかる。');
INSERT IGNORE INTO scene_infos (scene_id, info_id, `condition`) VALUES (2, 2, '玉座を調べるとわかる。');
INSERT IGNORE INTO scene_infos (scene_id, info_id, `condition`) VALUES (3, 3, '情報屋「K」と話すとわかる。');

-- npc_parts
INSERT IGNORE INTO npc_parts (npc_id, part_id) VALUES (1, 1);
INSERT IGNORE INTO npc_parts (npc_id, part_id) VALUES (2, 2);
INSERT IGNORE INTO npc_parts (npc_id, part_id) VALUES (3, 3);

-- npc_skills
INSERT IGNORE INTO npc_skills (npc_id, skill_id) VALUES (1, 1);
INSERT IGNORE INTO npc_skills (npc_id, skill_id) VALUES (3, 2);

-- npc_bootys
INSERT IGNORE INTO npc_bootys (npc_id, booty_id) VALUES (1, 1);
INSERT IGNORE INTO npc_bootys (npc_id, booty_id) VALUES (2, 2);
INSERT IGNORE INTO npc_bootys (npc_id, booty_id) VALUES (3, 3);