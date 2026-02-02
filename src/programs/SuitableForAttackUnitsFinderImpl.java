package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.List;
import java.util.ArrayList;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        // Проходим по всем рядам (строкам) юнитов противника
        for (List<Unit> row : unitsByRow) {
            if (row.isEmpty()) {
                continue;
            }

            // Для армии игрока (левая армия, isLeftArmyTarget = true):
            // Подходящий юнит — тот, который НЕ закрыт слева другим юнитом армии компьютера
            if (isLeftArmyTarget) {
                for (int i = 0; i < row.size(); i++) {
                    Unit currentUnit = row.get(i);
                    // Если это первый юнит в ряду или слева от него нет юнита — он подходит для атаки
                    if (i == 0 || !row.get(i - 1).isAlive()) {
                        suitableUnits.add(currentUnit);
                    }
                }
            }
            // Для армии компьютера (правая армия, isLeftArmyTarget = false):
            // Подходящий юнит — тот, который НЕ закрыт справа другим юнитом армии игрока
            else {
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit currentUnit = row.get(i);
                    // Если это последний юнит в ряду или справа от него нет юнита — он подходит для атаки
                    if (i == row.size() - 1 || !row.get(i + 1).isAlive()) {
                        suitableUnits.add(currentUnit);
                    }
                }
            }
        }

        return suitableUnits;
    }
}
