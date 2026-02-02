package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.List;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        // Размеры игрового поля
        private static final int WIDTH = 27;
        private static final int HEIGHT = 21;
        // 1. Создаём карту препятствий
        boolean[][] blocked = new boolean[WIDTH][HEIGHT];
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()
                    && !unit.equals(attackUnit)
                    && !unit.equals(targetUnit)) {
                blocked[unit.getX()][unit.getY()] = true;
            }
        }

        // 2. Получаем стартовые и целевые координаты
        int startX = attackUnit.getX();
        int startY = attackUnit.getY();
        int targetX = targetUnit.getX();
        int targetY = targetUnit.getY();

        // 3. Проверяем тривиальные случаи
        if (startX == targetX && startY == targetY) {
            return Collections.singletonList(new Edge(startX, startY));
        }

        if (blocked[targetX][targetY]) {
            return Collections.emptyList(); // Цель недоступна
        }

        // 4. Инициализируем структуры для BFS
        Queue<Edge> queue = new LinkedList<>();
        Map<Edge, Edge> parentMap = new HashMap<>();
        Set<Edge> visited = new HashSet<>();

        Edge start = new Edge(startX, startY);
        queue.offer(start);
        visited.add(start);

        // 5. Возможные направления движения (8 направлений: ортогональные + диагональные)
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},      // вверх, вниз, влево, вправо
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // диагонали
        };

        boolean found = false;

        // 6. Основной цикл BFS
        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                // Проверяем границы поля
                if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
                    continue;
                }

                Edge next = new Edge(newX, newY);

                // Если уже посетили или клетка заблокирована — пропускаем
                if (visited.contains(next) || blocked[newX][newY]) {
                    continue;
                }

                // Отмечаем посещение и сохраняем родителя
                visited.add(next);
                parentMap.put(next, current);
                queue.offer(next);

                // Если достигли цели — фиксируем успех
                if (newX == targetX && newY == targetY) {
                    found = true;
                    break;
                }
            }

            if (found) break;
        }

        // 7. Восстанавливаем путь, если нашли цель
        if (!found) {
            return Collections.emptyList();
        }

        List<Edge> path = new ArrayList<>();
        Edge current = new Edge(targetX, targetY);

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        // 8. Разворачиваем путь (от старта к цели)
        Collections.reverse(path);
        return path;
    }
}
