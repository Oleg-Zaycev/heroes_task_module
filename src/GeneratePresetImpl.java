import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        int remainingPoints = maxPoints;

        // 1. Сортируем юниты по эффективности: сначала attack/cost, затем health/cost
        List<Unit> sortedUnits = new ArrayList<>(unitList);
        sortedUnits.sort((u1, u2) -> {
            // Вычисляем эффективность по атаке
            double efficiencyAttack1 = (double) u1.getBaseAttack() / u1.getCost();
            double efficiencyAttack2 = (double) u2.getBaseAttack() / u2.getCost();

            // Если эффективность атаки различается — сортируем по ней
            if (Double.compare(efficiencyAttack1, efficiencyAttack2) != 0) {
                return Double.compare(efficiencyAttack2, efficiencyAttack1); // убывание
            }

            // Если эффективность атаки равна — сравниваем по эффективности здоровья
            double efficiencyHealth1 = (double) u1.getHealth() / u1.getCost();
            double efficiencyHealth2 = (double) u2.getHealth() / u2.getCost();
            return Double.compare(efficiencyHealth2, efficiencyHealth1); // убывание
        });

        // 2. Набираем армию, соблюдая лимиты
        for (Unit unit : sortedUnits) {
            int maxCountPerType = 11; // Лимит на количество юнитов одного типа
            int count = 0;

            while (count < maxCountPerType && remainingPoints >= unit.getCost()) {
                // Создаём копию юнита
                Unit newUnit = new Unit(
                        unit.getName(),
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        new HashMap<>(unit.getAttackBonuses()),
                        new HashMap<>(unit.getDefenceBonuses()),
                        0, // xCoordinate (назначается позже)
                        0  // yCoordinate (назначается позже)
                );

                army.getUnits().add(newUnit);
                remainingPoints -= unit.getCost();
                count++;
            }
        }

        // 3. Устанавливаем общее количество потраченных очков
        army.setPoints(maxPoints - remainingPoints);

        return null;
    }
}