-- roles
INSERT IGNORE INTO roles (id, role) VALUES (1, 'ROLE_PL');
INSERT IGNORE INTO roles (id, role) VALUES (2, 'ROLE_GM');

-- users
INSERT IGNORE INTO users (id, name, email, password, enabled) VALUES (1, '山田 太郎', 'taro.yamada@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);
INSERT IGNORE INTO users (id, name, email, password, enabled) VALUES (2, '鈴木 花子', 'hanako.suzuki@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);
INSERT IGNORE INTO users (id, name, email, password, enabled) VALUES (3, '佐藤 次郎', 'jiro.sato@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', TRUE);

-- scenarios
INSERT IGNORE INTO scenarios (id, user_id, title, introduction) VALUES (1, 1, '古城の秘宝', '忘れ去られた古城に眠るという伝説の秘宝を求める冒険。');
INSERT IGNORE INTO scenarios (id, user_id, title, introduction) VALUES (2, 2, '電脳都市の影', 'サイバーパンク都市を舞台に、巨大企業の陰謀を暴く。');
INSERT IGNORE INTO scenarios (id, user_id, title, introduction) VALUES (3, 1, '森の守り人', '古代の森が病んでいる。その原因を突き止め、森を救う。');

-- scenes
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (1, 1, '城門', '古城の巨大な城門の前に立つ。', '城門の守りは固い。', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (2, 1, '玉座の間', '広大な玉座の間。主のいない玉座が静かに佇んでいる。', '玉座の奥には隠し通路がある。', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (3, 2, 'ネオン街', '煌びやかなネオンが輝く雑踏の中、情報屋と接触する。', '情報屋は裏社会の顔役だ。', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (4, 2, 'ダミーシーン 1', 'これは電脳都市の影のダミーシーン1です。', 'ダミー情報1', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (5, 2, 'ダミーシーン 2', 'これは電脳都市の影のダミーシーン2です。', 'ダミー情報2', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (6, 2, 'ダミーシーン 3', 'これは電脳都市の影のダミーシーン3です。', 'ダミー情報3', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (7, 2, 'ダミーシーン 4', 'これは電脳都市の影のダミーシーン4です。', 'ダミー情報4', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (8, 2, 'ダミーシーン 5', 'これは電脳都市の影のダミーシーン5です。', 'ダミー情報5', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (9, 2, 'ダミーシーン 6', 'これは電脳都市の影のダミーシーン6です。', 'ダミー情報6', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (10, 2, 'ダミーシーン 7', 'これは電脳都市の影のダミーシーン7です。', 'ダミー情報7', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (11, 2, 'ダミーシーン 8', 'これは電脳都市の影のダミーシーン8です。', 'ダミー情報8', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (12, 2, 'ダミーシーン 9', 'これは電脳都市の影のダミーシーン9です。', 'ダミー情報9', '');
INSERT IGNORE INTO scenes (id, scenario_id, title, content, gm_info, image_path) VALUES (13, 2, 'ダミーシーン 10', 'これは電脳都市の影のダミーシーン10です。', 'ダミー情報10', '');

-- infos
INSERT IGNORE INTO infos (id, scenario_id, name, content) VALUES (1, 1, '城主の子孫', 'あなたはこの城の元城主の子孫である。');
INSERT IGNORE INTO infos (id, scenario_id, name, content) VALUES (2, 1, '秘密の通路', '城には秘密の通路があるという噂だ。');
INSERT IGNORE INTO infos (id, scenario_id, name, content) VALUES (3, 2, '旧友の事件', 'あなたの旧友が事件に巻き込まれたらしい。');

-- npcs
INSERT IGNORE INTO npcs (id, scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist, impurity, habitat) VALUES (1, 1, 'ゴブリン', '小柄で緑色の肌をしたモンスター。', 1, '低い', '五感(暗視可能)', '敵対的', '汎用蛮族語、妖魔語', '5/10', '火', 11, '11/-', 3, 3, 2, '森、山、洞窟');
INSERT IGNORE INTO npcs (id, scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist, impurity, habitat) VALUES (2, 1, 'スケルトン', '動く骸骨の戦士。', 2, '低い', '五感', '敵対的', 'なし', '3/8', '打撃', 8, '8/-', 5, 5, 0, '墓地');
INSERT IGNORE INTO npcs (id, scenario_id, name, description, level, intelligence, perception, position, language, popularity, weakness, preemptive, movement, life_resist, mind_resist, impurity, habitat) VALUES (3, 2, '情報屋「K」', '裏社会に精通した謎の男。', 5, '高い', '五感', '中立', '共通語', '12/15', 'なし', 15, '10/-', 10, 10, 0, '都市');

-- parts
INSERT IGNORE INTO parts (id, scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (1, 1, 'こん棒', 5, '1d6', 0, 0, 0, 0);
INSERT IGNORE INTO parts (id, scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (2, 1, '錆びた剣', 8, '1d8', 0, 0, 0, 0);
INSERT IGNORE INTO parts (id, scenario_id, name, hit, damage, evasion, protection, life_point, magic_point) VALUES (3, 2, 'ハンドガン', 10, '2d6', 0, 0, 0, 0);

-- skills
INSERT IGNORE INTO skills (id, scenario_id, name, content) VALUES (1, 1, '強打', '力を込めて殴りつけ、追加ダメージを与える。');
INSERT IGNORE INTO skills (id, scenario_id, name, content) VALUES (2, 2, 'ハッキング', '電子ロックを解除したり、情報を盗み見たりする。');
INSERT IGNORE INTO skills (id, scenario_id, name, content) VALUES (3, 3, '応急手当', '自分や他人の傷を癒す。');

-- bootys
INSERT IGNORE INTO bootys (id, scenario_id, dice_num, content) VALUES (1, 1, '2～6', 'ゴブリンの耳。換金アイテム。');
INSERT IGNORE INTO bootys (id, scenario_id, dice_num, content) VALUES (2, 1, '2～4', '魔法のポーション。ライフを回復する。');
INSERT IGNORE INTO bootys (id, scenario_id, dice_num, content) VALUES (3, 2, '7～10', '謎のデータチップ。');

-- The following tables are join tables and do not have an 'id' column.
-- They are not included in this modification.

-- scene_npcs
INSERT IGNORE INTO scene_npcs (id, scene_id, npc_id, appearance_notes) VALUES (1, 1, 1, '城門を守っている。');
INSERT IGNORE INTO scene_npcs (id, scene_id, npc_id, appearance_notes) VALUES (2, 2, 2, '玉座の間に潜んでいる。');
INSERT IGNORE INTO scene_npcs (id, scene_id, npc_id, appearance_notes) VALUES (3, 3, 3, 'バーのカウンターに座っている。');

-- scene_infos
INSERT IGNORE INTO scene_infos (scene_id, info_id, display_condition) VALUES (1, 1, '城門を調べるとわかる。');
INSERT IGNORE INTO scene_infos (scene_id, info_id, display_condition) VALUES (2, 2, '玉座を調べるとわかる。');
INSERT IGNORE INTO scene_infos (scene_id, info_id, display_condition) VALUES (3, 3, '情報屋「K」と話すとわかる。');

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

-- faqs
INSERT IGNORE INTO faqs (id, question, answer) VALUES (1, 'TRPG Writerとは何ですか？', 'TRPGシナリオの作成、管理、プレイを支援するアプリケーションです。');
INSERT IGNORE INTO faqs (id, question, answer) VALUES (2, '対応しているTRPGシステムは何ですか？', '現在はソード・ワールド2.5に対応しています。今後、他のシステムも追加予定です。');
INSERT IGNORE INTO faqs (id, question, answer) VALUES (3, '作成したシナリオは公開できますか？', '現在はシナリオ公開機能は未実装です。');
INSERT IGNORE INTO faqs (id, question, answer) VALUES (4, '利用料金はかかりますか？', '基本的な機能は無料でご利用いただけます。一部のプレミアム機能やマーケットプレイスでのシナリオ購入には料金が発生する場合があります。');
INSERT IGNORE INTO faqs (id, question, answer) VALUES (5, '動作環境を教えてください。', 'Webブラウザからアクセスできるため、特別なソフトウェアのインストールは不要です。最新のChrome、Firefox、Edge、Safariでの動作を推奨しています。');
