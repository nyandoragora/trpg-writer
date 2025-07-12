-- roles
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_PL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_GM');

-- users
INSERT IGNORE INTO users (id, name, email, password, enabled) VALUES (1, '山田 太郎', 'taro.yamada@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);
INSERT IGNORE INTO users (id, name, email, password, enabled) VALUES (2, '鈴木 花子', 'hanako.suzuki@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);
INSERT IGNORE INTO users (id, name, email, password, enabled) VALUES (3, '佐藤 次郎', 'jiro.sato@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);

-- scenarios
INSERT IGNORE INTO scenarios (id, user_id, title, introduction) VALUES (1, 1, '古城の秘宝', '忘れ去られた古城に眠るという伝説の秘宝を求める冒険。');
INSERT IGNORE INTO scenarios (id, user_id, title, introduction) VALUES (2, 2, '電脳都市の影', 'サイバーパンク都市を舞台に、巨大企業の陰謀を暴く。');
INSERT IGNORE INTO scenarios (id, user_id, title, introduction) VALUES (3, 1, '森の守り人', '古代の森が病んでいる。その原因を突き止め、森を救う。');

-- scenes
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (1, 1, '城門', '古城の巨大な城門の前に立つ。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (2, 1, '玉座の間', '広大な玉座の間。主のいない玉座が静かに佇んでいる。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (3, 2, 'ネオン街', '煌びやかなネオンが輝く雑踏の中、情報屋と接触する。');
-- Dummy scenes for scenario_id = 2
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (4, 2, 'ダミーシーン 1', 'これは電脳都市の影のダミーシーン1です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (5, 2, 'ダミーシーン 2', 'これは電脳都市の影のダミーシーン2です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (6, 2, 'ダミーシーン 3', 'これは電脳都市の影のダミーシーン3です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (7, 2, 'ダミーシーン 4', 'これは電脳都市の影のダミーシーン4です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (8, 2, 'ダミーシーン 5', 'これは電脳都市の影のダミーシーン5です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (9, 2, 'ダミーシーン 6', 'これは電脳都市の影のダミーシーン6です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (10, 2, 'ダミーシーン 7', 'これは電脳都市の影のダミーシーン7です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (11, 2, 'ダミーシーン 8', 'これは電脳都市の影のダミーシーン8です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (12, 2, 'ダミーシーン 9', 'これは電脳都市の影のダミーシーン9です。');
INSERT IGNORE INTO scenes (id, scenario_id, title, content) VALUES (13, 2, 'ダミーシーン 10', 'これは電脳都市の影のダミーシーン10です。');

-- infos
INSERT IGNORE INTO infos (id, scenario_id, role_id, content) VALUES (1, 1, 1, 'あなたはこの城の元城主の子孫である。');
INSERT IGNORE INTO infos (id, scenario_id, role_id, content) VALUES (2, 1, 2, '城には秘密の通路があるという噂だ。');
INSERT IGNORE INTO infos (id, scenario_id, role_id, content) VALUES (3, 2, 1, 'あなたの旧友が事件に巻き込まれたらしい。');

-- npcs
INSERT IGNORE INTO npcs (id, scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist) VALUES (1, 1, 'ゴブリン', '小柄で緑色の肌をしたモンスター。', 1, 5, 10, '前衛', 'ゴブリン語', 5, '火', 10, 10, 5, 5);
INSERT IGNORE INTO npcs (id, scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist) VALUES (2, 1, 'スケルトン', '動く骸骨の戦士。', 2, 3, 8, '前衛', 'なし', 3, '打撃', 8, 8, 10, 0);
INSERT IGNORE INTO npcs (id, scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist) VALUES (3, 2, '情報屋「K」', '裏社会に精通した謎の男。', 5, 15, 15, '後衛', '共通語', 12, '金', 12, 10, 10, 12);

-- parts
INSERT IGNORE INTO parts (id, scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (1, 1, 'こん棒', 5, '1d6', 0, 0, 0, 0);
INSERT IGNORE INTO parts (id, scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (2, 1, '錆びた剣', 8, '1d8', 0, 0, 0, 0);
INSERT IGNORE INTO parts (id, scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (3, 2, 'ハンドガン', 10, '2d6', 0, 0, 0, 0);

-- skills
INSERT IGNORE INTO skills (id, scenario_id, name, content) VALUES (1, 1, '強打', '力を込めて殴りつけ、追加ダメージを与える。');
INSERT IGNORE INTO skills (id, scenario_id, name, content) VALUES (2, 2, 'ハッキング', '電子ロックを解除したり、情報を盗み見たりする。');
INSERT IGNORE INTO skills (id, scenario_id, name, content) VALUES (3, 3, '応急手当', '自分や他人の傷を癒す。');

-- bootys
INSERT IGNORE INTO bootys (id, scenario_id, dice_num, content) VALUES (1, 1, 6, 'ゴブリンの耳。換金アイテム。');
INSERT IGNORE INTO bootys (id, scenario_id, dice_num, content) VALUES (2, 1, 4, '魔法のポーション。ライフを回復する。');
INSERT IGNORE INTO bootys (id, scenario_id, dice_num, content) VALUES (3, 2, 10, '謎のデータチップ。');

-- The following tables are join tables and do not have an 'id' column.
-- They are not included in this modification.

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