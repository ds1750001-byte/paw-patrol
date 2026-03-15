# 🐾 Paw Patrol Mod — Minecraft 1.12.2 Forge

## Что умеет мод
- Добавляет Чейза из Щенячьего патруля как живого моба
- **Shift + ПКМ** по Чейзу — переключение режимов:
  - 🐾 **Следует за игроком** — ходит рядом
  - ✋ **Стоит на месте** — ждёт
- 3D модель с оригинальными текстурами из игры PAW Patrol Rescue Run
- Анимация ходьбы и idle (дыхание)
- Звуки волка
- Яйцо призыва в Creative (вкладка "Щенячий Патруль")
- Не исчезает из мира

---

## Как собрать мод

### Требования
- **Java 8** (именно 8-я, не новее!)
- **Git**

### Шаги

1. **Скачай Forge MDK 1.12.2:**
   https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html
   Скачай версию `14.23.5.2847` → `Mdk`

2. **Распакуй MDK** в папку, например `C:\pawpatrol_forge\`

3. **Скопируй файлы из этого архива** в папку MDK:
   - Папку `src/` → заменяй целиком
   - `build.gradle` → заменяй
   - `mcmod.info` → уже внутри src/

4. **Открой командную строку** в папке MDK и выполни:
   ```
   gradlew setupDecompWorkspace
   gradlew build
   ```
   ⚠️ Первый запуск скачает ~1 ГБ файлов, займёт 10-20 минут

5. **Готовый .jar** появится в папке:
   ```
   build/libs/pawpatrol-1.0.0.jar
   ```

6. **Скопируй .jar** в папку `.minecraft/mods/`

---

## Структура проекта
```
src/main/java/com/pawpatrol/mod/
├── PawPatrolMod.java              ← Главный класс мода
├── entity/
│   ├── EntityChase.java           ← Сущность Чейза (AI, Shift+ПКМ)
│   └── EntityAIFollowOwnerTask.java ← AI следования за игроком
├── model/
│   ├── ModelChase.java            ← Модель с анимацией
│   └── ChaseObjLoader.java        ← Загрузчик 3D меша
├── render/
│   └── RenderChase.java           ← Рендерер (тело + рюкзак)
└── init/
    └── EntityInit.java            ← Регистрация сущностей

src/main/resources/assets/pawpatrol/
├── textures/entity/
│   ├── s2_chase.png               ← Текстура тела
│   └── s2_chase_backpack.png      ← Текстура рюкзака
├── models/entity/
│   ├── chase_body.bin             ← 3D меш тела (7158 вершин)
│   └── chase_pack.bin             ← 3D меш рюкзака (576 вершин)
└── lang/
    ├── en_us.lang
    └── ru_ru.lang
```

---

## Как добавить других щенков (Скай, Рокки и т.д.)

1. Скопируй `EntityChase.java` → `EntitySky.java`, замени имя класса и сообщения
2. Скопируй `ModelChase.java` → `ModelSky.java`
3. Добавь их текстуры в `textures/entity/`
4. Зарегистрируй в `EntityInit.java` по аналогии с Чейзом

---

## Возможные проблемы

**`gradlew setupDecompWorkspace` зависает** → Убедись что Java 8, не Java 11/17

**Белая/розовая текстура** → Проверь что файлы `.png` лежат в `assets/pawpatrol/textures/entity/`

**Мод не появляется в игре** → Убедись что `.jar` в папке `mods/` и версия Forge 1.12.2
